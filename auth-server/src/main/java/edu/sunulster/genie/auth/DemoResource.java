package edu.sunulster.genie.auth;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/login")
public class DemoResource {
    @GET
    public String login() {
        return "you are logged in :)";
    }
}