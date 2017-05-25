package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.GameRoomClosedEvent;
import org.moshe.arad.kafka.events.NewGameRoomOpenedEvent;
import org.moshe.arad.kafka.events.NewGameRoomOpenedEventAck;
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
public class GameRoomClosedEventLogoutConsumer extends SimpleEventsConsumer {

	@Autowired
	private LobbyView lobbyView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(GameRoomClosedEventLogoutConsumer.class);
	
	public GameRoomClosedEventLogoutConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		GameRoomClosedEvent gameRoomClosedEvent = convertJsonBlobIntoEvent(record.value());
		
		try{			
			logger.info("Will delete game room...");
			lobbyView.deleteGameRoom(gameRoomClosedEvent.getGameRoom());
			lobbyView.deleteOpenedByUser(gameRoomClosedEvent.getGameRoom(), gameRoomClosedEvent.getGameRoom().getOpenBy());
			logger.info("Game room deleted to view");
			logger.info("Will mark view update...");
			lobbyView.markGameRoomClosedUpdateView(gameRoomClosedEvent.getGameRoom().getName());
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private GameRoomClosedEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, GameRoomClosedEvent.class);
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