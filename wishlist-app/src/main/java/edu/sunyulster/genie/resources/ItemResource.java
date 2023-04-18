package edu.sunyulster.genie.resources;

import java.util.List;

import org.eclipse.microprofile.jwt.Claim;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.services.ItemService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@RolesAllowed({"user"})
public class ItemResource {
    
    @Inject
    @Claim("sub")
    private String userId;

    @Inject
    private ItemService itemService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createItem(Item item, @PathParam("wishlistId") String wishlistId) throws InvalidDataException, ForbiddenException, AuthenticationException {
        Item newItem = itemService.create(userId, wishlistId, item);
        return Response.ok()
            .entity(newItem)
            .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems(@PathParam("wishlistId") String wishlistId, @DefaultValue("true") @QueryParam("isOwner") boolean isOwner) throws AuthenticationException {
        System.out.println("IsOwner: " + isOwner);
        List<Item> items = itemService.getAll(userId, wishlistId, isOwner);
        return Response.ok()
            .entity(items)
            .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateItem(Item newItem, @PathParam("wishlistId") String wishlistId, @PathParam("id") String id) throws AuthenticationException, InvalidDataException {
        newItem.setId(id);
        Item updatedItem = itemService.update(userId, wishlistId, newItem);
        return Response.ok()
            .entity(updatedItem)
            .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("wishlistId") String wishlistId, @PathParam("id") String id) throws AuthenticationException {
        itemService.delete(userId, wishlistId, id);
        return Response.noContent().build();
    }

}