package edu.sunyulster.genie.auth.services;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lte;
import static edu.sunyulster.genie.auth.utils.AuthUtils.createCookie;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.ibm.websphere.security.jwt.JwtException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.sunyulster.genie.auth.models.Credentials;
import edu.sunyulster.genie.auth.utils.AuthUtils;
import edu.sunyulster.genie.auth.utils.Validator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.NewCookie;


@ApplicationScoped
public class LoginService {

    @Inject
    MongoDatabase db;
    
    public Document verifyUser(Credentials creds) throws AuthenticationException, NoSuchAlgorithmException { 
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(eq("email", creds.getEmail())).first();

        if (match == null) 
            throw new AuthenticationException("email not in db");

        // verify password
        String pHash = match.getString("password");
        System.out.println(pHash);
        if (!(Validator.isPasswordValid(creds.getPassword()) && AuthUtils.verifyPassword(creds.getPassword(), pHash))) 
            throw new AuthenticationException("passwords no not match");

        return match;
    }

    public String createJwt(Document doc, String userId, String roleKey) throws JwtException {
        String id = doc.getObjectId(userId).toString();
        // get user roles
        List<String> rolesList = doc.getList(roleKey, String.class);
        String[] roles = new String[rolesList.size()];
        for (int i = 0; i < roles.length; i++) {
            roles[i] = rolesList.get(i);
        }
        return AuthUtils.buildJwt(id, roles);
    }

    public NewCookie getNewCookie(Document user, TimeUnit unit, int timePeriod) {
        // session and cookie time-to-live and expiration 
        int ttl = (int) unit.toSeconds(timePeriod);
        Date expirationDate =  Date.from(Instant.now().plusSeconds(ttl));
        // delete previous session for this user
        deleteExpiredSessions((ObjectId) user.get("_id"));
        String sessionId = createSession(user, expirationDate);
        return createCookie(sessionId, ttl, expirationDate);
   }

    public String getRefreshToken(Cookie cookie) throws AuthenticationException, JwtException {
        Document session = getSession(cookie);
        if (session == null) 
            throw new AuthenticationException("Session either does not exists or session is expired");
        return createJwt(session, "userId", "roles");
    }

    private String createSession(Document user, Date expiration) {
         // create session for user
         MongoCollection<Document> sessions = db.getCollection("sessions");
         ObjectId sessionId = new ObjectId();
         Document session = new Document()
                 .append("_id", sessionId)
                 .append("userId", user.get("_id"))
                 .append("roles", user.get("roles"))
                 .append("expiration", expiration);
         sessions.insertOne(session);
         return sessionId.toString();
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

    private void deleteExpiredSessions(ObjectId userId) {
        // delete all sessions for the userId
        MongoCollection<Document> sessions = db.getCollection("sessions");
        Bson filter = and(eq("userId", userId), lte("expiration", new Date()));
        sessions.deleteMany(filter);
   }
    
}
