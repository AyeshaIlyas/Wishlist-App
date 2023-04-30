package edu.sunyulster.genie.auth.exceptions;

import java.security.NoSuchAlgorithmException;

import edu.sunyulster.genie.auth.models.AppError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NoSuchAlgorithmExceptionMapper implements ExceptionMapper<NoSuchAlgorithmException> {

    @Override
    public Response toResponse(NoSuchAlgorithmException exception) {
        return Response.serverError().type(MediaType.APPLICATION_JSON)
                    .entity(new AppError(500, "Hash function SHA-256 not supported"))
                    .build();
    }
    
}
