package edu.sunulster.genie.auth.db.exceptions;

import edu.sunulster.genie.auth.models.AppError;
import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidJsonExceptionMapper implements ExceptionMapper<JsonbException> {

    @Override
    public Response toResponse(JsonbException exception) {
        
        return Response.status(Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new AppError(400, "Invalid Json data"))
                    .build();
    }
    
}
