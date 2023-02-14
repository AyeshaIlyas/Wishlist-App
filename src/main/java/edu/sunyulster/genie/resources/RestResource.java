package edu.sunyulster.genie.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/resource")
public class RestResource {

    @GET
    public String hello() {
        return "Hello world, with Wendy's change!";
    }
    
}
