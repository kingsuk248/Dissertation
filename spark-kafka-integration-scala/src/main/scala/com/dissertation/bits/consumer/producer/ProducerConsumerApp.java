package com.dissertation.bits.consumer.producer;

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

import com.dissertation.bits.model.Search;
import com.dissertation.bits.utilities.Constants;
import com.dissertation.bits.utilities.DataPopulator;

@SpringBootApplication
public class ProducerConsumerApp {
	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(ProducerConsumerApp.class, args);

		MessageProducer producer = context.getBean(MessageProducer.class);
		MessageListener listener = context.getBean(MessageListener.class);

		for (int i = 0; i < Constants.BATCH_SIZE; i++) {
			Search search = createSearchObject();
			producer.sendSearchMessage(search);
		}
		listener.latch.await(5, TimeUnit.SECONDS);
		context.close();
	}

	private static Search createSearchObject() {
		Search search = DataPopulator.createSampleSearchObject("allianz");
		return search;
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

		@Value(value = "${search.topic.name}")
		private String searchTopicName;

		public void sendSearchMessage(Search message) {
			searchKafkaTemplate.send(searchTopicName, message);
		}
	}

	public static class MessageListener {

		private CountDownLatch latch = new CountDownLatch(3);

		@KafkaListener(topics = "${search.topic.name}", containerFactory = "searchKafkaListenerContainerFactory")
		public void downstreamSearchTopicListener(Search search) {
			System.out.println("Recieved search message: " + search);
			this.latch.countDown();
		}
	}

	@Configuration
	public class KafkaProducerConfig {

		@Value(value = "${kafka.bootstrapAddress}")
		private String bootstrapAddress;

		@Bean
		public ProducerFactory<String, String> producerFactory() {
			Map<String, Object> configProps = new HashMap<>();
			configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
			configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			return new DefaultKafkaProducerFactory<>(configProps);
		}

		@Bean
		public KafkaTemplate<String, String> kafkaTemplate() {
			return new KafkaTemplate<>(producerFactory());
		}

		@Bean
		public ProducerFactory<String, Search> searchMessageProducerFactory() {
			Map<String, Object> configProps = new HashMap<>();
			configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
			configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
			return new DefaultKafkaProducerFactory<>(configProps);
		}

		@Bean
		public KafkaTemplate<String, Search> searchKafkaTemplate() {
			return new KafkaTemplate<>(searchMessageProducerFactory());
		}
	}

	@EnableKafka
	@Configuration
	public class KafkaConsumerConfig {

		@Value(value = "${kafka.bootstrapAddress}")
		private String bootstrapAddress;

		public ConsumerFactory<String, Search> createSearchConsumerFactory() {
			Map<String, Object> props = new HashMap<>();
			props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
			props.put(ConsumerConfig.GROUP_ID_CONFIG, "search");
			return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
					new JsonDeserializer<>(Search.class));
		}

		@Bean
		public ConcurrentKafkaListenerContainerFactory<String, Search> searchKafkaListenerContainerFactory() {
			ConcurrentKafkaListenerContainerFactory<String, Search> factory = new ConcurrentKafkaListenerContainerFactory<>();
			factory.setConsumerFactory(createSearchConsumerFactory());
			return factory;
		}

	}
}
