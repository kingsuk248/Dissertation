package com.dissertation.bits.persistence;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

/**
 * Class for configuration of the mongodb with application server
 *
 * @author kisarkar
 *
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.dissertation.bits")
public class MongoConfiguration {

    @Bean
    @Primary
    public MongoDbFactory mongoDbFactory() throws UnknownHostException, UnsupportedEncodingException {
        final MongoClient mongoClient = new MongoClient("localhost" , 27017);
        return new MultiTenantMongoDbFactory(mongoClient, "allianzDB");

    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() throws UnknownHostException, UnsupportedEncodingException {
        return new MongoTemplate(mongoDbFactory());
    }
}
