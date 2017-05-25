package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.GameRoomClosedEvent;
import org.moshe.arad.kafka.events.NewGameRoomOpenedEvent;
import org.moshe.arad.kafka.events.NewGameRoomOpenedEventAck;
import org.moshe.arad.kafka.events.UserAddedAsWatcherEvent;
import org.moshe.arad.services.LobbyView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class UserAddedAsWatcherEventConsumer extends SimpleEventsConsumer {

	@Autowired
	private LobbyView lobbyView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(UserAddedAsWatcherEventConsumer.class);
	
	public UserAddedAsWatcherEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		UserAddedAsWatcherEvent userAddedAsWatcherEvent = convertJsonBlobIntoEvent(record.value());
		
		try{			
			logger.info("Will add game room...");
			lobbyView.addGameRoom(userAddedAsWatcherEvent.getGameRoom());
			lobbyView.addUserAsWatcher(userAddedAsWatcherEvent.getUsername(), userAddedAsWatcherEvent.getGameRoom().getName());
			lobbyView.markWatcherAddUpdateView(userAddedAsWatcherEvent.getGameRoom().getName(), userAddedAsWatcherEvent.getUsername());
			logger.info("Game room added to view");
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private UserAddedAsWatcherEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, UserAddedAsWatcherEvent.class);
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
