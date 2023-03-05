package edu.sunulster.genie.auth.services;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class UserService {

    @Inject
    MongoDatabase db;

    public String getUser() {
        System.out.println(db);
        MongoCollection<Document> collection = db.getCollection("users");
        collection.insertOne(new Document());
        System.out.println(collection);
        for (Document d : collection.find()) {
            System.out.println(d.toJson());
        }
        return "";
    }
    
}
