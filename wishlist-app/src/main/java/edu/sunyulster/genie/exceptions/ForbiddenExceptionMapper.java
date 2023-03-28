package edu.sunyulster.genie.exceptions;

import edu.sunyulster.genie.models.AppError;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    @Override
    public Response toResponse(ForbiddenException exception) {
        return Response.status(Status.FORBIDDEN)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new AppError(403, "Forbidden: " + exception.getMessage()))
                    .build();
    }
    
}
