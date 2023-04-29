package edu.sunyulster.genie.auth.resources;


import java.security.NoSuchAlgorithmException;

import com.ibm.websphere.security.jwt.JwtException;

import edu.sunyulster.genie.auth.exceptions.RegistrationException;
import edu.sunyulster.genie.auth.models.AppError;
import edu.sunyulster.genie.auth.models.User;
import edu.sunyulster.genie.auth.services.RegisterService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@RequestScoped
@Path("/register")
public class RegisterResource {
    @Inject
    RegisterService registerService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    // JwtException handled by JwtExceptionMapper
    public Response register(User user) throws JwtException, NoSuchAlgorithmException {
        try {
            String jwt = registerService.register(user);
            JsonObject content = Json.createObjectBuilder().add("token", jwt).build();
            return Response.ok().type(MediaType.APPLICATION_JSON)
                    .entity(content)
                    .build();
        } catch (RegistrationException e) { // custom exception
            return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON)
                    .entity(new AppError(400, e.getMessage()))
                    .build();
        }
    }

    @DELETE
    public Response deleteAccount(@CookieParam("auth-session") Cookie cookie) throws AuthenticationException {
        // user cannot delete account if they are not logged in 
        if (cookie == null) 
            throw new AuthenticationException("Cookie does not exist");
        // delete user details and all session for that user if logged in 
        NewCookie expiredCookie = registerService.deregister(cookie);
        return Response.noContent()
                .cookie(expiredCookie)
                .build();

    }
    
}