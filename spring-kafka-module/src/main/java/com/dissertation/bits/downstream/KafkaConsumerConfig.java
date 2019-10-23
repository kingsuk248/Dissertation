package com.dissertation.bits.downstream;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.dissertation.bits.model.Search;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    public ConsumerFactory<String, Search> createSearchConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "search");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Search.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Search> searchKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Search> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createSearchConsumerFactory());
        return factory;
    }
    
    public ConsumerFactory<String, Search> createDisplayConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "display");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Search.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Search> displayKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Search> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createDisplayConsumerFactory());
        return factory;
    }

    
    public ConsumerFactory<String, Search> createSocialConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "social");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Search.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Search> socialKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Search> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createSocialConsumerFactory());
        return factory;
    }
}