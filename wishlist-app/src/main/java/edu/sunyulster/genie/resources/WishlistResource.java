package edu.sunyulster.genie.resources;

import java.util.List;

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
import jakarta.ws.rs.core.Response;

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
    public Response createWishlist(Wishlist wishlist) throws InvalidDataException, AuthenticationException {
        Wishlist newWishlist = wishlistService.create(userId, wishlist);
        return Response.ok()
            .entity(newWishlist)
            .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWishlists() throws AuthenticationException {
        List<Wishlist> wishlists = wishlistService.getAll(userId);
        return Response.ok()
            .entity(wishlists)
            .build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWishlist(@PathParam("id") String id, @DefaultValue("true") @QueryParam("isOwner") boolean isOwner) throws AuthenticationException {
        Wishlist wishlist = wishlistService.get(userId, id, isOwner);
        System.out.println(wishlist);
        return Response.ok()
            .entity(wishlist)
            .build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWishlist(@PathParam("id") String id, Wishlist wishlist) throws InvalidDataException, AuthenticationException {
        wishlist.setId(id);
        Wishlist updatedWishlist = wishlistService.update(userId, wishlist);
        return Response.ok()
            .entity(updatedWishlist)
            .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteWishlist(@PathParam("id") String id) {
        System.out.println("IDDD: " + id);
        wishlistService.delete(userId, id);
        return Response.noContent().build();
    }

    @Path("/{wishlistId}/items")
    public Class<ItemResource> getItemResource() {
        return ItemResource.class;
    }

    @DELETE
    @Path("/{wishlistId}/sharedwith/{email}")
    public Response deleteUser( 
            @PathParam("wishlistId") String wishlistId,
            @PathParam("email") String userEmail) throws InvalidDataException{
        wishlistService.unshareWishlist(userEmail, wishlistId, userId);
        return Response.noContent().build();
    }

}
