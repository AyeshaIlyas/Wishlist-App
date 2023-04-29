package edu.sunyulster.genie.auth.services;

import static com.mongodb.client.model.Filters.eq;
import static edu.sunyulster.genie.auth.utils.AuthUtils.createCookie;
import static edu.sunyulster.genie.auth.utils.Validator.areRolesValid;
import static edu.sunyulster.genie.auth.utils.Validator.isEmailValid;
import static edu.sunyulster.genie.auth.utils.Validator.isPasswordValid;

import java.util.Arrays;
import java.util.Date;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.ibm.websphere.security.jwt.JwtException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import edu.sunyulster.genie.auth.exceptions.RegistrationException;
import edu.sunyulster.genie.auth.models.User;
import edu.sunyulster.genie.auth.utils.AuthUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;


@ApplicationScoped
public class RegisterService {

    @Inject
    MongoDatabase db;

    public String register(User user) throws RegistrationException, JwtException {
        // - - - - - - - - - -  VALIDATION - - - - - - - - - - // 
        if (!areRolesValid(user.getRoles()))
            throw new RegistrationException("At least one role must be specified");

        if (!isEmailValid(user.getEmail())) 
            throw new RegistrationException("Email is not valid");

        // check if email is unique
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(eq("email", user.getEmail())).first();
        if (match != null) 
            throw new RegistrationException("email already exists");

        if (!isPasswordValid(user.getPassword())) 
            throw new RegistrationException("Password is not valid");

        InsertOneResult result = users.insertOne(convertUserToDocument(user));
        String id = result.getInsertedId().asObjectId().getValue().toString();
        return AuthUtils.buildJwt(id, new String[] {"auth-server"});
    }

    // for use if user is registered, but there is an error creating the JWT.
    // therefore, the user should be removed from the db so that a registration can be reattempted
    public void removeUserByEmail(User user) {
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(eq("email", user.getEmail())).first();
        if (match != null) 
            users.deleteOne(eq("email", user.getEmail()));
    }

    public NewCookie deregister(Cookie cookie) throws AuthenticationException {        
        Document session = getSession(cookie);
        if (session == null) 
            throw new AuthenticationException("Session either does not exists or session is expired");

        // delete all session for the user
        MongoCollection<Document> sessions = db.getCollection("sessions");
        ObjectId userId = new ObjectId(session.get("userId").toString());
        sessions.deleteMany(eq("userId", userId));

        // delete user
        MongoCollection<Document> users = db.getCollection("users");
        users.deleteOne(eq("_id", userId));
        return createCookie("expiredCookie", 0, new Date());
    }

    private Document convertUserToDocument(User user) {
        Document userDoc = new Document()
            .append("_id", new ObjectId())
            .append("email", user.getEmail())
            .append("password", user.getPassword())
            .append("roles", Arrays.asList(user.getRoles()));
        return userDoc;
    }

    // Duplicated in RegisterService and LoginService
    private Document getSession(Cookie cookie) {
        // check if session exists (user is logged in)
        String sessionId = cookie.getValue();
        MongoCollection<Document> sessions = db.getCollection("sessions");
        Document sessionMatch = sessions.find(eq("_id", new ObjectId(sessionId))).first();
        if (sessionMatch == null)
            return null; // session does not exist
     
        // check if session expired 
        Date sessionExpiration = (Date) sessionMatch.get("expiration");
        Date currentDate = new Date();
        if (currentDate.compareTo(sessionExpiration) >= 0) 
            return null; // session expired; login required
        
        return sessionMatch;
    }
    
}
