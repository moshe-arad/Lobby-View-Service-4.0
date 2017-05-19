package org.moshe.arad.kafka.consumers.command;

import java.io.IOException;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.commands.GetAllGameRoomsCommand;
import org.moshe.arad.kafka.events.GetAllGameRoomsAckEvent;
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
		logger.info("Get All Game Rooms Command record recieved, " + record.value());
    	
		List<GameRoom> rooms = lobbyView.getAllGameRooms();
		GetAllGameRoomsAckEvent getAllGameRoomsAckEvent = context.getBean(GetAllGameRoomsAckEvent.class);
		getAllGameRoomsAckEvent.setGameRooms(rooms);
		getAllGameRoomsAckEvent.setUuid(getAllGameRoomsCommand.getUuid());
		
    	logger.info("passing Get All Game Rooms Ack Event to producer...");
    	consumerToProducerQueue.getEventsQueue().put(getAllGameRoomsAckEvent);
    	logger.info("Event passed to producer...");		
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




	