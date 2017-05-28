package org.moshe.arad.kafka.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.moshe.arad.entities.GameRoom;
import org.moshe.arad.services.LobbyViewChanges;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GetLobbyUpdateViewAckEvent extends BackgammonEvent{
	
	private LobbyViewChanges lobbyViewChanges;
	
	public GetLobbyUpdateViewAckEvent() {
	
	}

	public GetLobbyUpdateViewAckEvent(LobbyViewChanges lobbyViewChanges) {
		super();
		this.lobbyViewChanges = lobbyViewChanges;
	}

	@Override
	public String toString() {
		return "GetLobbyUpdateViewAckEvent [lobbyViewChanges=" + lobbyViewChanges + "]";
	}

	public LobbyViewChanges getLobbyViewChanges() {
		return lobbyViewChanges;
	}

	public void setLobbyViewChanges(LobbyViewChanges lobbyViewChanges) {
		this.lobbyViewChanges = lobbyViewChanges;
	}
}
