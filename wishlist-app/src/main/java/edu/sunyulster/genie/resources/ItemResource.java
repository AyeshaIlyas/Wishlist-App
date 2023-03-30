package edu.sunyulster.genie.resources;

import java.util.List;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.AppError;
import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.services.ItemService;
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
@Path("/items")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
// @RolesAllowed({"user"})
public class ItemResource {
    
    // @Inject
    // @Claim("sub")
    // private String userId;

    @Inject
    private ItemService itemService;

    @QueryParam("id")
    private String userId;
    
    @POST
    public Response createItem(Item item) throws InvalidDataException {
        Item newItem = itemService.create(userId, item);
        return Response.ok()
            .entity(newItem)
            .build();
    }

    @GET
    public Response getItems(@PathParam("id") String id) throws AuthenticationException {
        List<Item> items = itemService.getAll(id);
        return Response.ok()
            .entity(items)
            .build();
    }

    @GET
    @Path("/{id}")
    public Response getItem(@PathParam("id") String id) throws AuthenticationException {
        Item item = itemService.get(userId, id);
        return Response.ok()
            .entity(item)
            .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") String id) {
        itemService.delete(userId, id);
        return Response.noContent().build();
    }

}