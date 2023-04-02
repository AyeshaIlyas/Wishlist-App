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

import edu.sunyulster.genie.services.ItemService;
import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.Wishlist;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.ForbiddenException;


@ApplicationScoped
public class WishlistService extends MongoService {

    public Wishlist create(String userId, Wishlist w) throws InvalidDataException {
        // validate information
        if (!isWishlistValid(w)) 
            throw new InvalidDataException("Wishlist must have a name");

        // create wishlist
        Document newWishlist = new Document()
            .append("_id", new ObjectId())
            .append("name", w.getName())
            .append("items", new ArrayList<ObjectId>())
            .append("dateCreated", new Date());

        // add to wishlist collection
        MongoCollection<Document> wishlists = db.getCollection("wishlists");
        wishlists.insertOne(newWishlist);

        // add wishlist id to user
        MongoCollection<Document> users = db.getCollection("users");
        Bson update = Updates.addToSet("wishlists", newWishlist.getObjectId("_id"));
        users.updateOne(eq("authId", userId), update, null);

        return documentToWishlist(newWishlist);
    }

    public List<Wishlist> getAll(String userId) throws AuthenticationException {
        // get all wishlists for the user 
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(eq("authId", userId)).first();
        // occurs if user is deleted but still has valid jwt for example
        if (match == null)
            throw new AuthenticationException("User does not exist!");

        List<ObjectId> wishlistIds = (ArrayList<ObjectId>) match.get("wishlists");
        MongoCollection<Document> wishlistCol = db.getCollection("wishlists");
        FindIterable<Document> matchingWishlists = wishlistCol.find(in("_id", wishlistIds))
            .sort(Sorts.ascending("dateCreated"));

        List<Wishlist> wishlists = new ArrayList<>(wishlistIds.size());
        for (Document w : matchingWishlists) 
            wishlists.add(documentToWishlist(w));
        
        return wishlists;
    }

    public Document getDocument(String userId, String id){
        checkWishlistOwnsership(new ObjectId(id), new ObjectId(userId));
        return super.getDocument(id,"wishlists");
    }

    public Wishlist get(String userId, String id) {
        return documentToWishlist(getDocument(userId, id));
    }

    public Wishlist update(String userId, Wishlist newWishlist) throws InvalidDataException {
        ObjectId wishlistId = new ObjectId(newWishlist.getId());
        checkWishlistOwnsership(new ObjectId(userId), wishlistId);
        
         // validate information
         if (!isWishlistValid(newWishlist)) 
            throw new InvalidDataException("Wishlist must have a name");

        // get updated info
        Document updatedWishlist = new Document().append("name", newWishlist.getName());

        // replace previous wishlist data with new data
        MongoCollection<Document> wishlists = db.getCollection("wishlists");
        UpdateResult result = wishlists.replaceOne(eq("_id", wishlistId), updatedWishlist);
        // throw exception if the wishlist does not exist
        if (result.getModifiedCount() == 0)
            throw new NoSuchElementException("Wishlist does not exists");
        
        return documentToWishlist(wishlists.find(eq("_id", wishlistId)).first());
    }

    public void delete(String userId, String id) {
        ObjectId wishlistId = new ObjectId(id);
        checkWishlistOwnsership(new ObjectId(userId), wishlistId);

        MongoCollection<Document> wishlists = db.getCollection("wishlists");
        DeleteResult result = wishlists.deleteOne(eq("_id", wishlistId));
        if (result.getDeletedCount() == 0) 
            throw new NoSuchElementException("Wishlist does not exists");
    }

    private Wishlist documentToWishlist(Document d) {
        int itemCount = ((ArrayList<ObjectId>) d.get("items")).size();
        return new Wishlist(d.getObjectId("_id").toString(), 
            d.getString("name"), 
            itemCount, 
            d.getDate("dateCreated"));
    }

    private Document checkWishlistOwnsership(ObjectId userId, ObjectId wishlistId) {
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(eq("authId", userId)).first();
        List<ObjectId> wishlistIds = (ArrayList<ObjectId>) match.get("wishlists");
        for (ObjectId i : wishlistIds) {
            if (i.equals(wishlistId))
                return match;
        }
        throw new ForbiddenException("Wishlist does not belong to requesting user");
    }
}
