package org.moshe.arad.kafka.consumers.config;

import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.springframework.stereotype.Component;

@Component
public class GetLobbyUpdateViewCommandConfig extends SimpleConsumerConfig{

	public GetLobbyUpdateViewCommandConfig() {
		super();
		super.getProperties().put("group.id", KafkaUtils.GET_LOBBY_UPDATE_VIEW_COMMAND_GROUP);
	}
}
