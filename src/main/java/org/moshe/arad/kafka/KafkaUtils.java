package org.moshe.arad.kafka;

public class KafkaUtils {

	public static final String SERVERS = "localhost:9092,localhost:9093,localhost:9094";
	public static final String CREATE_NEW_USER_COMMAND_GROUP = "CreateNewUserCommandGroup";
	public static final String STRING_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
	public static final String STRING_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
	public static final String CREATE_NEW_USER_COMMAND_DESERIALIZER = "org.moshe.arad.kafka.deserializers.CreateNewUserCommandDeserializer";
	public static final String NEW_USER_CREATED_EVENT_SERIALIZER = "org.moshe.arad.kafka.serializers.NewUserCreatedEventSerializer";
	public static final String COMMANDS_TO_USERS_SERVICE_TOPIC = "Commands-To-Users-Service";
	public static final String NEW_USER_CREATED_EVENT_DESERIALIZER = "org.moshe.arad.kafka.deserializers.NewUserCreatedEventDeserializer";
	public static final String NEW_USER_CREATED_EVENT_TOPIC = "New-User-Created-Event";
	public static final String NEW_USER_JOINED_LOBBY_EVENT_TOPIC = "New-User-Joined-Lobby-Event";
	public static final String NEW_USER_JOINED_LOBBY_EVENT_SERIALIZER = "org.moshe.arad.kafka.serializers.NewUserCreatedEventSerializer";
	public static final String NEW_USER_CREATED_EVENT_GROUP = "NewUserCreatedEventGroup3";
	public static final String CHECK_USER_NAME_AVAILABILITY_COMMAND_DESERIALIZER = "org.moshe.arad.kafka.deserializers.CheckUserNameAvailabilityCommandDeserializer";
	public static final String CHECK_USER_EMAIL_AVAILABILITY_COMMAND_TOPIC = "Check-User-Email-Availability-Command";
	public static final String CHECK_USER_NAME_AVAILABILITY_COMMAND_TOPIC = "Check-User-Name-Availability-Command";
	public static final String USER_NAME_AVAILABILITY_CHECKED_EVENT_TOPIC = "User-Name-Availability-Checked-Event";
	public static final String CHECK_USER_NAME_AVAILABILITY_GROUP = "CheckUserNameAvailabilityGroup";
	public static final String USER_NAME_AVAILABILITY_CHECKED_EVENT_SERIALIZER = "org.moshe.arad.kafka.serializers.UserNameAvailabilityCheckedEventSerializer";
	public static final String CHECK_USER_EMAIL_AVAILABILITY_GROUP = "CheckUserEmailAvailabilityGroup";
	public static final String CHECK_USER_EMAIL_AVAILABILITY_COMMAND_DESERIALIZER = "org.moshe.arad.kafka.deserializers.CheckUserEmailAvailabilityCommandDeserializer";
	public static final String USER_EMAIL_AVAILABILITY_CHECKED_EVENT_SERIALIZER = "org.moshe.arad.kafka.serializers.UserEmailAvailabilityCheckedEventSerializer";
	public static final String EMAIL_AVAILABILITY_CHECKED_EVENT_TOPIC = "Email-Availability-Checked-Event";
	public static final String NEW_USER_JOINED_LOBBY_EVENT_GROUP2 = "NewUserJoinedLobbyEventGroup2";
	public static final String LOG_IN_USER_COMMAND_CONFIG_GROUP = "LogInUserCommnadConfigGroup";
	public static final String LOG_IN_USER_ACK_EVENT_TOPIC = "Log-In-User-Ack-Event";
	public static final String LOG_IN_USER_COMMAND_TOPIC = "Log-In-User-Command";
	public static final String EXISTING_USER_JOINED_LOBBY_EVENT_GROUP = "ExistingUserJoinedLobbyEventGroup2";
	public static final String EXISTING_USER_JOINED_LOBBY_EVENT_TOPIC = "Existing-User-Joined-Lobby-Event";
	public static final String LOG_OUT_USER_COMMAND_TOPIC = "Log-Out-User-Command";
	public static final String LOG_OUT_USER_COMMAND_CONFIG_GROUP = "LogOutUserCommandGroup";
	public static final String LOG_OUT_USER_ACK_EVENT_TOPIC = "Log-Out-User-Ack-Event";
	public static final String NEW_USER_CREATED_EVENT_ACK_TOPIC = "New-User-Created-Event-Ack";
	public static final String NEW_GAME_ROOM_OPENED_EVENT_GROUP = "NewGameRoomOpenedEventGroup";
	public static final String NEW_GAME_ROOM_OPENED_EVENT_ACK_TOPIC = "New-Game-Room-Opened-Event-Ack";
	public static final String NEW_GAME_ROOM_OPENED_EVENT_TOPIC = "New-Game-Room-Opened-Event";
	public static final String GAME_ROOM_CLOSED_EVENT_GROUP = "GameRoomClosedEventGroup1";
	public static final String GAME_ROOM_CLOSED_EVENT_TOPIC = "Game-Room-Closed-Event";
	public static final Object USER_ADDED_AS_WATCHER_EVENT_GROUP = "UserAddedAsWatcherEventGroup1";
	public static final String USER_ADDED_AS_WATCHER_EVENT_TOPIC = "User-Added-As-Watcher-Event";
	public static final String GET_ALL_GAME_ROOMS_COMMAND_GROUP = "GetAllGamesRoomsCommandGroup";
	public static final String GET_ALL_GAME_ROOMS_COMMAND_TOPIC = "Get-All-Game-Rooms-Command";
	public static final String GET_ALL_GAME_ROOMS_EVENT_ACK_TOPIC = "Get-All-Game-Rooms-Event-Ack";
	public static final String GAME_ROOM_CLOSED_EVENT_LOGOUT_GROUP = "GameRoomClosedEventLogoutGroup2";
	public static final String GAME_ROOM_CLOSED_EVENT_LOGOUT_TOPIC = "Game-Room-Closed-Event-Logout";
	public static final String WATCHER_REMOVED_EVENT_GROUP = "WatcherRemovedEventGroup2";
	public static final String WATCHER_REMOVED_EVENT_TOPIC = "Watcher-Removed-Event";
	public static final String GET_LOBBY_UPDATE_VIEW_COMMAND_GROUP = "GetLobbyUpdateViewCommandGroup";
	public static final String GET_LOBBY_UPDATE_VIEW_COMMAND_TOPIC = "Get-Lobby-Update-View-Command";
	public static final String GET_LOBBY_UPDATE_VIEW_ACK_EVENT_TOPIC = "Get-Lobby-Update-View-Ack-Event";
	public static final String USER_ADDED_AS_SECOND_PLAYER_EVENT_GROUP = "UserAddedAsSecondPlayereventGroup2";
	public static final String USER_ADDED_AS_SECOND_PLAYER_EVENT_TOPIC = "User-Added-As-Second-Player-Event";
	public static final String INIT_GAME_ROOM_COMPLETED_EVENT_TOPIC = "Init-Game-Room-Completed-Event";
	public static final String LOGGED_OUT_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_GROUP = "LoggedOutOpenbyLeftBeforeGameStartedEventGroup4";
	public static final String LOGGED_OUT_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC = "Logged-Out-Openby-Left-Before-Game-Started-Event";
	public static final String GAME_ROOM_CLOSED_LOGGED_OUT_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_GROUP = "GameRoomClosedLoggedOutOpenbyLeftBeforeGameStartedEventGroup2";
	public static final String GAME_ROOM_CLOSED_LOGGED_OUT_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC = "Game-Room-Closed-Logged-Out-Openby-Left-Before-Game-Started-Event";
	public static final String LOGGED_OUT_OPENBY_LEFT_EVENT_GROUP = "LoggedOutOpenbyLeftEventGroup3";
	public static final String LOGGED_OUT_OPENBY_LEFT_EVENT_TOPIC = "Logged-Out-Openby-Left-Event";
	public static final String LOGGED_OUT_WATCHER_LEFT_LAST_EVENT_GROUP = "LoggedOutWatcherLeftLastEventGroup4";
	public static final String GAME_ROOM_CLOSED_LOGGED_OUT_WATCHER_LEFT_LAST_EVENT_GROUP = "GameRoomClosedLoggedOutWatcherLeftLastEventGroup2";
	public static final String LOGGED_OUT_WATCHER_LEFT_LAST_EVENT_TOPIC = "Logged-Out-Watcher-Left-Last-Event";
	public static final String GAME_ROOM_CLOSED_LOGGED_OUT_WATCHER_LEFT_LAST_EVENT_TOPIC = "Game-Room-Closed-Logged-Out-Watcher-Left-Last-Event";
	public static final String LOGGED_OUT_WATCHER_LEFT_EVENT_GROUP = "LoggedOutWatcherLeftEventGroup3";
	public static final String LOGGED_OUT_WATCHER_LEFT_EVENT_TOPIC = "Logged-Out-Watcher-Left-Event";
	public static final String LOGGED_OUT_OPENBY_LEFT_FIRST_EVENT_GROUP = "LoggedOutOpenbyLeftFirstEventGroup3";
	public static final String LOGGED_OUT_OPENBY_LEFT_FIRST_EVENT_TOPIC = "Logged-Out-Openby-Left-First-Event";
	public static final String LOGGED_OUT_SECOND_LEFT_FIRST_EVENT_GROUP = "LoggedOutSecondLeftFirstEventGroup3";
	public static final String LOGGED_OUT_SECOND_LEFT_FIRST_EVENT_TOPIC = "Logged-Out-Second-Left-First-Event";
	public static final String LOGGED_OUT_SECOND_LEFT_EVENT_GROUP = "LoggedOutSecondLeftEventGroup3";
	public static final String LOGGED_OUT_SECOND_LEFT_EVENT_TOPIC = "Logged-Out-Second-Left-Event";
	public static final String LOGGED_OUT_OPENBY_LEFT_LAST_EVENT_GROUP = "LoggedOutOpenbyLeftLastEventGroup4";
	public static final String LOGGED_OUT_OPENBY_LEFT_LAST_EVENT_TOPIC = "Logged-Out-Openby-Left-Last-Event";
	public static final String GAME_ROOM_CLOSED_LOGGED_OUT_OPENBY_LEFT_LAST_EVENT_GROUP = "GameRoomClosedLoggedOutOpenbyLeftLastEventGroup2";
	public static final String GAME_ROOM_CLOSED_LOGGED_OUT_OPENBY_LEFT_LAST_EVENT_TOPIC = "Game-Room-Closed-Logged-Out-Openby-Left-Last-Event";
	public static final String GAME_ROOM_CLOSED_LOGGED_OUT_SECOND_LEFT_LAST_EVENT_GROUP = "GameRoomClosedLoggedOutSecondLeftLastEventGroup2";
	public static final String LOGGED_OUT_SECOND_LEFT_LAST_EVENT_GROUP = "LoggedOutSecondLeftLastEventGroup4";
	public static final String LOGGED_OUT_SECOND_LEFT_LAST_EVENT_TOPIC = "Logged-Out-Second-Left-Last-Event";
	public static final String GAME_ROOM_CLOSED_LOGGED_OUT_SECOND_LEFT_LAST_EVENT_TOPIC = "Game-Room-Closed-Logged-Out-Second-Left-Last-Event";
	public static final String OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_GROUP = "OpenbyLeftBeforeGameStartedEventGroup3";
	public static final String OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC = "Openby-Left-Before-Game-Started-Event";
	public static final String GAME_ROOM_CLOSED_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_GROUP = "GameRoomClosedOpenbyLeftBeforeGameStartedEventGroup2";
	public static final String GAME_ROOM_CLOSED_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC = "Game-Room-Closed-Openby-Left-Before-Game-Started-Event";
	public static final String OPENBY_LEFT_EVENT_GROUP = "OpenbyLeftEventGroup3";
	public static final String OPENBY_LEFT_EVENT_TOPIC = "Openby-Left-Event";
}