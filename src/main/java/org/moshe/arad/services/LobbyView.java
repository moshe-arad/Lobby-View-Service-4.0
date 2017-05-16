package org.moshe.arad.services;

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
}