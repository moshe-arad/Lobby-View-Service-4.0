package org.moshe.arad.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.moshe.arad.entities.GameRoom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LobbyView {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	public static final String GAME_ROOMS = "GameRooms";
	public static final String USERS_OPENED_BY = "UsersOpenedBy";
	public static final String USERS_WATCHERS = "UsersWatchers";
	
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
}
