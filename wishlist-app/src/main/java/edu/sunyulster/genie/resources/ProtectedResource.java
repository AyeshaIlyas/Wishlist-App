package edu.sunyulster.genie.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@RequestScoped
@Path("/protected")
public class ProtectedResource {

    @RolesAllowed({"user"})
    @GET
    public String getString() {
        return "hello world";
    }
    
}
