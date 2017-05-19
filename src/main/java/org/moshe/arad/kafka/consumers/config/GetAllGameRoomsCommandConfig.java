package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class GetAllGameRoomsCommandConfig extends SimpleConsumerConfig{

	public GetAllGameRoomsCommandConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.GET_ALL_GAME_ROOMS_COMMAND_GROUP);
	}
}
