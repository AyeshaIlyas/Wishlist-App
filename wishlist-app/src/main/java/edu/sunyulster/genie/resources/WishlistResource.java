package edu.sunyulster.genie.resources;

import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.microprofile.jwt.Claim;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.Wishlist;
import edu.sunyulster.genie.services.WishlistService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@RequestScoped
@Path("/wishlists")
@RolesAllowed({"user"})
public class WishlistResource {
    
    @Inject
    @Claim("sub")
    private String userId;

    @Inject
    private WishlistService wishlistService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Wishlist createWishlist(Wishlist wishlist) throws InvalidDataException, AuthenticationException {
        return wishlistService.create(userId, wishlist);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Wishlist> getWishlists() throws AuthenticationException {
        return wishlistService.getAll(userId);
    }

    // get wishlist which user owns or which was shared with the user
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Wishlist getWishlist(
        @PathParam("id") String id, 
        @DefaultValue("true") @QueryParam("isOwner") boolean isOwner) 
        throws AuthenticationException, NoSuchElementException {

        return wishlistService.get(userId, id, isOwner);
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Wishlist updateWishlist(@PathParam("id") String id, Wishlist wishlist) throws InvalidDataException, AuthenticationException {
        wishlist.setId(id);
        return wishlistService.update(userId, wishlist);
    }

    @DELETE
    @Path("/{id}")
    public void deleteWishlist(@PathParam("id") String id) throws AuthenticationException, NoSuchElementException {
        wishlistService.delete(userId, id);
    }

    @Path("/{wishlistId}/items")
    public Class<ItemResource> getItemResource() {
        return ItemResource.class;
    }

    @DELETE
    @Path("/{wishlistId}/shared-with/{email}")
    public void deleteUserFromWishlist ( 
            @PathParam("wishlistId") String wishlistId,
            @PathParam("email") String userEmail) 
            throws AuthenticationException, NoSuchElementException {

        wishlistService.unshareWishlist(userEmail, wishlistId, userId);
    }

}
