package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class GameRoomClosedWatcherLeftLastEventConfig extends SimpleConsumerConfig{

	public GameRoomClosedWatcherLeftLastEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.GAME_ROOM_CLOSED_WATCHER_LEFT_LAST_EVENT_GROUP);
	}
}
