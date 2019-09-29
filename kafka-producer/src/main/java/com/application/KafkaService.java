package com.application;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.model.Tenant;
import com.serializer.KafkaJsonSerializer;

public class KafkaService {
	public static final String BOOTSTRAP_SERVER = "localhost:9092";
	public static final String DOWNSTREAM_SEARCH_TOPIC = "downstream-search-topic";
	public static final String DOWNSTREAM_DISPLAY_TOPIC = "downstream-display-topic";
	public static final String DOWNSTREAM_SOCIAL_TOPIC = "downstream-social-topic";

	private final Producer<String, Tenant> searchChannelKafkaProducer;

	public KafkaService() {
		Properties props = new Properties();
		props.put("bootstrap.servers", BOOTSTRAP_SERVER);
		searchChannelKafkaProducer = new KafkaProducer<String, Tenant>(props, new StringSerializer(),
				new KafkaJsonSerializer<Tenant>());
	}

	public void send(Tenant search) {
		searchChannelKafkaProducer.send(
				new ProducerRecord<>(DOWNSTREAM_SEARCH_TOPIC, String.valueOf(System.currentTimeMillis()), search));
	}
}
