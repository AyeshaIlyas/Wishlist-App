package edu.sunyulster.genie.services;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static edu.sunyulster.genie.utils.Validator.isItemValid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.Item;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.ForbiddenException;


@ApplicationScoped
public class ItemService {
    @Inject
    MongoDatabase db;

    public Item create(String userId, String wishlistId, Item i) throws ForbiddenException, InvalidDataException, AuthenticationException{
        ObjectId wId = new ObjectId(wishlistId);
        checkWishlistOwnsership(new ObjectId(userId), wId);

        // validate information
        if (!isItemValid(i)) 
            throw new InvalidDataException("Item data is not valid. Item must have a name and non empty fields.");

        // create item
        ObjectId itemId = new ObjectId();
        Document newItem = new Document()
            .append("_id", itemId)
            .append("name", i.getName())
            .append("gifter", null)
            .append("dateCreated", new Date());

        // price is optional data
        if (i.getPrice() != null) {
            if (i.getPrice() < 0)
                throw new InvalidDataException("Price must be >= 0"); 
            else 
                newItem.append("price", i.getPrice());
        }

        // supplier is optional data
        if (i.getSupplier() != null && !i.getSupplier().isEmpty())
            newItem.append("supplier", i.getSupplier());

        
        // add to items collection
        MongoCollection<Document> items = db.getCollection("items");
        items.insertOne(newItem);

        // add item id to wishlist
        MongoCollection<Document> wishlists = db.getCollection("wishlists");
        Bson update = Updates.addToSet("items", itemId);
        wishlists.updateOne(eq("_id", wId), update);

        return documentToItem(newItem);
    }

    public List<Item> getAll(String userId, String wishlistId, boolean isOwner) throws ForbiddenException, AuthenticationException {
        ObjectId wId = new ObjectId(wishlistId);
        if (isOwner)
            checkWishlistOwnsership(new ObjectId(userId), wId);
        else {
            // see if the user has been added to the wishlist
            MongoCollection<Document> users = db.getCollection("users");
            Document match = users.find(Filters.and(eq("authId", new ObjectId(userId)), in("sharedWishlists", new ObjectId(wishlistId)))).first();
            System.out.println(match);
            if (match == null)
                throw new ForbiddenException("User has not been added to this wishlist");
        }

        // get all items for the user 
        MongoCollection<Document> wishlists = db.getCollection("wishlists");
        Document match = wishlists.find(eq("_id", wId)).first();
        // occurs if user is deleted but still has valid jwt for example
        // or if they specified the wrong object id in the request
        if (match == null)
            throw new AuthenticationException("Wishlist does not exist!");

        List<ObjectId> itemIds = (ArrayList<ObjectId>) match.get("items");
        MongoCollection<Document> itemCol = db.getCollection("items");
        FindIterable<Document> matchingItems = itemCol.find(in("_id", itemIds))
            .sort(Sorts.ascending("dateCreated"));

        List<Item> items = new ArrayList<>(itemIds.size());
        for (Document i : matchingItems)
            items.add(documentToItem(i));
        
        return items;
    }

    public Item update(String userId, String wishlistId, Item newItem) throws InvalidDataException, AuthenticationException{
        ObjectId itemId = new ObjectId(newItem.getId());
        Document match = checkItemOwnsership(new ObjectId(userId), new ObjectId(wishlistId), itemId);
        
         // validate information
         if (!isItemValid(newItem)) 
            throw new InvalidDataException("Item must have a name and non empty fields");

        // get updated info
        Document updatedItem = new Document()
            .append("dateCreated", match.getDate("dateCreated"))
            .append("gifter", match.getString("gifter"))
            .append("name", newItem.getName());

        // price is optional data
        if (newItem.getPrice() != null) {
            if (newItem.getPrice() < 0)
                throw new InvalidDataException("Price must be >= 0"); 
            else 
                updatedItem.append("price", newItem.getPrice());
        }

        if (newItem.getSupplier() != null && !newItem.getSupplier().isEmpty()) {
            updatedItem.append("supplier", newItem.getSupplier());
        }

        MongoCollection<Document> items = db.getCollection("items");
        items.replaceOne(eq("_id", itemId), updatedItem);
        
        return documentToItem(items.find(eq("_id", itemId)).first());
    }

    public void delete(String userId, String wishlistId, String id) throws AuthenticationException {
        ObjectId itemId = new ObjectId(id);
        ObjectId wId = new ObjectId(wishlistId);
        // item exists and belongs to user
        checkItemOwnsership(new ObjectId(userId), wId, itemId);

        // remove item
        MongoCollection<Document> items = db.getCollection("items");
        items.deleteOne(eq("_id", itemId));
        // remove item id from wishlist
        MongoCollection<Document> wishlists = db.getCollection("wishlists");
        wishlists.updateOne(eq("_id", wId), Updates.pull("items", itemId));
    }

    private Item documentToItem(Document d) {
        return new Item(
            d.getObjectId("_id").toString(),
            d.getString("name"),
            d.getDouble("price"),
            d.getString("supplier"),
            d.getDate("dateCreated"),
            d.getString("gifter"));
    }

    private Document checkItemOwnsership(ObjectId userId, ObjectId wishlistId, ObjectId itemId) throws AuthenticationException {
        checkWishlistOwnsership(userId, wishlistId);
        MongoCollection<Document> wishlists = db.getCollection("wishlists");
        Document match = wishlists.find(eq("_id", wishlistId)).first();
        List<ObjectId> itemIds = (ArrayList<ObjectId>) match.get("items");
        for (ObjectId i : itemIds) {
            if (i.equals(itemId)) {
                MongoCollection<Document> items = db.getCollection("items");
                return items.find(eq("_id", itemId)).first();
            }
        }
        throw new NoSuchElementException("Item does not belong to requesting user");
    }

    private Document checkWishlistOwnsership(ObjectId userId, ObjectId wishlistId) throws AuthenticationException {
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(eq("authId", userId)).first();
        if (match == null) 
            throw new AuthenticationException("user does not exist");
        
        List<ObjectId> wishlistIds = (ArrayList<ObjectId>) match.get("wishlists");
        for (ObjectId i : wishlistIds) {
            if (i.equals(wishlistId))
                return match;
        }
        throw new ForbiddenException("Wishlist does not belong to requesting user");
    }
}
