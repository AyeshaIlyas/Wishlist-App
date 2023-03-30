package edu.sunyulster.genie.resources;

import java.util.List;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.AppError;
import edu.sunyulster.genie.models.Wishlist;
import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.services.WishlistService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

@RequestScoped
@Path("/wishlists")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
// @RolesAllowed({"user"})
public class WishlistResource {
    
    // @Inject
    // @Claim("sub")
    // private String userId;

    @Inject
    private WishlistService wishlistService;

    @QueryParam("id")
    private String userId;
    
    @POST
    public Response createWishlist(Wishlist wishlist) throws InvalidDataException {
        Wishlist newWishlist = wishlistService.create(userId, wishlist);
        return Response.ok()
            .entity(newWishlist)
            .build();
    }

    @GET
    public Response getWishlists() throws AuthenticationException {
        List<Wishlist> wishlists = wishlistService.getAll(userId);
        return Response.ok()
            .entity(wishlists)
            .build();
    }

    @GET
    @Path("/{id}")
    public Response getWishlist(@PathParam("id") String id) throws AuthenticationException {
        Wishlist wishlist = wishlistService.get(userId, id);
        return Response.ok()
            .entity(wishlist)
            .build();
    }

    @PUT
    @Path("/{id}")
    // why doest Wishlist wishlist, @PathParam("id") String id work
    // ERROR: Resource methods cannot have more than one entity parameterjakarta-jax_rs(ResourceMethodMultipleEntityParams)
    public Response updateWishlist(Wishlist wishlist, @Context UriInfo uriInfo) throws InvalidDataException {
        List<String> ids = uriInfo.getPathParameters().get("id");
        if (ids == null)
            return Response.status(Status.BAD_REQUEST)
                .entity(new AppError(400, "Must include wishlist id in request as path param"))
                .build();
        
        wishlist.setId(ids.get(0));
        Wishlist updatedWishlist = wishlistService.update(userId, wishlist);
        return Response.ok()
            .entity(updatedWishlist)
            .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteWishlist(@PathParam("id") String id) {
        wishlistService.delete(userId, id);
        return Response.noContent().build();
    }
}
