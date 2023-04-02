package edu.sunyulster.genie.services;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static edu.sunyulster.genie.utils.Validator.isWishlistValid;

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
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.Item;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.ForbiddenException;


@ApplicationScoped
public class ItemService extends MongoService {

    public Item create(String userId, Item i) throws InvalidDataException{
        // create item
        Document newItem = new Document()
            .append("_id", new ObjectId())
            .append("name", i.getName())
            .append("price", i.getPrice())
            .append("supplier", i.getSupplier())
            .append("purchased", false);
        // add to wishlist collection
        MongoCollection<Document> items = db.getCollection("items");
        items.insertOne(newItem);

        // add wishlist id to user
        MongoCollection<Document> users = db.getCollection("users");
        Bson update = Updates.addToSet("items", newItem.getObjectId("_id"));
        users.updateOne(eq("authId", userId), update, null);

        return documentToItem(newItem);
    }
    
    private Item documentToItem(Document d) {
        return new Item(d.getString("name"),
        Double.parseDouble(d.getString("price")) ,
        d.getString("supplier"));
    }
    public Document getDocument(String userId, String id){
        checkItemOwnsership(new ObjectId(id), new ObjectId(userId));
        return super.getDocument(id,"items");
    }
    public Item get(String userId, String id) {
        return documentToItem(getDocument(userId, id));
    }

    public List<Item> getAll(String wishlistId) throws AuthenticationException {
        // get all items for the user 
        MongoCollection<Document> wishlists = db.getCollection("wishlists");
        Document match = wishlists.find(eq("_id", wishlistId)).first();
        // occurs if user is deleted but still has valid jwt for example
        if (match == null)
            throw new AuthenticationException("Wishlist does not exist!");

        List<ObjectId> itemIds = (ArrayList<ObjectId>) match.get("items");
        MongoCollection<Document> itemCol = db.getCollection("items");
        FindIterable<Document> matchingItems = itemCol.find(in("_id", itemIds))
            .sort(Sorts.ascending("name"));

        List<Item> items = new ArrayList<>(itemIds.size());
        for (Document i : matchingItems)
            items.add(documentToItem(i));
        
        return items;
    }
    public void delete(String userId, String id) {
        ObjectId itemId = new ObjectId(id);
        checkItemOwnsership(new ObjectId(userId), itemId);

        MongoCollection<Document> items = db.getCollection("items");
        DeleteResult result = items.deleteOne(eq("_id", itemId));
        if (result.getDeletedCount() == 0) 
            throw new NoSuchElementException("Wishlist does not exists");
    }
    private Document checkItemOwnsership(ObjectId userId, ObjectId itemId) {
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(eq("authId", userId)).first();
        List<ObjectId> itemIds = (ArrayList<ObjectId>) match.get("items");
        for (ObjectId i : itemIds) {
            if (i.equals(itemId))
                return match;
        }
        throw new ForbiddenException("Wishlist does not belong to requesting user");
    }
}
