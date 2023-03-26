package edu.sunyulster.genie.exceptions;

import com.mongodb.MongoException;

import edu.sunyulster.genie.models.AppError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MongoExceptionMapper implements ExceptionMapper<MongoException> {

    @Override
    public Response toResponse(MongoException exception) {
        return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new AppError(500, String.format("Mongo Exception : %s", exception.getMessage())))
                    .build();
    }
    
}
