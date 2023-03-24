package edu.sunyulster.genie.auth.exceptions;

import com.ibm.websphere.security.jwt.JwtException;

import edu.sunyulster.genie.auth.models.AppError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JwtExceptionMapper implements ExceptionMapper<JwtException> {

    @Override
    public Response toResponse(JwtException exception) {
        return Response.serverError().type(MediaType.APPLICATION_JSON)
                    .entity(new AppError(500, "Unable to create authentication token"))
                    .build();
    }
    
}
