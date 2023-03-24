package edu.sunyulster.genie.resources;

import java.util.List;

import edu.sunyulster.genie.models.User;
import edu.sunyulster.genie.services.LoginService;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/login")
public class LoginResource {
    private LoginService loginService;

    public LoginResource() {
        loginService = new LoginService();
    }


    @POST
    public String login(@FormParam("email") String email, @FormParam("password") String password) {
        // throws NullPointerException if either email or password are not specififed in request payload
        // should return a Response obj
        //      success - return Response with JWT
        //      failure - return AppError with e.getMessage()
        try {
            String jwt_token = loginService.login(email, password);
            return jwt_token;
            //return "<html> <body> <p> hi this is sommat</p> </body> </html>";
        } catch (AuthenticationException e) {
            return e.getMessage();
        }  
    }

    // NOTE : Temporary route for debugging purposes only
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    public List<User> users() {
        return loginService.getUsers();
    }

    
}
