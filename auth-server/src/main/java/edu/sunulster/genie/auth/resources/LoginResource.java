package edu.sunulster.genie.auth.resources;

import com.ibm.websphere.security.jwt.JwtException;

import edu.sunulster.genie.auth.models.Credentials;
import edu.sunulster.genie.auth.services.LoginService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;


@RequestScoped
@Path("/login")
public class LoginResource {
    @Inject 
    LoginService loginService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(Credentials creds) {
        try {
            String jwt = loginService.login(creds);
            return "Logged in. Here is your JWT: " + jwt;
        } catch (JwtException e) {
            return "JWT EXCEPTION";
        } catch (AuthenticationException e) {
            return "AUTH EXCEPTION";
        }
    }

}