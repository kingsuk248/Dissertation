package com.dissertation.bits.persistence;

import java.net.UnknownHostException;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

@EnableMongoRepositories(basePackages = "com.dissertation.bits")
public class MultiTenantMongoDbFactory extends SimpleMongoDbFactory {


    public String DEFAULT_DB = "allianzDB";

    public MultiTenantMongoDbFactory(final MongoClientURI uri) throws UnknownHostException {
        super(uri);
    }

    public MultiTenantMongoDbFactory(final MongoClient mongoClient, final String databaseName)
            throws UnknownHostException {

        super(mongoClient, databaseName);
    }

    @Override
    public MongoDatabase getDb() throws DataAccessException {
        return super.getDb(DEFAULT_DB);
    }
}
