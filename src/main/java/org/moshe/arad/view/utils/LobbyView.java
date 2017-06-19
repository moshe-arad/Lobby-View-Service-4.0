package org.moshe.arad.view.utils;

import java.util.List;

import org.moshe.arad.entities.GameRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LobbyView {

	@Autowired
	private LobbyViewUpdate lobbyViewUpdate;
	
	@Autowired
	private LobbyViewSimple lobbyViewSimple;
	
	public LobbyViewChanges getNeedToUpdateAllUsers(){
		return lobbyViewUpdate.getNeedToUpdateAllUsers();
	}
	
	public LobbyViewChanges getNeedToUpdateGroupUsers(String group){
		return lobbyViewUpdate.getNeedToUpdateGroupUsers(group);
	}
	
	public LobbyViewChanges getNeedToUpdateUser(String username){
		return lobbyViewUpdate.getNeedToUpdateUser(username);
	}
	
	public void markNeedToUpdateAllUsers(LobbyViewChanges lobbyViewChanges){
		lobbyViewUpdate.markNeedToUpdateAllUsers(lobbyViewChanges);
	}
	
	public void markNeedToUpdateGroupUsers(LobbyViewChanges lobbyViewChanges, String group){
		lobbyViewUpdate.markNeedToUpdateGroupUsers(lobbyViewChanges, group);
	}
	
	public void markNeedToUpdateSingleUser(LobbyViewChanges lobbyViewChanges, String username){
		lobbyViewUpdate.markNeedToUpdateSingleUser(lobbyViewChanges, username);
	}
	
	public boolean isGameRoomExist(String gameRoomName){
		return lobbyViewSimple.isGameRoomExist(gameRoomName);
	}
	
	public void removeGameRoom(String gameRoomName){
		lobbyViewSimple.removeGameRoom(gameRoomName);
	}
	
	public void addGameRoom(GameRoom gameRoom){
		lobbyViewSimple.addGameRoom(gameRoom);
	}
	
	public void addOpenedByUser(GameRoom gameRoom, String username){
		lobbyViewSimple.addOpenedByUser(gameRoom, username);
	}
	
	public void deleteGameRoom(GameRoom gameRoom){
		lobbyViewSimple.deleteGameRoom(gameRoom);
	}
	
	public void deleteOpenedByUser(String username){
		lobbyViewSimple.deleteOpenedByUser(username);
	}
	
	public void deleteSecondUser(String username){
		lobbyViewSimple.deleteSecondUser(username);
	}
	
	public void addUserAsWatcher(String username, String gameRoomName){
		lobbyViewSimple.addUserAsWatcher(username, gameRoomName);
	}
	
	public void addUserAsSecondPlayer(String username, String gameRoomName){
		lobbyViewSimple.addUserAsSecondPlayer(username, gameRoomName);
	}
	
	public void deleteWatcherFromGameRoom(String watcher){
		lobbyViewSimple.deleteWatcherFromGameRoom(watcher);
	}
	
	public List<GameRoom> getAllGameRooms(){
		return lobbyViewSimple.getAllGameRooms();
	}
	
	public GameRoom getGameRoom(String gameRoomName){
		return lobbyViewSimple.getGameRoom(gameRoomName);
	}
}
