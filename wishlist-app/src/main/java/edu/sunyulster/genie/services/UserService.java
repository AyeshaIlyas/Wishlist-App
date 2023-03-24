package edu.sunyulster.genie.services;

import static com.mongodb.client.model.Filters.eq;
import static edu.sunyulster.genie.utils.Validator.isEmailValid;
import static edu.sunyulster.genie.utils.Validator.isNameValid;

import java.util.Arrays;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    @Inject
    MongoDatabase db;

    public void createUser(User user, String authId) throws InvalidDataException {
        // - - - - - - - - - -  VALIDATION - - - - - - - - - - // 
        // role validation??
    
        if (isNameValid(user.getFirstName()) || isNameValid(user.getLastName())) 
            throw new InvalidDataException("Name is not valid");

        if (!isEmailValid(user.getEmail())) 
            throw new InvalidDataException("Email is not valid");
    
        // check if user was already added : can occur for ex if a request with the JWT 
        //      coming from the auth-server with a role of auth-server is replayed
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(eq("email", user.getEmail())).first();
        if (match != null) 
            throw new IllegalArgumentException("user already exists"); // what error here??
    
        users.insertOne(convertUserToDocument(user, authId));
    }

    public void deleteUser(String userId) {
        // delete wishlists, items, everything associated with user

        // remove user from db
        MongoCollection<Document> users = db.getCollection("users");
        users.deleteOne(eq("authId", new ObjectId(userId)));
    }


    private Document convertUserToDocument(User user, String authId) {
        Document userDoc = new Document()
            .append("_id", new ObjectId())
            .append("authId", new ObjectId(authId))
            .append("email", user.getEmail())
            .append("firstName", user.getFirstName())
            .append("lastName", user.getLastName())
            .append("roles", Arrays.asList(user.getRoles()));

        return userDoc;
    }
    
    
}
