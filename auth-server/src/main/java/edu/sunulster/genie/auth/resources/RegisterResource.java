package edu.sunulster.genie.auth.resources;

import com.mongodb.MongoException;

import edu.sunulster.genie.auth.models.AppError;
import edu.sunulster.genie.auth.models.User;
import edu.sunulster.genie.auth.services.RegisterService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@RequestScoped
@Path("/register")
public class RegisterResource {
    @Inject
    RegisterService registerService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(User user) {
        try {
            String userId = registerService.register(user);
            JsonObject content = Json.createObjectBuilder().add("userId", userId).build();
            return Response.ok().type(MediaType.APPLICATION_JSON)
                           .entity(content)
                           .build();
        } catch (MongoException e) {
            return Response.serverError().type(MediaType.APPLICATION_JSON)
                           .entity(new AppError(500, "Database operations failed"))
                           .build();
        } 
        // is an AuthenticationException appropriate here? what status code if failed to register bc info was not valid?
        catch (AuthenticationException e) {
            return Response.status(Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON)
                           .entity(new AppError(401, e.getMessage()))
                           .build();
        }
    }
    
}