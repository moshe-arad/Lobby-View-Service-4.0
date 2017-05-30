package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;
import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.InitGameRoomCompletedEvent;
import org.moshe.arad.kafka.events.UserAddedAsSecondPlayerEvent;
import org.moshe.arad.services.UsersView;
import org.moshe.arad.services.LobbyViewChanges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class UserAddedAsSecondPlayerEventConsumer extends SimpleEventsConsumer {

//	@Autowired
//	private LobbyViewOld lobbyView;
	
	@Autowired
	private UsersView lobbyView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(UserAddedAsSecondPlayerEventConsumer.class);
	
	public UserAddedAsSecondPlayerEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		UserAddedAsSecondPlayerEvent userAddedAsSecondPlayerEvent = convertJsonBlobIntoEvent(record.value());
		LobbyViewChanges lobbyViewChanges = context.getBean(LobbyViewChanges.class);
		
		try{			
			logger.info("Will add game room...");
			lobbyView.addGameRoom(userAddedAsSecondPlayerEvent.getGameRoom());
			lobbyView.addUserAsSecondPlayer(userAddedAsSecondPlayerEvent.getUsername(), userAddedAsSecondPlayerEvent.getGameRoom().getName());
			
			lobbyViewChanges.getAddSecondPlayer().put(userAddedAsSecondPlayerEvent.getGameRoom().getName(), userAddedAsSecondPlayerEvent.getUsername());
			lobbyView.markNeedToUpdateGroupUsers(lobbyViewChanges, "lobby");
			
			InitGameRoomCompletedEvent initGameRoomCompletedEvent = context.getBean(InitGameRoomCompletedEvent.class);
			initGameRoomCompletedEvent.setUuid(userAddedAsSecondPlayerEvent.getUuid());
			initGameRoomCompletedEvent.setArrived(new Date());
			initGameRoomCompletedEvent.setClazz("InitGameRoomCompletedEvent");
			initGameRoomCompletedEvent.setGameRoom(userAddedAsSecondPlayerEvent.getGameRoom());
			
			consumerToProducerQueue.getEventsQueue().put(initGameRoomCompletedEvent);
			
			logger.info("Game room added to view");
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private UserAddedAsSecondPlayerEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, UserAddedAsSecondPlayerEvent.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
		this.consumerToProducerQueue = consumerToProducerQueue;
	}

}
