package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class WatcherRemovedEventConfig extends SimpleConsumerConfig{

	public WatcherRemovedEventConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.WATCHER_REMOVED_EVENT_GROUP);
	}
}
