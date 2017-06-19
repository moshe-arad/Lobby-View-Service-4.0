package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.LoggedOutOpenByLeftBeforeGameStartedEvent;
import org.moshe.arad.kafka.events.NewGameRoomOpenedEvent;
import org.moshe.arad.view.utils.LobbyView;
import org.moshe.arad.view.utils.LobbyViewChanges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class LoggedOutOpenByLeftBeforeGameStartedEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private LobbyView lobbyView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(LoggedOutOpenByLeftBeforeGameStartedEventConsumer.class);
	
	public LoggedOutOpenByLeftBeforeGameStartedEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		LoggedOutOpenByLeftBeforeGameStartedEvent loggedOutOpenByLeftBeforeGameStartedEvent = convertJsonBlobIntoEvent(record.value());
		
		try{
			logger.info("Will add game room...");
			lobbyView.addGameRoom(loggedOutOpenByLeftBeforeGameStartedEvent.getGameRoom());
			String loggedOutUser = loggedOutOpenByLeftBeforeGameStartedEvent.getLoggedOutUserName();
			String gameRoomName = loggedOutOpenByLeftBeforeGameStartedEvent.getGameRoom().getName();
			
			GameRoom room = lobbyView.getGameRoom(gameRoomName);
			
			if(room.getOpenBy().equals(loggedOutUser)){
				lobbyView.deleteOpenedByUser(loggedOutUser);
			}
			else if(room.getSecondPlayer().equals(loggedOutUser)){
				lobbyView.deleteSecondUser(loggedOutUser);
			}
			else if(room.getWatchers().contains(loggedOutUser)){
				lobbyView.deleteWatcherFromGameRoom(loggedOutUser);
			}
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private LoggedOutOpenByLeftBeforeGameStartedEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, LoggedOutOpenByLeftBeforeGameStartedEvent.class);
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
