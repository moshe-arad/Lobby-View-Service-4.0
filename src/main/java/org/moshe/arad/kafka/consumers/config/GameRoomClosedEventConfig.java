package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class GameRoomClosedEventConfig extends SimpleConsumerConfig{

	public GameRoomClosedEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.GAME_ROOM_CLOSED_EVENT_GROUP);
	}
}
