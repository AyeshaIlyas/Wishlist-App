package edu.sunyulster.genie.auth.resources;

import java.util.concurrent.TimeUnit;

import org.bson.Document;

import com.ibm.websphere.security.jwt.JwtException;

import edu.sunyulster.genie.auth.models.Credentials;
import edu.sunyulster.genie.auth.services.LoginService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;


@RequestScoped
@Path("/login")
public class LoginResource {
    @Inject 
    LoginService loginService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    // JwtException handled by JwtExceptionMapper, AuthenticationException handled by AuthenticationExceptionMapper
    public Response login(Credentials creds) throws JwtException, AuthenticationException {
        Document userDetails = loginService.verifyUser(creds);
        String jwt = loginService.createJwt(userDetails, "_id", "roles");
        NewCookie cookie = loginService.getNewCookie(userDetails, TimeUnit.DAYS, 7); // generate cookie that is valid for 1 week
        JsonObject content = Json.createObjectBuilder().add("token", jwt).build();
        return Response.ok().type(MediaType.APPLICATION_JSON)
                .cookie(cookie)
                .entity(content)
                .build();
    }

    @GET
    @Path("/refresh")
    // JwtException handled by JwtExceptionMapper, AuthenticationException handled by AuthenticationExceptionMapper
    public Response refresh(@CookieParam("auth-session") Cookie cookie) throws JwtException, AuthenticationException {
        if (cookie == null) 
            throw new AuthenticationException("Cookie does not exist");
        String jwt = loginService.getRefreshToken(cookie);
        JsonObject content = Json.createObjectBuilder().add("token", jwt).build();
        return Response.ok().type(MediaType.APPLICATION_JSON)
                .entity(content)
                .build();
    }
    
}