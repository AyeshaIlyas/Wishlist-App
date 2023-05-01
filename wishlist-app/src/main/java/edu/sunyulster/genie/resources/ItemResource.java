package edu.sunyulster.genie.resources;

import java.util.List;
import java.util.NoSuchElementException;

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
    public Item createItem(Item item, @PathParam("wishlistId") String wishlistId) throws InvalidDataException, ForbiddenException, AuthenticationException {
        return itemService.create(userId, wishlistId, item);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> getItems(
        @PathParam("wishlistId") String wishlistId, 
        @DefaultValue("true") @QueryParam("isOwner") boolean isOwner) 
        throws AuthenticationException, ForbiddenException, NoSuchElementException, InvalidDataException {

        return itemService.getAll(userId, wishlistId, isOwner);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Item updateItem(
        Item newItem, 
        @PathParam("wishlistId") String wishlistId, 
        @PathParam("id") String id) 
        throws AuthenticationException, InvalidDataException {

        newItem.setId(id);
        return itemService.update(userId, wishlistId, newItem);
    }

    @DELETE
    @Path("/{id}")
    public void deleteItem(
        @PathParam("wishlistId") String wishlistId,
        @PathParam("id") String id) 
        throws AuthenticationException, NoSuchElementException, InvalidDataException {
            
        itemService.delete(userId, wishlistId, id);
    }

}