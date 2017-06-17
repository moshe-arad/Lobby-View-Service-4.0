package org.moshe.arad.kafka.consumers.events;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.events.LoggedOutOpenByLeftLastEvent;
import org.moshe.arad.kafka.events.LoggedOutSecondLeftLastEvent;
import org.moshe.arad.kafka.events.LoggedOutWatcherLeftLastEvent;
import org.moshe.arad.kafka.events.SecondLeftLastEvent;
import org.moshe.arad.view.utils.LobbyView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope("prototype")
public class SecondLeftLastEventConsumer extends SimpleEventsConsumer {
	
	@Autowired
	private LobbyView lobbyView;
	
	@Autowired
	private ApplicationContext context;
	
	private ConsumerToProducerQueue consumerToProducerQueue;
	
	Logger logger = LoggerFactory.getLogger(SecondLeftLastEventConsumer.class);
	
	public SecondLeftLastEventConsumer() {
	}
	
	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		SecondLeftLastEvent secondLeftLastEvent = convertJsonBlobIntoEvent(record.value());
		
		try{
			lobbyView.addGameRoom(secondLeftLastEvent.getGameRoom());
			String loggedOutUser = secondLeftLastEvent.getSecond();
			
			lobbyView.deleteSecondUser(loggedOutUser);
		}
		catch(Exception e){
			logger.error("Failed to add new game room to view...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private SecondLeftLastEvent convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, SecondLeftLastEvent.class);
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
