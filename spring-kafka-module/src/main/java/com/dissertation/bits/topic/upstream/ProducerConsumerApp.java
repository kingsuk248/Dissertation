package com.dissertation.bits.topic.upstream;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.dissertation.bits.model.Display;
import com.dissertation.bits.model.Search;
import com.dissertation.bits.model.Social;
import com.dissertation.bits.utilities.Constants;
import com.dissertation.bits.utilities.DataPopulator;

@SpringBootApplication
public class ProducerConsumerApp {
	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(ProducerConsumerApp.class, args);

		UpstreamMessageProducer producer = context.getBean(UpstreamMessageProducer.class);
		UpstreamMessageListener listener = context.getBean(UpstreamMessageListener.class);

		for (int i = 0; i < Constants.BATCH_SIZE; i++) {
			Search search = getSearchObject();
			producer.sendSearchMessage(search);
			//Display display = getDisplayObject();
			//producer.sendDisplayMessage(display);
			//Social social = getSocialObject();
			//producer.sendSocialMessage(social);
		}
		listener.latch.await(5, TimeUnit.SECONDS);
		context.close();
	}

	@Bean
	public UpstreamMessageProducer upstreamMessageProducer() {
		return new UpstreamMessageProducer();
	}

	@Bean
	public UpstreamMessageListener upstreamMessageListener() {
		return new UpstreamMessageListener();
	}

	public static class UpstreamMessageProducer {

		@Autowired
		private KafkaTemplate<String, Search> searchUpstreamKafkaTemplate;

		@Value(value = "${search.topic.name}")
		private String searchTopicName;

		public void sendSearchMessage(Search message) {
			searchUpstreamKafkaTemplate.send(searchTopicName, message);
		}
		
		/*
		 * @Autowired private KafkaTemplate<String, Display>
		 * displayUpstreamKafkaTemplate;
		 * 
		 * @Value(value = "${display.topic.name}") private String displayTopicName;
		 * 
		 * public void sendDisplayMessage(Display message) {
		 * displayUpstreamKafkaTemplate.send(displayTopicName, message); }
		 * 
		 * @Autowired private KafkaTemplate<String, Social> socialUpstreamKafkaTemplate;
		 * 
		 * @Value(value = "${social.topic.name}") private String socialTopicName;
		 * 
		 * public void sendSocialMessage(Social message) {
		 * socialUpstreamKafkaTemplate.send(socialTopicName, message); }
		 */

	}

	public static class UpstreamMessageListener {

		private CountDownLatch latch = new CountDownLatch(3);

		@KafkaListener(topics = "${search.topic.name}", containerFactory = "searchUpstreamKafkaListenerContainerFactory")
		public void searchUpstreamTopicListener(Search search) {
			System.out.println("Recieved search message: " + search);
			this.latch.countDown();
		}
		
		@KafkaListener(topics = "${display.topic.name}", containerFactory = "displayUpstreamKafkaListenerContainerFactory")
		public void displayUpstreamTopicListener(Display display) {
			System.out.println("Recieved display message: " + display);
			this.latch.countDown();
		}
		
		@KafkaListener(topics = "${social.topic.name}", containerFactory = "socialUpstreamKafkaListenerContainerFactory")
		public void socialUpstreamTopicListener(Social social) {
			System.out.println("Recieved social message: " + social);
			this.latch.countDown();
		}
	}

	@Configuration
	public class KafkaUpstreamProducerConfig {

		@Value(value = "${kafka.bootstrapAddress}")
		private String bootstrapAddress;

		@Bean
		public ProducerFactory<String, Search> searchUpstreamMessageProducerFactory() {
			Map<String, Object> configProps = new HashMap<>();
			configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
			configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
			return new DefaultKafkaProducerFactory<>(configProps);
		}

		@Bean
		public KafkaTemplate<String, Search> searchUpstreamKafkaTemplate() {
			return new KafkaTemplate<>(searchUpstreamMessageProducerFactory());
		}
		
		@Bean
		public ProducerFactory<String, Search> displayUpstreamMessageProducerFactory() {
			Map<String, Object> configProps = new HashMap<>();
			configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
			configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
			return new DefaultKafkaProducerFactory<>(configProps);
		}
		
		@Bean
		public KafkaTemplate<String, Search> displayUpstreamKafkaTemplate() {
			return new KafkaTemplate<>(displayUpstreamMessageProducerFactory());
		}
		
		@Bean
		public ProducerFactory<String, Search> socialUpstreamMessageProducerFactory() {
			Map<String, Object> configProps = new HashMap<>();
			configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
			configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
			return new DefaultKafkaProducerFactory<>(configProps);
		}
		
		@Bean
		public KafkaTemplate<String, Search> socialUpstreamKafkaTemplate() {
			return new KafkaTemplate<>(socialUpstreamMessageProducerFactory());
		}
	}

	@EnableKafka
	@Configuration
	public class KafkaUpstreamConsumerConfig {

		@Value(value = "${kafka.bootstrapAddress}")
		private String bootstrapAddress;

		public ConsumerFactory<String, Search> createSearchUpstreamConsumerFactory() {
			Map<String, Object> props = new HashMap<>();
			props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
			props.put(ConsumerConfig.GROUP_ID_CONFIG, "search-upstream");
			return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
					new JsonDeserializer<>(Search.class));
		}
		
		public ConsumerFactory<String, Display> createDisplayUpstreamConsumerFactory() {
			Map<String, Object> props = new HashMap<>();
			props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
			props.put(ConsumerConfig.GROUP_ID_CONFIG, "display-upstream");
			return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
					new JsonDeserializer<>(Display.class));
		}
		
		public ConsumerFactory<String, Social> createSocialUpstreamConsumerFactory() {
			Map<String, Object> props = new HashMap<>();
			props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
			props.put(ConsumerConfig.GROUP_ID_CONFIG, "social-upstream");
			return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
					new JsonDeserializer<>(Social.class));
		}

		@Bean
		public ConcurrentKafkaListenerContainerFactory<String, Search> searchUpstreamKafkaListenerContainerFactory() {
			ConcurrentKafkaListenerContainerFactory<String, Search> factory = new ConcurrentKafkaListenerContainerFactory<>();
			factory.setConsumerFactory(createSearchUpstreamConsumerFactory());
			return factory;
		}
		
		@Bean
		public ConcurrentKafkaListenerContainerFactory<String, Display> displayUpstreamKafkaListenerContainerFactory() {
			ConcurrentKafkaListenerContainerFactory<String, Display> factory = new ConcurrentKafkaListenerContainerFactory<>();
			factory.setConsumerFactory(createDisplayUpstreamConsumerFactory());
			return factory;
		}
		
		@Bean
		public ConcurrentKafkaListenerContainerFactory<String, Social> socialUpstreamKafkaListenerContainerFactory() {
			ConcurrentKafkaListenerContainerFactory<String, Social> factory = new ConcurrentKafkaListenerContainerFactory<>();
			factory.setConsumerFactory(createSocialUpstreamConsumerFactory());
			return factory;
		}
	}
	
	private static Search getSearchObject() {
		Search search = DataPopulator.createSampleSingleSearchObject("allianz");
		return search;
	}
	
	private static Display getDisplayObject() {
		Display display = DataPopulator.createSampleSingleDisplayObject("allianz");
		return display;
	}
	
	private static Social getSocialObject() {
		Social social = DataPopulator.createSampleSingleSocialObject("allianz");
		return social;
	}
}
