package org.moshe.arad.kafka.consumers.command;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.commands.GetLobbyUpdateViewCommand;
import org.moshe.arad.kafka.events.GetLobbyUpdateViewAckEvent;
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
public class GetLobbyUpdateViewCommandConsumer extends SimpleCommandsConsumer {

	private ConsumerToProducerQueue consumerToProducerQueue;
	private Logger logger = LoggerFactory.getLogger(GetLobbyUpdateViewCommandConsumer.class);
	
//	@Autowired
//	private LobbyViewOld lobbyView;
	
	@Autowired
	private UsersView lobbyView;
	
	@Autowired
	private ApplicationContext context;
	
	public GetLobbyUpdateViewCommandConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		GetLobbyUpdateViewAckEvent getLobbyUpdateViewAckEvent = context.getBean(GetLobbyUpdateViewAckEvent.class);
		GetLobbyUpdateViewCommand getLobbyUpdateViewCommand = convertJsonBlobIntoEvent(record.value());
		LobbyViewChanges lobbyViewChanges = null;
		
		logger.info("Get Lobby Update View Command record recieved, " + record.value());
		
		
		if(getLobbyUpdateViewCommand.isAllLevel()){
			lobbyViewChanges = lobbyView.getNeedToUpdateAllUsers();
		}
		else if(getLobbyUpdateViewCommand.isGroupLevel()){
			lobbyViewChanges = lobbyView.getNeedToUpdateGroupUsers(getLobbyUpdateViewCommand.getGroup());
		}
		else if(getLobbyUpdateViewCommand.isUserLevel()){
			lobbyViewChanges = lobbyView.getNeedToUpdateUser(getLobbyUpdateViewCommand.getUser());
		}
		
		getLobbyUpdateViewAckEvent.setUuid(getLobbyUpdateViewCommand.getUuid());
		getLobbyUpdateViewAckEvent.setLobbyViewChanges(lobbyViewChanges);
		
    	logger.info("passing get Lobby Update View Ack Event to producer...");
    	consumerToProducerQueue.getEventsQueue().put(getLobbyUpdateViewAckEvent);
    	logger.info("Event passed to producer...");		
	}

	public ConsumerToProducerQueue getConsumerToProducerQueue() {
		return consumerToProducerQueue;
	}

	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
		this.consumerToProducerQueue = consumerToProducerQueue;
	}
	
	private GetLobbyUpdateViewCommand convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, GetLobbyUpdateViewCommand.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}




	