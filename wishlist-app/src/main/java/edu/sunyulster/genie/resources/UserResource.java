package edu.sunyulster.genie.resources;

import org.eclipse.microprofile.jwt.Claim;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.User;
import edu.sunyulster.genie.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonString;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RequestScoped
@Path("/user")
public class UserResource {

    @Inject
    private UserService userService;

    @Inject
    @Claim("sub") 
    private JsonString userId;

    @RolesAllowed({"auth-server"})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createUser(User user) throws InvalidDataException, ForbiddenException {
        userService.create(user, userId.getString());      
    }

    @RolesAllowed({"user"})
    @GET
    @Produces(MediaType.APPLICATION_JSON) 
    public User getUser() throws AuthenticationException {
        return userService.get(userId.getString());
    }

    // @RolesAllowed({"user"})
    // @DELETE
    // public void deleteUser() throws AuthenticationException {
    //     userService.delete(userId.getString());
    // }    
}
