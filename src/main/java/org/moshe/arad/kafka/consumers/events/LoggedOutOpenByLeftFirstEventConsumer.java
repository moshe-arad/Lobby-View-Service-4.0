package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.LoggedOutOpenByLeftBeforeGameStartedEvent;
import org.moshe.arad.kafka.events.LoggedOutOpenByLeftEvent;
import org.moshe.arad.kafka.events.LoggedOutOpenByLeftFirstEvent;
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
public class LoggedOutOpenByLeftFirstEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private LobbyView lobbyView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(LoggedOutOpenByLeftFirstEventConsumer.class);
	
	public LoggedOutOpenByLeftFirstEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		LoggedOutOpenByLeftFirstEvent loggedOutOpenByLeftFirstEvent = convertJsonBlobIntoEvent(record.value());
		LobbyViewChanges lobbyViewChanges = context.getBean(LobbyViewChanges.class);
		
		try{
			lobbyView.addGameRoom(loggedOutOpenByLeftFirstEvent.getGameRoom());
			lobbyView.deleteOpenedByUser(loggedOutOpenByLeftFirstEvent.getOpenBy());
			
			lobbyViewChanges.getGameRoomsUpdate().add(loggedOutOpenByLeftFirstEvent.getGameRoom());
			lobbyView.markNeedToUpdateGroupUsers(lobbyViewChanges, "lobby");
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private LoggedOutOpenByLeftFirstEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, LoggedOutOpenByLeftFirstEvent.class);
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
