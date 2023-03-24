package edu.sunyulster.genie.db;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class MongoProducer {
    @Inject
    @ConfigProperty(name = "mongo.hostname", defaultValue = "localhost")
    String hostname;

    @Inject
    @ConfigProperty(name = "mongo.port", defaultValue = "27017")
    int port;

    @Inject
    @ConfigProperty(name = "mongo.dbname", defaultValue = "wishlistAppDb")
    String dbName;

    @Produces
    public MongoClient createMongo() {
        return MongoClients.create(String.format("mongodb://%s:%s", hostname, port));
    }

    @Produces
    public MongoDatabase createDB(MongoClient client) {
        return client.getDatabase(dbName);
    }

    public void close(@Disposes MongoClient client) {
        client.close();
    }
}