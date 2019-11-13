package com.dissertation.bits.application;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import com.dissertation.bits.cache.DisplayInMemoryCache;
import com.dissertation.bits.cache.SearchInMemoryCache;
import com.dissertation.bits.cache.SocialInMemoryCache;
import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;
import com.dissertation.bits.utilities.Constants;
import com.dissertation.bits.utilities.DataPopulator;

@SpringBootApplication
@ComponentScan(basePackages = "com.dissertation.bits")
public class Application {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		MessageProducer producer = context.getBean(MessageProducer.class);
		MessageListener listener = context.getBean(MessageListener.class);

		Runnable searchThread = () -> {
			for (int i = 0; i < Constants.DEMO_NUMBER_MESSAGES; i++) {
				Search search = DataPopulator.getSearchObjectFromBatch("allianz");
				producer.sendSearchMessage(search);
				try {
					listener.latch.await(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
				}
			}
		};

		Runnable displayThread = () -> {
			for (int i = 0; i < Constants.DEMO_NUMBER_MESSAGES; i++) {
				Display display = DataPopulator.getDisplayObjectFromBatch("allianz");
				producer.sendDisplayMessage(display);
				try {
					listener.latch.await(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
				}
			}
		};

		Runnable socialThread = () -> {
			for (int i = 0; i < Constants.DEMO_NUMBER_MESSAGES; i++) {
				Social social = DataPopulator.getSocialObjectFromBatch("allianz");
				producer.sendSocialMessage(social);
				try {
					listener.latch.await(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
				}
			}
		};

		new Thread(searchThread).start();
		new Thread(displayThread).start();
		new Thread(socialThread).start();
		context.stop();
		context.close();
	}

	@Bean
	public MessageProducer messageProducer() {
		return new MessageProducer();
	}

	@Bean
	public MessageListener messageListener() {
		return new MessageListener();
	}

	public static class MessageProducer {

		@Autowired
		private KafkaTemplate<String, Search> searchKafkaTemplate;

		@Autowired
		private KafkaTemplate<String, Display> displayKafkaTemplate;

		@Autowired
		private KafkaTemplate<String, Social> socialKafkaTemplate;

		@Value(value = "${downstream.search.topic.name}")
		private String downstreamSearchTopicName;

		@Value(value = "${downstream.display.topic.name}")
		private String downstreamDisplayTopicName;

		@Value(value = "${downstream.social.topic.name}")
		private String downstreamSocialTopicName;

		public void sendSearchMessage(Search message) {
			searchKafkaTemplate.send(downstreamSearchTopicName, message);
		}

		public void sendDisplayMessage(Display message) {
			displayKafkaTemplate.send(downstreamDisplayTopicName, message);
		}

		public void sendSocialMessage(Social message) {
			socialKafkaTemplate.send(downstreamSocialTopicName, message);
		}
	}

	public static class MessageListener {

		private CountDownLatch latch = new CountDownLatch(3);

		@KafkaListener(topics = "${downstream.search.topic.name}", containerFactory = "searchKafkaListenerContainerFactory")
		public void downstreamSearchTopicListener(Search search) {
			System.out.println("Recieved downstream search message: " + search);
			long currentTime = System.currentTimeMillis();
			SearchInMemoryCache.put(currentTime, search);
		}

		@KafkaListener(topics = "${downstream.display.topic.name}", containerFactory = "displayKafkaListenerContainerFactory")
		public void downstreamDisplayTopicListener(Display display) {
			System.out.println("Recieved downstream display message: " + display);
			long currentTime = System.currentTimeMillis();
			DisplayInMemoryCache.put(currentTime, display);
		}

		@KafkaListener(topics = "${downstream.social.topic.name}", containerFactory = "socialKafkaListenerContainerFactory")
		public void downstreamSocialTopicListener(Social social) {
			System.out.println("Recieved downstream social message: " + social);
			long currentTime = System.currentTimeMillis();
			SocialInMemoryCache.put(currentTime, social);
		}
	}
}
