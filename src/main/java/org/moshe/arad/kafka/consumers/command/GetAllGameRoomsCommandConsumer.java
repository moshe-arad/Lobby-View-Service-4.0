package org.moshe.arad.kafka.consumers.command;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.commands.GetAllGameRoomsCommand;
import org.moshe.arad.kafka.commands.GetLobbyUpdateViewCommand;
import org.moshe.arad.kafka.events.GetLobbyUpdateViewAckEvent;
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
public class GetAllGameRoomsCommandConsumer extends SimpleCommandsConsumer {

	private ConsumerToProducerQueue consumerToProducerQueue;
	private Logger logger = LoggerFactory.getLogger(GetAllGameRoomsCommandConsumer.class);
	
	@Autowired
	private LobbyView lobbyView;
	
	@Autowired
	private ApplicationContext context;
	
	public GetAllGameRoomsCommandConsumer() {
	}

	@Override
	public void consumerOperations(ConsumerRecord<String, String> record) {
		GetAllGameRoomsCommand getAllGameRoomsCommand = convertJsonBlobIntoEvent(record.value());
		LobbyViewChanges lobbyViewChanges = context.getBean(LobbyViewChanges.class);
		
		List<GameRoom> rooms = lobbyView.getAllGameRooms();
		lobbyViewChanges.setGameRoomsAdd(rooms);
		
		lobbyView.markNeedToUpdateSingleUser(lobbyViewChanges, getAllGameRoomsCommand.getUsername());
	}

	public ConsumerToProducerQueue getConsumerToProducerQueue() {
		return consumerToProducerQueue;
	}

	public void setConsumerToProducerQueue(ConsumerToProducerQueue consumerToProducerQueue) {
		this.consumerToProducerQueue = consumerToProducerQueue;
	}
	
	private GetAllGameRoomsCommand convertJsonBlobIntoEvent(String JsonBlob){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(JsonBlob, GetAllGameRoomsCommand.class);
		} catch (IOException e) {
			logger.error("Falied to convert Json blob into Event...");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}




	