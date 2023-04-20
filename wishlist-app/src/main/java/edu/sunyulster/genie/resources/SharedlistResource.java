package edu.sunyulster.genie.resources;

import java.util.List;

import org.eclipse.microprofile.jwt.Claim;

import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.models.Wishlist;
import edu.sunyulster.genie.services.SharedlistService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/sharedlists")
@RolesAllowed({"user"})
public class SharedlistResource {
    @Inject 
    SharedlistService service;

    @Inject 
    @Claim("sub")
    String userId;
    
    // query param behavior
    // 1. no param - true (default)
    // 2. invalid param - false
    // 3. true param - true
    // 4. false param - false
    @PATCH
    @Path("/{wishlistId}/items/{itemId}")
    public Response buyItem(@PathParam("wishlistId") String wishlistId, @PathParam("itemId") String itemId, @DefaultValue("true") @QueryParam("buy") boolean buy) throws AuthenticationException {
        Item item = service.buy(userId, wishlistId, itemId, buy);
        return Response.ok(item).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    public Response getSharedWishlists() throws AuthenticationException {
        List<Wishlist> sharedWishlist = service.getAll(userId);
        return Response.ok(sharedWishlist).type(MediaType.APPLICATION_JSON).build();
    }
    @DELETE
    @Path("/{id}")
    public Response leaveSharedWishlist(@PathParam("id") String id)throws AuthenticationException{
        Wishlist leftWishlist = service.leaveSharedWishlist(userID,id);
        return Response.ok(leftWishlist).type(MediaType.APPLICATION_JSON).build();
    }
}
