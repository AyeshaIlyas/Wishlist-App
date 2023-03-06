package edu.sunulster.genie.auth.resources;

import org.bson.BsonValue;

import com.mongodb.MongoException;

import edu.sunulster.genie.auth.models.User;
import edu.sunulster.genie.auth.services.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@RequestScoped
@Path("/register")
public class RegisterResource {
    @Inject
    UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String register(User user) {
        try {
            BsonValue result = userService.register(user);
            return result.toString();
        } catch (AuthenticationException e) {
            System.out.println(e);
            return "Invalid registration information: " + e.getMessage();
        } catch (MongoException e) {
            System.out.println(e);
            return "Database error " + e.getMessage();
        }
    }
    
}