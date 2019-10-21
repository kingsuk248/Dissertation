package com.dissertation.bits.consumer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import com.dissertation.bits.cache.DisplayInMemoryCache;
import com.dissertation.bits.cache.SearchInMemoryCache;
import com.dissertation.bits.cache.SocialInMemoryCache;
import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;
import com.dissertation.bits.utilities.DataPopulator;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

		MessageProducer producer = context.getBean(MessageProducer.class);
		MessageListener listener = context.getBean(MessageListener.class);

		Search search = DataPopulator.createSampleSearchObject("allianz");
		producer.sendSearchMessage(search);
		listener.latch.await(10, TimeUnit.SECONDS);
		
		Display display = DataPopulator.createSampleDisplayObject("allianz");
		producer.sendDisplayMessage(display);
		listener.latch.await(10, TimeUnit.SECONDS);
		
		Social social = DataPopulator.createSampleSocialObject("allianz");
		producer.sendSocialMessage(social);
		listener.latch.await(10, TimeUnit.SECONDS);
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
			System.out.println("Recieved search message: " + search);
			long currentTime = System.currentTimeMillis();
			SearchInMemoryCache.put(currentTime, search);
			this.latch.countDown();
		}

		@KafkaListener(topics = "${downstream.display.topic.name}", containerFactory = "displayKafkaListenerContainerFactory")
		public void downstreamDisplayTopicListener(Display display) {
			System.out.println("Recieved display message: " + display);
			long currentTime = System.currentTimeMillis();
			DisplayInMemoryCache.put(currentTime, display);
			this.latch.countDown();
		}

		@KafkaListener(topics = "${downstream.social.topic.name}", containerFactory = "socialKafkaListenerContainerFactory")
		public void downstreamSocialTopicListener(Social social) {
			System.out.println("Recieved social message: " + social);
			long currentTime = System.currentTimeMillis();
			SocialInMemoryCache.put(currentTime, social);
			this.latch.countDown();
		}
	}
}
