package com.kafka.consumer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.kafka.model.message.Search;
import com.kafka.utilities.DataPopulator;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        
        MessageProducer producer = context.getBean(MessageProducer.class);
        MessageListener listener = context.getBean(MessageListener.class);
        
        Search search = DataPopulator.createSampleSearchObject("allianz");
        producer.sendSearchMessage(search);
        listener.latch.await(10, TimeUnit.SECONDS);
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


        @Value(value = "${downstream.search.topic.name}")
        private String downstreamSearchTopicName;

        public void sendSearchMessage(Search message) {
            
        	searchKafkaTemplate.send(downstreamSearchTopicName, message);
            /*ListenableFuture<SendResult<String, Search>> future = searchKafkaTemplate.send(downstreamSearchTopicName, message);
            
            future.addCallback(new ListenableFutureCallback<SendResult<String, Search>>() {

                @Override
                public void onSuccess(SendResult<String, Search> result) {
                    System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
                }
                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
                }
            });*/
        }
    }

    public static class MessageListener {

        private CountDownLatch latch = new CountDownLatch(3);
        
        @KafkaListener(topics = "${downstream.search.topic.name}", containerFactory = "searchKafkaListenerContainerFactory")
        public void downstreamSearchTopicListener(Search search) {
            System.out.println("Recieved search message: " + search);
            this.latch.countDown();
        }
    }
}
