package org.moshe.arad.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.moshe.arad.entities.GameRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class LobbyViewSimple {

	private RedisTemplate<String, String> redisTemplate;
	
	public static final String GAME_ROOMS = "GameRooms";
	public static final String USERS_OPENED_BY = "UsersOpenedBy";
	public static final String USERS_SECOND_PLAYER = "UsersSecondPlayer";
	public static final String USERS_WATCHERS = "UsersWatchers";
	
	private Logger logger = LoggerFactory.getLogger(LobbyViewSimple.class);
	
	@Autowired
    public LobbyViewSimple(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
	
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
	
	public void deleteOpenedByUser(String username){
		redisTemplate.opsForHash().delete(USERS_OPENED_BY, username);
	}
	
	public void deleteSecondUser(String username){
		redisTemplate.opsForHash().delete(USERS_SECOND_PLAYER, username);
	}
	
	public void addUserAsWatcher(String username, String gameRoomName){
		redisTemplate.opsForHash().put(USERS_WATCHERS, username, gameRoomName);
	}
	
	public void addUserAsSecondPlayer(String username, String gameRoomName){
		redisTemplate.opsForHash().put(USERS_SECOND_PLAYER, username, gameRoomName);
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
	
	public List<GameRoom> getAllGameRooms(){
		List<GameRoom> result = new ArrayList<>(1000000);
		
		Set<Map.Entry<Object,Object>> entries = redisTemplate.opsForHash().entries(GAME_ROOMS).entrySet();
		Iterator<Map.Entry<Object,Object>> it = entries.iterator();
		
		while(it.hasNext()){
			Map.Entry<Object,Object> entry = it.next();
			ObjectMapper objectMapper = new ObjectMapper();
			GameRoom room = null;
			try {
				room = objectMapper.readValue(entry.getValue().toString(), GameRoom.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			result.add(room);
		}
		
		return result;
	}
	
	public GameRoom getGameRoom(String gameRoomName){
		String gameRoomJson = redisTemplate.opsForHash().get(GAME_ROOMS, gameRoomName).toString();
		ObjectMapper objectMapper = new ObjectMapper();
		GameRoom room = null;
		try {
			room = objectMapper.readValue(gameRoomJson, GameRoom.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return room;
	}
}
