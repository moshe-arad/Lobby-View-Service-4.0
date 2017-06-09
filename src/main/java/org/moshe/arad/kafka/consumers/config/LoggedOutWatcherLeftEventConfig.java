package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class LoggedOutWatcherLeftEventConfig extends SimpleConsumerConfig{

	public LoggedOutWatcherLeftEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.LOGGED_OUT_WATCHER_LEFT_EVENT_GROUP);
	}
}
