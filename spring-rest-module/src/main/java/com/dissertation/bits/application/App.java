package com.dissertation.bits.application;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;

import com.dissertation.bits.cache.DisplayInMemoryCache;
import com.dissertation.bits.cache.SearchInMemoryCache;
import com.dissertation.bits.cache.SocialInMemoryCache;
import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;

@SpringBootApplication
@ComponentScan(basePackages = "com.dissertation.bits")
public class App {
	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(App.class, args);

		/*
		 * for (int i = 0; i < 5; i++) { Thread.sleep(100); Search search =
		 * DataPopulator.getSearchObjectFromBatch("allianz"); long currentTime =
		 * System.currentTimeMillis(); SearchInMemoryCache.put(currentTime, search); }
		 * for (int i = 0; i < 5; i++) { Thread.sleep(100); Display display =
		 * DataPopulator.getDisplayObjectFromBatch("allianz"); long currentTime =
		 * System.currentTimeMillis(); DisplayInMemoryCache.put(currentTime, display); }
		 * for (int i = 0; i < 5; i++) { Thread.sleep(100); Social social =
		 * DataPopulator.getSocialObjectFromBatch("allianz"); long currentTime =
		 * System.currentTimeMillis(); SocialInMemoryCache.put(currentTime, social); }
		 */
		
		MessageListener listener = context.getBean(MessageListener.class);
		listener.latch.await(5, TimeUnit.SECONDS);
	}
	
	@Bean
	public MessageListener messageListener() {
		return new MessageListener();
	}
	
	public static class MessageListener {

		private CountDownLatch latch = new CountDownLatch(3);

		@KafkaListener(topics = "${downstream.search.topic.name}", containerFactory = "searchKafkaListenerContainerFactory")
		public void downstreamSearchTopicListener(Search search) {
			System.out.println("Recieved downstream search message: " + search);
			long currentTime = System.currentTimeMillis();
			SearchInMemoryCache.put(currentTime, search);
			this.latch.countDown();
		}

		@KafkaListener(topics = "${downstream.display.topic.name}", containerFactory = "displayKafkaListenerContainerFactory")
		public void downstreamDisplayTopicListener(Display display) {
			System.out.println("Recieved downstream display message: " + display);
			long currentTime = System.currentTimeMillis();
			DisplayInMemoryCache.put(currentTime, display);
			this.latch.countDown();
		}

		@KafkaListener(topics = "${downstream.social.topic.name}", containerFactory = "socialKafkaListenerContainerFactory")
		public void downstreamSocialTopicListener(Social social) {
			System.out.println("Recieved downstream social message: " + social);
			long currentTime = System.currentTimeMillis();
			SocialInMemoryCache.put(currentTime, social);
			this.latch.countDown();
		}
	}
}
