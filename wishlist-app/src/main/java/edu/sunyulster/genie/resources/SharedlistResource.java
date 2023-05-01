package edu.sunyulster.genie.resources;

import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.microprofile.jwt.Claim;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.models.Wishlist;
import edu.sunyulster.genie.services.SharedlistService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/shared-wishlists")
@RolesAllowed({"user"})
public class SharedlistResource {
    @Inject 
    SharedlistService service;

    @Inject 
    @Claim("sub")
    String userId;

    // move into /wishlists?isOwner=false ??
    @GET
    public List<Wishlist> getSharedWishlists() throws AuthenticationException {
        return service.getAll(userId);
    }

    // move into /wishlists?isOwner=false ??
    @DELETE
    @Path("/{wishlistId}")
    public Response leaveSharedWishlist(@PathParam("wishlistId") String wishlistId) throws AuthenticationException, NoSuchElementException, InvalidDataException {
        service.leaveSharedWishlist(userId, wishlistId);
        return Response.noContent().type(MediaType.APPLICATION_JSON).build();
    }

    // query param behavior
    // 1. no param - true (default)
    // 2. invalid param - false
    // 3. true param - true
    // 4. false param - false
    @PATCH
    @Path("/{wishlistId}/items/{itemId}")
    public Item buyItem(
        @PathParam("wishlistId") String wishlistId, 
        @PathParam("itemId") String itemId,
        @DefaultValue("true") @QueryParam("buy") boolean buy) 
        throws AuthenticationException, NoSuchElementException, InvalidDataException {

        return service.buy(userId, wishlistId, itemId, buy);
    }
}
