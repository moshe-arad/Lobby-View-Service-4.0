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
import org.moshe.arad.kafka.consumers.config.GetAllGameRoomsCommandConfig;
import org.moshe.arad.kafka.consumers.config.GetLobbyUpdateViewCommandConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutOpenByLeftBeforeGameStartedEventConfig;
import org.moshe.arad.kafka.consumers.config.LoggedOutOpenByLeftEventConfig;
import org.moshe.arad.kafka.consumers.config.NewGameRoomOpenedEventConfig;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.config.UserAddedAsSecondPlayerEventConfig;
import org.moshe.arad.kafka.consumers.config.UserAddedAsWatcherEventConfig;
import org.moshe.arad.kafka.consumers.events.GameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutOpenByLeftBeforeGameStartedEventConsumer;
import org.moshe.arad.kafka.consumers.events.LoggedOutOpenByLeftEventConsumer;
import org.moshe.arad.kafka.consumers.events.NewGameRoomOpenedEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserAddedAsSecondPlayerEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserAddedAsWatcherEventConsumer;
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
			
			initSingleConsumer(newGameRoomOpenedEventConsumer, KafkaUtils.NEW_GAME_ROOM_OPENED_EVENT_TOPIC, newGameRoomOpenedEventConfig, null);
			
			initSingleConsumer(userAddedAsWatcherEventConsumer, KafkaUtils.USER_ADDED_AS_WATCHER_EVENT_TOPIC, userAddedAsWatcherEventConfig, null);
			
			initSingleConsumer(userAddedAsSecondPlayerEventConsumer, KafkaUtils.USER_ADDED_AS_SECOND_PLAYER_EVENT_TOPIC, userAddedAsSecondPlayerEventConfig, initGameRoomCompletedQueue);
			
			initSingleConsumer(loggedOutOpenByLeftBeforeGameStartedEventConsumer, KafkaUtils.LOGGED_OUT_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC, loggedOutOpenByLeftBeforeGameStartedEventConfig, null);
			
			initSingleConsumer(gameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConsumer, KafkaUtils.GAME_ROOM_CLOSED_LOGGED_OUT_OPENBY_LEFT_BEFORE_GAME_STARTED_EVENT_TOPIC, gameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConfig, null);
			
			initSingleConsumer(loggedOutOpenByLeftEventConsumer, KafkaUtils.LOGGED_OUT_OPENBY_LEFT_EVENT_TOPIC, loggedOutOpenByLeftEventConfig, null);
			
			executeProducersAndConsumers(Arrays.asList(newGameRoomOpenedEventConsumer, 
					userAddedAsWatcherEventConsumer,
					userAddedAsSecondPlayerEventConsumer,
					loggedOutOpenByLeftBeforeGameStartedEventConsumer,
					gameRoomClosedLoggedOutOpenByLeftBeforeGameStartedEventConsumer,
					loggedOutOpenByLeftEventConsumer));
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
