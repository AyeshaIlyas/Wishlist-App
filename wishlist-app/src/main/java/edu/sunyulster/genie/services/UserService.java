package edu.sunyulster.genie.services;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import edu.sunyulster.genie.logging.LogManager;
import edu.sunyulster.genie.logging.Logger;
import edu.sunyulster.genie.db.DbConstants;
import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.User;
import edu.sunyulster.genie.utils.DataConverter;
import edu.sunyulster.genie.utils.Validator;
import edu.sunyulster.genie.utils.VerifyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.ForbiddenException;

@ApplicationScoped
public class UserService {

    @Inject
    private MongoDatabase db;

    @Inject 
    private VerifyService verifier;

    public void create(User user, String authId) throws InvalidDataException, ForbiddenException {

        //LOGGING
        Logger logger = LogManager.addLog(
            "PUT",
            "void create",
            getClass().getName() + ":" + new Throwable().getStackTrace()[0].getLineNumber(),
            user.toString());
        //LOGGING

        // - - - - - - - - - -  VALIDATION - - - - - - - - - - // 
        if (!Validator.exists(user.getFirstName()) || !Validator.exists(user.getLastName())) 
            throw new InvalidDataException("Name is not valid");

        if (!Validator.isEmailValid(user.getEmail())) 
            throw new InvalidDataException("Email is not valid");
    
        // check if user was already added : can occur for ex if a request with the JWT 
        //      coming from the auth-server with a role of auth-server is replayed
        MongoCollection<Document> users = db.getCollection(DbConstants.USERS);
        Document match = users.find(Filters.eq(DbConstants.EMAIL, user.getEmail())).first();
        if (match != null) 
            throw new ForbiddenException("user already exists"); // what error here??
    
        users.insertOne(DataConverter.userToDocument(user, authId));

        //LOGGING
        logger.Success();
        //LOGGING
    }

    public User get(String userId) throws AuthenticationException {

        //LOGGING
        Logger logger = LogManager.addLog(
            "GET",
            "User get",
            getClass().getName() + ":" + new Throwable().getStackTrace()[0].getLineNumber(),
            "User Id "+userId);
        //LOGGING

        Document user = verifier.verifyUser(userId);

        //LOGGING
        logger.Success();
        //LOGGING
        
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
