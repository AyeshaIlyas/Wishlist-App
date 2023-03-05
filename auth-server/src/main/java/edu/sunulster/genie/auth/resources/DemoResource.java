package edu.sunulster.genie.auth.resources;

import edu.sunulster.genie.auth.services.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;


@RequestScoped
@Path("/login")
public class DemoResource {
    @Inject 
    UserService s;

    @GET
    public String login() {
        s.getUser();
        return "you are logged in :)";
    }
}