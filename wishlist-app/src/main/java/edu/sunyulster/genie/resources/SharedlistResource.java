package edu.sunyulster.genie.resources;

import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.services.SharedlistService;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("sharedlists")
// @RolesAllowed({"user"})
public class SharedlistResource {
    @Inject 
    SharedlistService service;
    
    // query param behavior
    // 1. no param - true (default)
    // 2. invalid param - false
    // 3. true param - true
    // 4. false param - false
    @PATCH
    @Path("/{wishlistId}/items/{itemId}")
    public Response buyItem(@PathParam("wishlistId") String wishlistId, @PathParam("itemId") String itemId, @DefaultValue("true") @QueryParam("buy") boolean buy, @QueryParam("user") String user) throws AuthenticationException {
        Item item = service.buy(user, wishlistId, itemId, buy);
        return Response.ok(item).type(MediaType.APPLICATION_JSON).build();
    }
}
