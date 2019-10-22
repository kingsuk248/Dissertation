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

@SpringBootApplication
public class ProducerConsumerApplication {
	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(ProducerConsumerApplication.class, args);

		MessageProducer producer = context.getBean(MessageProducer.class);
		MessageListener listener = context.getBean(MessageListener.class);

		DummyObject dummyObject = createDummyObject();
		producer.sendSearchMessage(dummyObject);
		listener.latch.await(5, TimeUnit.SECONDS);
		context.close();
	}

	private static DummyObject createDummyObject() {
		DummyObject dummyObject = new DummyObject();
		dummyObject.setFirstName("John");
		dummyObject.setLastName("Doe");
		dummyObject.setAge(40);
		return dummyObject;
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
		private KafkaTemplate<String, DummyObject> dummyKafkaTemplate;

		@Value(value = "${test.topic.name}")
		private String testTopicName;

		public void sendSearchMessage(DummyObject message) {
			dummyKafkaTemplate.send(testTopicName, message);
		}
	}

	public static class MessageListener {

		private CountDownLatch latch = new CountDownLatch(3);

		@KafkaListener(topics = "${test.topic.name}", containerFactory = "dummyKafkaListenerContainerFactory")
		public void downstreamSearchTopicListener(DummyObject dummyObject) {
			System.out.println("Recieved dummy message: " + dummyObject);
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
		public ProducerFactory<String, DummyObject> dummyMessageProducerFactory() {
			Map<String, Object> configProps = new HashMap<>();
			configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
			configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
			return new DefaultKafkaProducerFactory<>(configProps);
		}

		@Bean
		public KafkaTemplate<String, DummyObject> dummyKafkaTemplate() {
			return new KafkaTemplate<>(dummyMessageProducerFactory());
		}
	}

	@EnableKafka
	@Configuration
	public class KafkaConsumerConfig {

		@Value(value = "${kafka.bootstrapAddress}")
		private String bootstrapAddress;

		public ConsumerFactory<String, DummyObject> createDummyConsumerFactory() {
			Map<String, Object> props = new HashMap<>();
			props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
			props.put(ConsumerConfig.GROUP_ID_CONFIG, "dummy");
			return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
					new JsonDeserializer<>(DummyObject.class));
		}

		@Bean
		public ConcurrentKafkaListenerContainerFactory<String, DummyObject> dummyKafkaListenerContainerFactory() {
			ConcurrentKafkaListenerContainerFactory<String, DummyObject> factory = new ConcurrentKafkaListenerContainerFactory<>();
			factory.setConsumerFactory(createDummyConsumerFactory());
			return factory;
		}

	}
}
