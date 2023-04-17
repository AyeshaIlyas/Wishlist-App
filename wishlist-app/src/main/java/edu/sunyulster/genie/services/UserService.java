package edu.sunyulster.genie.services;

import static com.mongodb.client.model.Filters.eq;
import static edu.sunyulster.genie.utils.Validator.isEmailValid;
import static edu.sunyulster.genie.utils.Validator.isNameValid;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.Filters;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;

@ApplicationScoped
public class UserService {

    @Inject
    MongoDatabase db;

    public void create(User user, String authId) throws InvalidDataException {
        // - - - - - - - - - -  VALIDATION - - - - - - - - - - // 
        // role validation??
    
        if (!isNameValid(user.getFirstName()) || !isNameValid(user.getLastName())) 
            throw new InvalidDataException("Name is not valid");

        if (!isEmailValid(user.getEmail())) 
            throw new InvalidDataException("Email is not valid");
    
        // check if user was already added : can occur for ex if a request with the JWT 
        //      coming from the auth-server with a role of auth-server is replayed
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(eq("email", user.getEmail())).first();
        if (match != null) 
            throw new IllegalArgumentException("user already exists"); // what error here??
    
        users.insertOne(userToDocument(user, authId));
    }

    public User get(String userId) throws AuthenticationException {
        MongoCollection<Document> users = db.getCollection("users");
        Document user = users.find(eq("authId", new ObjectId(userId))).first();
        // if user was deleted but token is still valid this is possible:
        if (user == null) 
            throw new AuthenticationException("User does not exist");
        return documentToUser(user);
    }

    public void delete(String userId) throws AuthenticationException {
        // delete wishlists, items, everything associated with user

        // remove user from db
        MongoCollection<Document> users = db.getCollection("users");
        DeleteResult result = users.deleteOne(eq("authId", new ObjectId(userId)));
        // if user was already deleted
        if (result.getDeletedCount() == 0) 
            throw new AuthenticationException("User does not exist");
    
    }

    

    public User updateList(String wishlistId, String userEmail) {
        //get user with userEmail
        MongoCollection<Document> users = db.getCollection("users");
        Document user = users.find(eq("email", userEmail)).first();
        User newUser=documentToUser(user);
        newUser.addSharedList(wishlistId);
        Bson update=null;
        Bson filter = Filters.eq("email", new ObjectId(newUser.getEmail()));

        Bson listUpdate = Updates.addToSet("sharedWith", newUser.getSharedLists().get(0));
        update = listUpdate;
        users.updateOne(filter, update);

        return documentToUser(users.find(eq("email", userEmail)).first());
    }

    public static Document userToDocument(User user, String authId) {
        Document userDoc = new Document()
            .append("_id", new ObjectId())
            .append("authId", new ObjectId(authId))
            .append("email", user.getEmail())
            .append("firstName", user.getFirstName())
            .append("lastName", user.getLastName())
            .append("sharedLists", user.getSharedLists() )
            .append("wishlists", new ArrayList<ObjectId>());

        return userDoc;
    }

    public static User documentToUser(Document user) {
        return new User(user.getString("email"), 
            user.getString("firstName"), 
            user.getString("lastName"));
    }
    


    
}
