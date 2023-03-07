package edu.sunulster.genie.auth.exceptions;

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
    public Response toResponse(JsonbException e) {
        return Response.status(Status.BAD_REQUEST)
                       .type(MediaType.APPLICATION_JSON)
                       .entity(new AppError(Status.BAD_REQUEST.getStatusCode(), "Json data does not conform to expected values OR Json is invalid"))
                       .build();
    }
}
