package edu.sunyulster.genie.services;

import static com.mongodb.client.model.Filters.eq;
import static edu.sunyulster.genie.utils.Validator.exists;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.sunyulster.genie.db.DbConstants;
import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.User;
import edu.sunyulster.genie.utils.DataConverter;
import edu.sunyulster.genie.utils.VerifyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.ForbiddenException;

@ApplicationScoped
public class UserService {

    @Inject
    MongoDatabase db;

    @Inject 
    VerifyService verifier;

    public void create(User user, String authId) throws InvalidDataException, ForbiddenException {
        // - - - - - - - - - -  VALIDATION - - - - - - - - - - // 
        if (!exists(user.getFirstName()) || !exists(user.getLastName())) 
            throw new InvalidDataException("Name is not valid");

        if (!exists(user.getEmail())) 
            throw new InvalidDataException("Email is not valid");
    
        // check if user was already added : can occur for ex if a request with the JWT 
        //      coming from the auth-server with a role of auth-server is replayed
        MongoCollection<Document> users = db.getCollection(DbConstants.USERS);
        Document match = users.find(eq(DbConstants.EMAIL, user.getEmail())).first();
        if (match != null) 
            throw new ForbiddenException("user already exists"); // what error here??
    
        users.insertOne(DataConverter.userToDocument(user, authId));
    }

    public User get(String userId) throws AuthenticationException {
        Document user = verifier.verifyUser(userId);
        return DataConverter.documentToUser(user);
    }


    // public void delete(String userId) throws AuthenticationException {
    //     // delete wishlists, items, everything associated with user

    //     // remove user from db
    //     MongoCollection<Document> users = db.getCollection("users");
    //     DeleteResult result = users.deleteOne(eq("authId", new ObjectId(userId)));

    //     // remove wishlists and wishlist items
        
    //     // if user was already deleted
    //     if (result.getDeletedCount() == 0) 
    //         throw new AuthenticationException("User does not exist");
    
    // }

    
}
