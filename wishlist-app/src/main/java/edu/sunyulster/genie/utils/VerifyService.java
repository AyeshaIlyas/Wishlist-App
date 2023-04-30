package edu.sunyulster.genie.utils;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import edu.sunyulster.genie.db.DbConstants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;

@ApplicationScoped
public class VerifyService {

    @Inject 
    MongoDatabase db;

    public Document verifyUser(String userId) throws AuthenticationException {
        MongoCollection<Document> users = db.getCollection(DbConstants.USERS);
        Document user = users.find(Filters.eq(DbConstants.AUTH_ID, new ObjectId(userId))).first();
        // if user was deleted but token is still valid this is possible:
        if (user == null) 
            throw new AuthenticationException("User does not exist");
        return user;
    }
    
    
}
