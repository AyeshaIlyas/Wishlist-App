package edu.sunyulster.genie.exceptions;

import edu.sunyulster.genie.models.AppError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidDataExceptionMapper implements ExceptionMapper<InvalidDataException> {

    @Override
    public Response toResponse(InvalidDataException exception) {
        return Response.status(Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new AppError(400, exception.getMessage()))
                    .build();
    }
    
}
