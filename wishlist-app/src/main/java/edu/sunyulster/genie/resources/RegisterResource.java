package edu.sunyulster.genie.resources;

import edu.sunyulster.genie.models.AppError;
import edu.sunyulster.genie.models.User;
import edu.sunyulster.genie.services.RegisterService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/register")
public class RegisterResource {
    private RegisterService registerService;

    public RegisterResource() {
        registerService = new RegisterService();
    }    

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public Response register(User user){
        // needs modification 
        try {
            return Response.ok()
            .entity(registerService.register(user))
            .build();
        } catch (IllegalArgumentException e) {
            return Response.status(400)
            .entity(new AppError(400, e.getMessage()))
            .build();
        }
       
   }


}