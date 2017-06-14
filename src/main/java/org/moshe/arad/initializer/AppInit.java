package org.moshe.arad.initializer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.moshe.arad.kafka.ConsumerToProducerQueue;
import org.moshe.arad.kafka.KafkaUtils;
import org.moshe.arad.kafka.consumers.ISimpleConsumer;
import org.moshe.arad.kafka.consumers.command.GetAllGameRoomsCommandConsumer;
import org.moshe.arad.kafka.consumers.command.GetLobbyUpdateViewCommandConsumer;
import org.moshe.arad.kafka.consumers.config.GameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConfig;
import org.moshe.arad.kafka.consumers.config.GameRoomClosedLoggedOutOpenByLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.config.GameRoomClosedLoggedOutSecondLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.config.GameRoomClosedLoggedOutWatcherLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.config.GameRoomClosedOpenByLeftBeforeGameStartedEventConfig;
import org.moshe.arad.kafka.consumers.config.GameRoomClosedWatcherLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.config.GetAllGameRoomsCommandConfig;
import org.moshe.arad.kafka.consumers.config.GetLobbyUpdateViewCommandConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutOpenByLeftBeforeGameStartedEventConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutOpenByLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutOpenByLeftFirstEventConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutOpenByLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutSecondLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutSecondLeftFirstEventConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutSecondLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutWatcherLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutWatcherLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.config.NewGameRoomOpenedEventConfig;
import org.moshe.arad.kafka.consumers.config.OpenByLeftBeforeGameStartedEventConfig;
import org.moshe.arad.kafka.consumers.config.OpenByLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.config.UserAddedAsSecondPlayerEventConfig;
import org.moshe.arad.kafka.consumers.config.UserAddedAsWatcherEventConfig;
import org.moshe.arad.kafka.consumers.config.WatcherLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.WatcherLeftLastEventConfig;
import org.moshe.arad.kafka.consumers.events.GameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConsumer;
import org.moshe.arad.kafka.consumers.events.GameRoomClosedLoggedOutOpenByLeftLastEventConsumer;
import org.moshe.arad.kafka.consumers.events.GameRoomClosedLoggedOutSecondLeftLastEventConsumer;
import org.moshe.arad.kafka.consumers.events.GameRoomClosedLoggedOutWatcherLeftLastEventConsumer;
import org.moshe.arad.kafka.consumers.events.GameRoomClosedOpenByLeftBeforeGameStartedEventConsumer;
import org.moshe.arad.kafka.consumers.events.GameRoomClosedWatcherLeftLastEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutOpenByLeftBeforeGameStartedEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutOpenByLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutOpenByLeftFirstEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutOpenByLeftLastEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutSecondLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutSecondLeftFirstEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutSecondLeftLastEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutWatcherLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutWatcherLeftLastEventConsumer;
import org.moshe.arad.kafka.consumers.events.NewGameRoomOpenedEventConsumer;
import org.moshe.arad.kafka.consumers.events.OpenByLeftBeforeGameStartedEventConsumer;
import org.moshe.arad.kafka.consumers.events.OpenByLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserAddedAsSecondPlayerEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserAddedAsWatcherEventConsumer;
import org.moshe.arad.kafka.consumers.events.WatcherLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.WatcherLeftLastEventConsumer;
import org.moshe.arad.kafka.events.GetAllGameRoomsAckEvent;
import org.moshe.arad.kafka.events.GetLobbyUpdateViewAckEvent;
import org.moshe.arad.kafka.events.InitGameRoomCompletedEvent;
import org.moshe.arad.kafka.producers.ISimpleProducer;
import org.moshe.arad.kafka.producers.events.SimpleEventsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppInit implements ApplicationContextAware, IAppInitializer {	
	
	private ExecutorService executor = Executors.newFixedThreadPool(6);
	
	private Logger logger = LoggerFactory.getLogger(AppInit.class);
	
	private ApplicationContext context;
	
	private NewGameRoomOpenedEventConsumer newGameRoomOpenedEventConsumer;
	
	@Autowired
	private NewGameRoomOpenedEventConfig newGameRoomOpenedEventConfig;
	
	private UserAddedAsWatcherEventConsumer userAddedAsWatcherEventConsumer;
	
	@Autowired
	private UserAddedAsWatcherEventConfig userAddedAsWatcherEventConfig;
	
	private GetAllGameRoomsCommandConsumer getAllGameRoomsCommandConsumer;
	
	@Autowired
	private GetAllGameRoomsCommandConfig getAllGameRoomsCommandConfig;
	
	@Autowired
	private SimpleEventsProducer<GetAllGameRoomsAckEvent> getAllGameRoomsAckEventProducer;
	
	private ConsumerToProducerQueue getAllGameRoomsQueue;
	
	private GetLobbyUpdateViewCommandConsumer getLobbyUpdateViewCommandConsumer;
	
	@Autowired
	private GetLobbyUpdateViewCommandConfig getLobbyUpdateViewCommandConfig;
	
	private ConsumerToProducerQueue getLobbyUpdateViewQueue;
	
	@Autowired
	private SimpleEventsProducer<GetLobbyUpdateViewAckEvent> getLobbyUpdateViewAckEventProducer;
	
	private UserAddedAsSecondPlayerEventConsumer userAddedAsSecondPlayerEventConsumer;
	
	@Autowired
	private UserAddedAsSecondPlayerEventConfig userAddedAsSecondPlayerEventConfig;
	
	private LoggedOutOpenByLeftBeforeGameStartedEventConsumer loggedOutOpenByLeftBeforeGameStartedEventConsumer;
	
	@Autowired
	private LoggedOutOpenByLeftBeforeGameStartedEventConfig loggedOutOpenByLeftBeforeGameStartedEventConfig;
	
	private ConsumerToProducerQueue initGameRoomCompletedQueue;
	
	@Autowired
	private SimpleEventsProducer<InitGameRoomCompletedEvent> initGameRoomCompletedEventProducer;
	
	private GameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConsumer gameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConsumer;
	
	@Autowired
	private GameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConfig gameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConfig;
	
	private LoggedOutOpenByLeftEventConsumer loggedOutOpenByLeftEventConsumer;
	
	@Autowired
	private LoggedOutOpenByLeftEventConfig loggedOutOpenByLeftEventConfig;
	
	private LoggedOutWatcherLeftLastEventConsumer loggedOutWatcherLeftLastEventConsumer;
	
	@Autowired
	private LoggedOutWatcherLeftLastEventConfig loggedOutWatcherLeftLastEventConfig;
	
	private GameRoomClosedLoggedOutWatcherLeftLastEventConsumer gameRoomClosedLoggedOutWatcherLeftLastEventConsumer;
	
	@Autowired
	private GameRoomClosedLoggedOutWatcherLeftLastEventConfig gameRoomClosedLoggedOutWatcherLeftLastEventConfig;
	
	private LoggedOutWatcherLeftEventConsumer loggedOutWatcherLeftEventConsumer;
	
	@Autowired
	private LoggedOutWatcherLeftEventConfig loggedOutWatcherLeftEventConfig;
	
	private LoggedOutOpenByLeftFirstEventConsumer loggedOutOpenByLeftFirstEventConsumer;
	
	@Autowired
	private LoggedOutOpenByLeftFirstEventConfig loggedOutOpenByLeftFirstEventConfig;
	
	private LoggedOutSecondLeftFirstEventConsumer loggedOutSecondLeftFirstEventConsumer;
	
	@Autowired
	private LoggedOutSecondLeftFirstEventConfig loggedOutSecondLeftFirstEventConfig;
	
	private LoggedOutSecondLeftEventConsumer loggedOutSecondLeftEventConsumer;
	
	@Autowired
	private LoggedOutSecondLeftEventConfig loggedOutSecondLeftEventConfig;
	
	private LoggedOutOpenByLeftLastEventConsumer loggedOutOpenByLeftLastEventConsumer;
	
	@Autowired
	private LoggedOutOpenByLeftLastEventConfig loggedOutOpenByLeftLastEventConfig;
	
	private GameRoomClosedLoggedOutOpenByLeftLastEventConsumer gameRoomClosedLoggedOutOpenByLeftLastEventConsumer;
	
	@Autowired
	private GameRoomClosedLoggedOutOpenByLeftLastEventConfig gameRoomClosedLoggedOutOpenByLeftLastEventConfig;
	
	private LoggedOutSecondLeftLastEventConsumer loggedOutSecondLeftLastEventConsumer;
	
	@Autowired
	private LoggedOutSecondLeftLastEventConfig loggedOutSecondLeftLastEventConfig;
	
	private GameRoomClosedLoggedOutSecondLeftLastEventConsumer gameRoomClosedLoggedOutSecondLeftLastEventConsumer;
	
	@Autowired
	private GameRoomClosedLoggedOutSecondLeftLastEventConfig gameRoomClosedLoggedOutSecondLeftLastEventConfig;
	
	private OpenByLeftBeforeGameStartedEventConsumer openByLeftBeforeGameStartedEventConsumer;
	
	@Autowired
	private OpenByLeftBeforeGameStartedEventConfig openByLeftBeforeGameStartedEventConfig;
	
	private GameRoomClosedOpenByLeftBeforeGameStartedEventConsumer gameRoomClosedOpenByLeftBeforeGameStartedEventConsumer;
	
	@Autowired
	private GameRoomClosedOpenByLeftBeforeGameStartedEventConfig gameRoomClosedOpenByLeftBeforeGameStartedEventConfig;

	private OpenByLeftEventConsumer openByLeftEventConsumer;
	
	@Autowired
	private OpenByLeftEventConfig openByLeftEventConfig;
	
	private WatcherLeftLastEventConsumer watcherLeftLastEventConsumer;
	
	@Autowired
	private WatcherLeftLastEventConfig watcherLeftLastEventConfig;
	
	private GameRoomClosedWatcherLeftLastEventConsumer gameRoomClosedWatcherLeftLastEventConsumer;
	
	@Autowired
	private GameRoomClosedWatcherLeftLastEventConfig gameRoomClosedWatcherLeftLastEventConfig;
	
	private WatcherLeftEventConsumer watcherLeftEventConsumer;
	
	@Autowired
	private WatcherLeftEventConfig watcherLeftEventConfig;
	
	public static final int NUM_CONSUMERS = 3;
	
	@Override
	public void initKafkaCommandsConsumers() {		
		getAllGameRoomsQueue = context.getBean(ConsumerToProducerQueue.class);
		getLobbyUpdateViewQueue = context.getBean(ConsumerToProducerQueue.class);
		
		for(int i=0; i<NUM_CONSUMERS; i++){			
			getLobbyUpdateViewCommandConsumer = context.getBean(GetLobbyUpdateViewCommandConsumer.class);
			getAllGameRoomsCommandConsumer = context.getBean(GetAllGameRoomsCommandConsumer.class);
			
			initSingleConsumer(getLobbyUpdateViewCommandConsumer, KafkaUtils.GET_LOBBY_UPDATE_VIEW_COMMAND_TOPIC, getLobbyUpdateViewCommandConfig, getLobbyUpdateViewQueue);
			
			initSingleConsumer(getAllGameRoomsCommandConsumer, KafkaUtils.GET_ALL_GAME_ROOMS_COMMAND_TOPIC, getAllGameRoomsCommandConfig, null);
			
			executeProducersAndConsumers(Arrays.asList(
					getLobbyUpdateViewCommandConsumer,
					getAllGameRoomsCommandConsumer));
		}
	}

	@Override
	public void initKafkaEventsConsumers() {
		
		initGameRoomCompletedQueue = context.getBean(ConsumerToProducerQueue.class);
		
		for(int i=0; i<NUM_CONSUMERS; i++){
			newGameRoomOpenedEventConsumer = context.getBean(NewGameRoomOpenedEventConsumer.class);			
			userAddedAsWatcherEventConsumer = context.getBean(UserAddedAsWatcherEventConsumer.class);
			userAddedAsSecondPlayerEventConsumer = context.getBean(UserAddedAsSecondPlayerEventConsumer.class);
			loggedOutOpenByLeftBeforeGameStartedEventConsumer = context.getBean(LoggedOutOpenByLeftBeforeGameStartedEventConsumer.class);
			gameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConsumer = context.getBean(GameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConsumer.class);
			loggedOutOpenByLeftEventConsumer = context.getBean(LoggedOutOpenByLeftEventConsumer.class);
			loggedOutWatcherLeftLastEventConsumer = context.getBean(LoggedOutWatcherLeftLastEventConsumer.class);
			gameRoomClosedLoggedOutWatcherLeftLastEventConsumer = context.getBean(GameRoomClosedLoggedOutWatcherLeftLastEventConsumer.class);
			loggedOutWatcherLeftEventConsumer = context.getBean(LoggedOutWatcherLeftEventConsumer.class);
			loggedOutOpenByLeftFirstEventConsumer = context.getBean(LoggedOutOpenByLeftFirstEventConsumer.class);
			loggedOutSecondLeftFirstEventConsumer = context.getBean(LoggedOutSecondLeftFirstEventConsumer.class);
			loggedOutSecondLeftEventConsumer = context.getBean(LoggedOutSecondLeftEventConsumer.class);
			loggedOutOpenByLeftLastEventConsumer = context.getBean(LoggedOutOpenByLeftLastEventConsumer.class);
			gameRoomClosedLoggedOutOpenByLeftLastEventConsumer = context.getBean(GameRoomClosedLoggedOutOpenByLeftLastEventConsumer.class);
			loggedOutSecondLeftLastEventConsumer = context.getBean(LoggedOutSecondLeftLastEventConsumer.class);
			gameRoomClosedLoggedOutSecondLeftLastEventConsumer = context.getBean(GameRoomClosedLoggedOutSecondLeftLastEventConsumer.class);
			openByLeftBeforeGameStartedEventConsumer = context.getBean(OpenByLeftBeforeGameStartedEventConsumer.class);
			gameRoomClosedOpenByLeftBeforeGameStartedEventConsumer = context.getBean(GameRoomClosedOpenByLeftBeforeGameStartedEventConsumer.class);
			openByLeftEventConsumer = context.getBean(OpenByLeftEventConsumer.class);
			watcherLeftLastEventConsumer = context.getBean(WatcherLeftLastEventConsumer.class);
			gameRoomClosedWatcherLeftLastEventConsumer = context.getBean(GameRoomClosedWatcherLeftLastEventConsumer.class);
			watcherLeftEventConsumer = context.getBean(WatcherLeftEventConsumer.class);
			
			initSingleConsumer(newGameRoomOpenedEventConsumer, KafkaUtils.NEW_GAME_ROOM_OPENED_EVENT_TOPIC, newGameRoomOpenedEventConfig, null);
			
			initSingleConsumer(userAddedAsWatcherEventConsumer, KafkaUtils.USER_ADDED_AS_WATCHER_EVENT_TOPIC, userAddedAsWatcherEventConfig, null);
			
			initSingleConsumer(userAddedAsSecondPlayerEventConsumer, KafkaUtils.USER_ADDED_AS_SECOND_PLAYER_EVENT_TOPIC, userAddedAsSecondPlayerEventConfig, initGameRoomCompletedQueue);
			
			initSingleConsumer(loggedOutOpenByLeftBeforeGameStartedEventConsumer, KafkaUtils.LOGGED_OUT_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC, loggedOutOpenByLeftBeforeGameStartedEventConfig, null);
			
			initSingleConsumer(gameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConsumer, KafkaUtils.GAME_ROOM_CLOSED_LOGGED_OUT_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC, gameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConfig, null);
			
			initSingleConsumer(loggedOutOpenByLeftEventConsumer, KafkaUtils.LOGGED_OUT_OPENBY_LEFT_EVENT_TOPIC, loggedOutOpenByLeftEventConfig, null);
			
			initSingleConsumer(loggedOutWatcherLeftLastEventConsumer, KafkaUtils.LOGGED_OUT_WATCHER_LEFT_LAST_EVENT_TOPIC, loggedOutWatcherLeftLastEventConfig, null);
			
			initSingleConsumer(gameRoomClosedLoggedOutWatcherLeftLastEventConsumer, KafkaUtils.GAME_ROOM_CLOSED_LOGGED_OUT_WATCHER_LEFT_LAST_EVENT_TOPIC, gameRoomClosedLoggedOutWatcherLeftLastEventConfig, null);
			
			initSingleConsumer(loggedOutWatcherLeftEventConsumer, KafkaUtils.LOGGED_OUT_WATCHER_LEFT_EVENT_TOPIC, loggedOutWatcherLeftEventConfig, null);
			
			initSingleConsumer(loggedOutOpenByLeftFirstEventConsumer, KafkaUtils.LOGGED_OUT_OPENBY_LEFT_FIRST_EVENT_TOPIC, loggedOutOpenByLeftFirstEventConfig, null);
			
			initSingleConsumer(loggedOutSecondLeftFirstEventConsumer, KafkaUtils.LOGGED_OUT_SECOND_LEFT_FIRST_EVENT_TOPIC, loggedOutSecondLeftFirstEventConfig, null);
			
			initSingleConsumer(loggedOutSecondLeftEventConsumer, KafkaUtils.LOGGED_OUT_SECOND_LEFT_EVENT_TOPIC, loggedOutSecondLeftEventConfig, null);
			
			initSingleConsumer(loggedOutOpenByLeftLastEventConsumer, KafkaUtils.LOGGED_OUT_OPENBY_LEFT_LAST_EVENT_TOPIC, loggedOutOpenByLeftLastEventConfig, null);
			
			initSingleConsumer(gameRoomClosedLoggedOutOpenByLeftLastEventConsumer, KafkaUtils.GAME_ROOM_CLOSED_LOGGED_OUT_OPENBY_LEFT_LAST_EVENT_TOPIC, gameRoomClosedLoggedOutOpenByLeftLastEventConfig, null);
			
			initSingleConsumer(loggedOutSecondLeftLastEventConsumer, KafkaUtils.LOGGED_OUT_SECOND_LEFT_LAST_EVENT_TOPIC, loggedOutSecondLeftLastEventConfig, null);
			
			initSingleConsumer(gameRoomClosedLoggedOutSecondLeftLastEventConsumer, KafkaUtils.GAME_ROOM_CLOSED_LOGGED_OUT_SECOND_LEFT_LAST_EVENT_TOPIC, gameRoomClosedLoggedOutSecondLeftLastEventConfig, null);
			
			initSingleConsumer(openByLeftBeforeGameStartedEventConsumer, KafkaUtils.OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC, openByLeftBeforeGameStartedEventConfig, null);
			
			initSingleConsumer(gameRoomClosedOpenByLeftBeforeGameStartedEventConsumer, KafkaUtils.GAME_ROOM_CLOSED_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC, gameRoomClosedOpenByLeftBeforeGameStartedEventConfig, null);
			
			initSingleConsumer(openByLeftEventConsumer, KafkaUtils.OPENBY_LEFT_EVENT_TOPIC, openByLeftEventConfig, null);
			
			initSingleConsumer(watcherLeftLastEventConsumer, KafkaUtils.WATCHER_LEFT_LAST_EVENT_TOPIC, watcherLeftLastEventConfig, null);
			
			initSingleConsumer(gameRoomClosedWatcherLeftLastEventConsumer, KafkaUtils.GAME_ROOM_CLOSED_WATCHER_LEFT_LAST_EVENT_TOPIC, gameRoomClosedWatcherLeftLastEventConfig, null);
			
			initSingleConsumer(watcherLeftEventConsumer, KafkaUtils.WATCHER_LEFT_EVENT_TOPIC, watcherLeftEventConfig, null);
			
			executeProducersAndConsumers(Arrays.asList(newGameRoomOpenedEventConsumer, 
					userAddedAsWatcherEventConsumer,
					userAddedAsSecondPlayerEventConsumer,
					loggedOutOpenByLeftBeforeGameStartedEventConsumer,
					gameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConsumer,
					loggedOutOpenByLeftEventConsumer,
					loggedOutWatcherLeftLastEventConsumer,
					gameRoomClosedLoggedOutWatcherLeftLastEventConsumer,
					loggedOutWatcherLeftEventConsumer,
					loggedOutOpenByLeftFirstEventConsumer,
					loggedOutSecondLeftFirstEventConsumer,
					loggedOutSecondLeftEventConsumer,
					loggedOutOpenByLeftLastEventConsumer,
					gameRoomClosedLoggedOutOpenByLeftLastEventConsumer,
					loggedOutSecondLeftLastEventConsumer,
					gameRoomClosedLoggedOutSecondLeftLastEventConsumer,
					openByLeftBeforeGameStartedEventConsumer,
					gameRoomClosedOpenByLeftBeforeGameStartedEventConsumer,
					openByLeftEventConsumer,
					watcherLeftLastEventConsumer,
					gameRoomClosedWatcherLeftLastEventConsumer,
					watcherLeftEventConsumer));
		}
	}

	@Override
	public void initKafkaCommandsProducers() {
		
	}

	@Override
	public void initKafkaEventsProducers() {
		initSingleProducer(getAllGameRoomsAckEventProducer, KafkaUtils.GET_ALL_GAME_ROOMS_EVENT_ACK_TOPIC, getAllGameRoomsQueue);
		
		initSingleProducer(getLobbyUpdateViewAckEventProducer, KafkaUtils.GET_LOBBY_UPDATE_VIEW_ACK_EVENT_TOPIC, getLobbyUpdateViewQueue);
		
		initSingleProducer(initGameRoomCompletedEventProducer, KafkaUtils.INIT_GAME_ROOM_COMPLETED_EVENT_TOPIC, initGameRoomCompletedQueue);
		
		executeProducersAndConsumers(Arrays.asList(getAllGameRoomsAckEventProducer,
				getLobbyUpdateViewAckEventProducer,
				initGameRoomCompletedEventProducer));	
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public void engineShutdown() {
		logger.info("about to do shutdown.");	
		selfShutdown();
		logger.info("shutdown compeleted.");
	}	
	
	private void initSingleConsumer(ISimpleConsumer consumer, String topic, SimpleConsumerConfig consumerConfig, ConsumerToProducerQueue queue) {
		consumer.setTopic(topic);
		consumer.setSimpleConsumerConfig(consumerConfig);
		consumer.initConsumer();	
		consumer.setConsumerToProducerQueue(queue);
	}
	
	private void initSingleProducer(ISimpleProducer producer, String topic, ConsumerToProducerQueue queue) {
		producer.setTopic(topic);	
		producer.setConsumerToProducerQueue(queue);
	}
	
	private void shutdownSingleConsumer(ISimpleConsumer consumer) {
		consumer.setRunning(false);
		consumer.getScheduledExecutor().shutdown();	
	}
	
	private void shutdownSingleProducer(ISimpleProducer producer) {
		producer.setRunning(false);
		producer.getScheduledExecutor().shutdown();	
	}
	
	private void selfShutdown(){
		this.executor.shutdown();
	}
	
	private void executeProducersAndConsumers(List<Runnable> jobs){
		for(Runnable job:jobs)
			executor.execute(job);
	}
}
