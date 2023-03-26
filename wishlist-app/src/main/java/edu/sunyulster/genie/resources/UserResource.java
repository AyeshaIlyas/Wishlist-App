package edu.sunyulster.genie.resources;

import org.eclipse.microprofile.jwt.Claim;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.AppError;
import edu.sunyulster.genie.models.User;
import edu.sunyulster.genie.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonString;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
    public Response createUser(User user) {
        try {
            userService.create(user, userId.getString());
            return Response.noContent().build();
        } catch (InvalidDataException e) {
            return Response.status(Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new AppError(400, e.getMessage()))
                .build();
        } catch (IllegalArgumentException e) { // user already added
            return Response.status(Status.CONFLICT) // 409 - the request conflicts with the current state of the server
                .type(MediaType.APPLICATION_JSON)
                .entity(new AppError(409, e.getMessage()))
                .build();
        }
    }

    @RolesAllowed({"user"})
    @GET
    public Response getUser() throws AuthenticationException {
        User user = userService.get(userId.getString());
        return Response.ok()
            .type(MediaType.APPLICATION_JSON)
            .entity(user)
            .build();
    }

    @RolesAllowed({"user"})
    @DELETE
    public Response deleteUser() throws AuthenticationException {
        userService.delete(userId.getString());
        return Response.noContent().build();
    }    
}
