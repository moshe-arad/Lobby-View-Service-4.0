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
import org.moshe.arad.kafka.consumers.config.GameRoomClosedEventConfig;
import org.moshe.arad.kafka.consumers.config.GameRoomClosedEventLogoutConfig;
import org.moshe.arad.kafka.consumers.config.GetAllGameRoomsCommandConfig;
import org.moshe.arad.kafka.consumers.config.GetLobbyUpdateViewCommandConfig;
import org.moshe.arad.kafka.consumers.config.NewGameRoomOpenedEventConfig;
import org.moshe.arad.kafka.consumers.config.SimpleConsumerConfig;
import org.moshe.arad.kafka.consumers.config.UserAddedAsSecondPlayerEventConfig;
import org.moshe.arad.kafka.consumers.config.UserAddedAsWatcherEventConfig;
import org.moshe.arad.kafka.consumers.config.WatcherRemovedEventConfig;
import org.moshe.arad.kafka.consumers.events.GameRoomClosedEventConsumer;
import org.moshe.arad.kafka.consumers.events.GameRoomClosedEventLogoutConsumer;
import org.moshe.arad.kafka.consumers.events.NewGameRoomOpenedEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserAddedAsSecondPlayerEventConsumer;
import org.moshe.arad.kafka.consumers.events.UserAddedAsWatcherEventConsumer;
import org.moshe.arad.kafka.consumers.events.WatcherRemovedEventConsumer;
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
	
	private GameRoomClosedEventConsumer gameRoomClosedEventConsumer;
	
	@Autowired
	private GameRoomClosedEventConfig GameRoomClosedEventConfig;
	
	private UserAddedAsWatcherEventConsumer userAddedAsWatcherEventConsumer;
	
	@Autowired
	private UserAddedAsWatcherEventConfig userAddedAsWatcherEventConfig;
	
	private GetAllGameRoomsCommandConsumer getAllGameRoomsCommandConsumer;
	
	@Autowired
	private GetAllGameRoomsCommandConfig getAllGameRoomsCommandConfig;
	
	@Autowired
	private SimpleEventsProducer<GetAllGameRoomsAckEvent> getAllGameRoomsAckEventProducer;
	
	private ConsumerToProducerQueue getAllGameRoomsQueue;
	
	private GameRoomClosedEventLogoutConsumer gameRoomClosedEventLogoutConsumer;
	
	@Autowired
	private GameRoomClosedEventLogoutConfig gameRoomClosedEventLogoutConfig;
	
	private WatcherRemovedEventConsumer watcherRemovedEventConsumer;
	
	@Autowired
	private WatcherRemovedEventConfig watcherRemovedEventConfig;
	
	private GetLobbyUpdateViewCommandConsumer getLobbyUpdateViewCommandConsumer;
	
	@Autowired
	private GetLobbyUpdateViewCommandConfig getLobbyUpdateViewCommandConfig;
	
	private ConsumerToProducerQueue getLobbyUpdateViewQueue;
	
	@Autowired
	private SimpleEventsProducer<GetLobbyUpdateViewAckEvent> getLobbyUpdateViewAckEventProducer;
	
	private UserAddedAsSecondPlayerEventConsumer userAddedAsSecondPlayerEventConsumer;
	
	@Autowired
	private UserAddedAsSecondPlayerEventConfig userAddedAsSecondPlayerEventConfig;
	
	private ConsumerToProducerQueue initGameRoomCompletedQueue;
	
	@Autowired
	private SimpleEventsProducer<InitGameRoomCompletedEvent> initGameRoomCompletedEventProducer;
	
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
			gameRoomClosedEventConsumer = context.getBean(GameRoomClosedEventConsumer.class);
			userAddedAsWatcherEventConsumer = context.getBean(UserAddedAsWatcherEventConsumer.class);
			gameRoomClosedEventLogoutConsumer = context.getBean(GameRoomClosedEventLogoutConsumer.class);
			watcherRemovedEventConsumer = context.getBean(WatcherRemovedEventConsumer.class);
			userAddedAsSecondPlayerEventConsumer = context.getBean(UserAddedAsSecondPlayerEventConsumer.class);
			
			initSingleConsumer(newGameRoomOpenedEventConsumer, KafkaUtils.NEW_GAME_ROOM_OPENED_EVENT_TOPIC, newGameRoomOpenedEventConfig, null);
			
			initSingleConsumer(gameRoomClosedEventConsumer, KafkaUtils.GAME_ROOM_CLOSED_EVENT_TOPIC, GameRoomClosedEventConfig, null);
			
			initSingleConsumer(userAddedAsWatcherEventConsumer, KafkaUtils.USER_ADDED_AS_WATCHER_EVENT_TOPIC, userAddedAsWatcherEventConfig, null);
			
			initSingleConsumer(gameRoomClosedEventLogoutConsumer, KafkaUtils.GAME_ROOM_CLOSED_EVENT_LOGOUT_TOPIC, gameRoomClosedEventLogoutConfig, null);
			
			initSingleConsumer(watcherRemovedEventConsumer, KafkaUtils.WATCHER_REMOVED_EVENT_TOPIC, watcherRemovedEventConfig, null);
			
			initSingleConsumer(userAddedAsSecondPlayerEventConsumer, KafkaUtils.USER_ADDED_AS_SECOND_PLAYER_EVENT_TOPIC, userAddedAsSecondPlayerEventConfig, initGameRoomCompletedQueue);
			
			executeProducersAndConsumers(Arrays.asList(newGameRoomOpenedEventConsumer, 
					gameRoomClosedEventConsumer,
					userAddedAsWatcherEventConsumer,
					gameRoomClosedEventLogoutConsumer,
					watcherRemovedEventConsumer,
					userAddedAsSecondPlayerEventConsumer));
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
