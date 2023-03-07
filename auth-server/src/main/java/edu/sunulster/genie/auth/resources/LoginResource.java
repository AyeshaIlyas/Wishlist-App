package edu.sunulster.genie.auth.resources;


import com.ibm.websphere.security.jwt.JwtException;

import edu.sunulster.genie.auth.models.AppError;
import edu.sunulster.genie.auth.models.Credentials;
import edu.sunulster.genie.auth.services.LoginService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@RequestScoped
@Path("/login")
public class LoginResource {
    @Inject 
    LoginService loginService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(Credentials creds) {
        try {
            String jwt = loginService.login(creds);
            JsonObject content = Json.createObjectBuilder().add("jwt", jwt).build();
            return Response.ok().type(MediaType.APPLICATION_JSON)
                           .entity(content)
                           .build();
        } catch (JwtException e) {
            return Response.serverError().type(MediaType.APPLICATION_JSON)
                           .entity(new AppError(500, "Unable to create authentication token"))
                           .build();
        } catch (AuthenticationException e) {
            return Response.status(Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON)
                           .entity(new AppError(401, e.getMessage()))
                           .build();
        }
    }

}