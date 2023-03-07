package edu.sunulster.genie.auth.services;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.ibm.websphere.security.jwt.Claims;
import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.JwtBuilder;
import com.ibm.websphere.security.jwt.JwtException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.sunulster.genie.auth.models.Credentials;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.ServerErrorException;


@ApplicationScoped
public class LoginService {

    @Inject
    MongoDatabase db;
    
    public String login(Credentials creds) throws AuthenticationException, JwtException {        
        try {
            MongoCollection<Document> users = db.getCollection("users");
            Document match = users.find(eq("email", creds.getEmail())).first();

            if (match == null) {
                throw new AuthenticationException("email no in db");
            } 

            if (!creds.getPassword().equals(match.get("password"))) {
                throw new AuthenticationException("passwords no not match");
            }

            String id = match.getObjectId("_id").toString();
            String jwt = buildJwt(creds, id, new String[] {"user"});
            System.out.println("JWT: " + jwt);
            return jwt;
        } catch (JwtException e) {
            throw new ServerErrorException("Could not build JWT", 500);
        }   
    }

    private String buildJwt(Credentials creds, String userId, String[] roles) throws JwtException {
        try {
            return JwtBuilder.create("jwtBuilderConfig")
                            .claim(Claims.SUBJECT, userId)
                            .claim("upn", userId)
                            .claim("groups", roles)
                            .claim("aud", "wishlist-app")
                            .buildJwt()
                            .compact();
        } catch (JwtException | InvalidClaimException | InvalidBuilderException e) {
            throw new JwtException("Error building JWT: " + e.getMessage());
        }
    }
    
}
