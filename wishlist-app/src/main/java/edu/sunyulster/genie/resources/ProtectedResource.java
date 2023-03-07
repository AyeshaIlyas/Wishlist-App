package edu.sunyulster.genie.resources;

import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@RequestScoped
@Path("/protected")
public class ProtectedResource {

    @Inject
    JsonWebToken token;

    @RolesAllowed({"user"})
    @GET
    public String getString() {
        return "You are accessible a protected resource\nJWT userID: " + token.getSubject();
    }
    
}
