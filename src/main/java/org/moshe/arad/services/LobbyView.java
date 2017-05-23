package org.moshe.arad.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.kafka.events.GetLobbyUpdateViewAckEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LobbyView implements Callable<Set<String>>{

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private ApplicationContext context;
	
	public static final String GAME_ROOMS = "GameRooms";
	public static final String USERS_OPENED_BY = "UsersOpenedBy";
	public static final String USERS_WATCHERS = "UsersWatchers";
	public static final String NEED_TO_UPDATE = "NeedToUpdate";
	public static final String DELETE_WATCHER = "DeleteWatcher";
	public static final String DELETE_GAME_ROOM = "DeleteGameRoom";
	public static final String ADD_GAME_ROOM = "AddGameRoom";
	
	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
	
	private Logger logger = LoggerFactory.getLogger(LobbyView.class);
	
	public boolean isGameRoomExist(String gameRoomName){
		return redisTemplate.opsForHash().hasKey(GAME_ROOMS, gameRoomName);
	}
	
	public void removeGameRoom(String gameRoomName){
		if(this.isGameRoomExist(gameRoomName)) redisTemplate.opsForHash().delete(GAME_ROOMS, gameRoomName);
	}
	
	public void addGameRoom(GameRoom gameRoom){
		if(isGameRoomExist(gameRoom.getName())) removeGameRoom(gameRoom.getName());
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			redisTemplate.opsForHash().put(GAME_ROOMS, gameRoom.getName(), objectMapper.writeValueAsString(gameRoom));
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		}
	}
	
	public void addOpenedByUser(GameRoom gameRoom, String username){
		redisTemplate.opsForHash().put(USERS_OPENED_BY, username, gameRoom.getName());
	}
	
	public void deleteGameRoom(GameRoom gameRoom){
		redisTemplate.opsForHash().delete(GAME_ROOMS, gameRoom.getName());
	}
	
	public void deleteOpenedByUser(GameRoom gameRoom, String username){
		redisTemplate.opsForHash().delete(USERS_OPENED_BY, username);
	}
	
	public void addUserAsWatcher(String username, String gameRoomName){
		redisTemplate.opsForHash().put(USERS_WATCHERS, username, gameRoomName);
	}

	public List<GameRoom> getAllGameRooms() {
		List<GameRoom> result = new ArrayList<>(100000);
		List<Object> temp = redisTemplate.opsForHash().values(GAME_ROOMS);
		ObjectMapper objectMapper = new ObjectMapper();
		ListIterator<Object> it = temp.listIterator();
		while(it.hasNext()){
			String jsonRoom = it.next().toString();
			try {
				result.add(objectMapper.readValue(jsonRoom, GameRoom.class));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public void markWatcherRemoveUpdateView(String gameRoomName, String watcher){
		redisTemplate.opsForHash().put(NEED_TO_UPDATE + ":" + DELETE_WATCHER, gameRoomName, watcher);
	}
	
	public void markGameRoomClosedUpdateView(String gameRoomName){
		redisTemplate.opsForHash().put(NEED_TO_UPDATE + ":" + DELETE_GAME_ROOM, gameRoomName, gameRoomName);
	}
	
	public void markGameRoomOpenedUpdateView(GameRoom gameRoom){
		ObjectMapper objectMapper = new ObjectMapper();
		String gameRoomJson = null;
		try {
			gameRoomJson = objectMapper.writeValueAsString(gameRoom);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		redisTemplate.opsForHash().put(NEED_TO_UPDATE + ":" + ADD_GAME_ROOM, gameRoom.getName(), gameRoomJson);
	}
	
	public void deleteWatcherFromGameRoom(String watcher){
		String gameRoomName = (String) redisTemplate.opsForHash().get(USERS_WATCHERS, watcher);
		redisTemplate.opsForHash().delete(USERS_WATCHERS, watcher);
		ObjectMapper objectMapper = new ObjectMapper();
		String gameRoomJson = (String) redisTemplate.opsForHash().get(GAME_ROOMS, gameRoomName);
		try {
			GameRoom gameRoom = objectMapper.readValue(gameRoomJson, GameRoom.class);
			gameRoom.getWatchers().remove(watcher);
			gameRoomJson = objectMapper.writeValueAsString(gameRoom);
			redisTemplate.opsForHash().put(GAME_ROOMS, gameRoomName, gameRoomJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GetLobbyUpdateViewAckEvent getNeedToUpdate(){
		GetLobbyUpdateViewAckEvent result = context.getBean(GetLobbyUpdateViewAckEvent.class);
		UpdateViewJob updateViewJob = context.getBean(UpdateViewJob.class);
		
		Future<Set<String>> keysFuture = executor.submit(updateViewJob);
		Set<String> keys = null;
		try {
			keys = keysFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} 

		Iterator<String> it = keys.iterator();
		
		while(it.hasNext()){
			String key = it.next();
			
			if(key.equals(NEED_TO_UPDATE + ":" + DELETE_WATCHER)){
				result.setDeleteWatchers(redisTemplate.opsForHash().entries(NEED_TO_UPDATE + ":" + DELETE_WATCHER));
				redisTemplate.delete(NEED_TO_UPDATE + ":" + DELETE_WATCHER);
			}
			else if(key.equals(NEED_TO_UPDATE + ":" + DELETE_GAME_ROOM)){
				redisTemplate.opsForHash().entries(NEED_TO_UPDATE + ":" + DELETE_GAME_ROOM).keySet().stream()
				.forEach((Object itemKey) -> {result.getGameRoomsDelete().add(itemKey.toString());});
				redisTemplate.delete(NEED_TO_UPDATE + ":" + DELETE_GAME_ROOM);
			}
			else if(key.equals(NEED_TO_UPDATE + ":" + ADD_GAME_ROOM)){
				redisTemplate.opsForHash().entries(NEED_TO_UPDATE + ":" + ADD_GAME_ROOM).values().stream()
				.forEach((Object room) -> {
					ObjectMapper objectMapper = new ObjectMapper();
					GameRoom gameRoom = null;
					try {
						gameRoom = objectMapper.readValue(room.toString(), GameRoom.class);
					} catch (IOException e) {
						e.printStackTrace();
					}
					result.getGameRoomsAdd().add(gameRoom);
					});
				redisTemplate.delete(NEED_TO_UPDATE + ":" + ADD_GAME_ROOM);
			}
		}
		
		return result;
	}
	
	@Override
	public Set<String> call() throws Exception {
		Set<String> keys = null;
		
		while(true){
			keys = redisTemplate.keys(NEED_TO_UPDATE + ":*");
			if(keys != null && !keys.isEmpty()){
				break;
			}
			else Thread.sleep(50);
		}
		return keys;
	}
}
