package edu.sunyulster.genie.services;

import java.util.ArrayList;
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

import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.models.Wishlist;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.ForbiddenException;

@ApplicationScoped
public class SharedlistService {

    @Inject
    MongoDatabase db;

    public Item buy(String userId, String wishlistId, String itemId, boolean buy) throws AuthenticationException {
        // check that user is added to the wishlist and get their email

        // get user email
        MongoCollection<Document> users = db.getCollection("users");
        Document user = users.find(Filters.eq("authId", new ObjectId(userId))).first();
        if (user == null) 
            throw new AuthenticationException("User does not exist");
        String email = user.getString("email");
        
        // get wishlist
        MongoCollection<Document> wishlists = db.getCollection("wishlists");
        Document wishlist = wishlists.find(Filters.eq("_id", new ObjectId(wishlistId))).first();
        if (wishlist == null) 
            throw new NoSuchElementException("Wishlist does not exist");

        // check if user has been added to the wishlist
        List<String> emails = (List<String>) wishlist.get("sharedWith");
        if (emails.indexOf(email) == -1) 
            throw new ForbiddenException("User has not been added to the requested wishlist");

        // check that item exists in the wishlist
        List<ObjectId> itemIds = (List<ObjectId>) wishlist.get("items");
        if (itemIds.indexOf(new ObjectId(itemId)) == -1) 
            throw new NoSuchElementException("Requested item does not belong to specified wishlist");

        
        // get item
        MongoCollection<Document> items = db.getCollection("items");
        Document item = items.find(Filters.eq("_id", new ObjectId(itemId))).first();
        if (item == null) 
            throw new NoSuchElementException("Item does not exist");

        String gifter = item.getString("gifter");
        if (!(gifter == null || gifter.equals(email))) 
            throw new ForbiddenException("Item has already been bought");
        
        // if no one bought the item or the person with userId bought the item, they can change the bought status
        Item i = documentToItem(item);
        // no db change needed if the value requested equals the value in the db
        if ((!buy && gifter == null) || (buy && email.equals(gifter))) 
            return i;
        
        // update item
        Bson update = Updates.set("gifter", buy ? email : null);
        items.updateOne(Filters.eq("_id", new ObjectId(itemId)), update);
        
        // set gifter with correct value
        i.setGifter(buy ? email : null);

        return i;
    }

    public List<Wishlist> getAll(String userId) throws AuthenticationException {
        // get all wishlists for the user 
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(Filters.eq("authId", new ObjectId((userId)))).first();
        // occurs if user is deleted but still has valid jwt for example
        if (match == null)
            throw new AuthenticationException("User does not exist!");

        List<ObjectId> wishlistIds = (ArrayList<ObjectId>) match.get("sharedWishlists");
        MongoCollection<Document> wishlistCol = db.getCollection("wishlists");
        FindIterable<Document> matchingWishlists = wishlistCol.find(Filters.in("_id", wishlistIds))//take wishlist collection and
            .sort(Sorts.ascending("dateCreated"));

        List<Wishlist> wishlists = new ArrayList<>(wishlistIds.size()); //140-143 convert documents to wishlists
        for (Document w : matchingWishlists) 
            wishlists.add(documentToWishlist(w));

        return wishlists;
    }

    private Wishlist documentToWishlist(Document d) {
        int itemCount = ((ArrayList<ObjectId>) d.get("items")).size();
        Wishlist list =  new Wishlist(
            d.getString("owner"),
            d.getObjectId("_id").toString(), 
            d.getString("name"), 
            itemCount, 
            d.getDate("dateCreated"));
        list.setSharedWith((List<String>) d.get("sharedWith"));
        return list;
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
    
    public Wishlist leaveSharedWishlist(String userId, String wishlistId) throws AuthenticationException{
        // check that user is added to the wishlist and get their email

        // get user email
        MongoCollection<Document> users = db.getCollection("users");
        Document user = users.find(Filters.eq("authId", new ObjectId(userId))).first();
        if (user == null) 
            throw new AuthenticationException("User does not exist");
        String email = user.getString("email");
        
        // get wishlist
        MongoCollection<Document> wishlists = db.getCollection("wishlists");
        Document wishlist = wishlists.find(Filters.eq("_id", new ObjectId(wishlistId))).first();
        if (wishlist == null) 
            throw new NoSuchElementException("Wishlist does not exist");

        Wishlist w = documentToWishlist(wishlist);

        w.getSharedWith().remove(email);
        
        Bson filter = Filters.eq("_id", new ObjectId(w.getId()));
        Bson update = null;
        return w;
    }

}
