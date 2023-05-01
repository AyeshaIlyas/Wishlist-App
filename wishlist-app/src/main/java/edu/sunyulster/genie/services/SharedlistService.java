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

import edu.sunyulster.genie.db.DbConstants;
import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.models.Wishlist;
import edu.sunyulster.genie.utils.DataConverter;
import edu.sunyulster.genie.utils.VerifyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.ForbiddenException;

@ApplicationScoped
public class SharedlistService {

    @Inject
    private MongoDatabase db;

    @Inject
    private VerifyService verifier;

    // move to /wishlists?isOwner=false
    public List<Wishlist> getAll(String userId) throws AuthenticationException {
        Document user = verifier.verifyUser(userId);

        List<ObjectId> wishlistIds = (ArrayList<ObjectId>) user.get(DbConstants.WISHLISTS_SHARED_WITH_ME);
        MongoCollection<Document> wishlistCol = db.getCollection(DbConstants.WISHLISTS);
        FindIterable<Document> matchingWishlists = wishlistCol.find(Filters.in(DbConstants.ID, wishlistIds))
            .sort(Sorts.ascending(DbConstants.DATE_CREATED));

        List<Wishlist> wishlists = new ArrayList<>(wishlistIds.size()); 
        for (Document w : matchingWishlists) 
            wishlists.add(DataConverter.documentToWishlist(w));

        return wishlists;
    }

    public void leaveSharedWishlist(String userId, String wishlistId) throws AuthenticationException, NoSuchElementException {
        Document user = verifier.verifyUser(userId); // check if user exists
        String email = user.getString(DbConstants.EMAIL);
        
        // check that wishlist was shared with user, that user exists, wishlist exists, and ids are valid
        verifier.isWishlistSharedWithUser(userId, wishlistId);
        
        // remove user's email from wishlist
        MongoCollection<Document> wishlists = db.getCollection(DbConstants.WISHLISTS);
        Bson filter = Filters.eq(DbConstants.ID, new ObjectId(wishlistId));
        Bson update = Updates.pull(DbConstants.SHARED_WITH, email);
        wishlists.updateOne(filter, update);

        // remove wishlist id from user
        MongoCollection<Document> users = db.getCollection(DbConstants.USERS);
        filter = Filters.eq(DbConstants.AUTH_ID, new ObjectId(userId));
        update = Updates.pull(DbConstants.WISHLISTS_SHARED_WITH_ME, new ObjectId(wishlistId));
        users.updateOne(filter, update);
    }
    

    public Item buy(String userId, String wishlistId, String itemId, boolean buy) throws AuthenticationException, NoSuchElementException {        
        // get user email
        Document user = verifier.verifyUser(userId);
        String email = user.getString(DbConstants.EMAIL);

        // get wishlist
        Document wishlist = verifier.verifyWishlist(wishlistId);

        // check if user has been added to the wishlist
        List<String> emails = (List<String>) wishlist.get(DbConstants.SHARED_WITH);
        if (emails.indexOf(email) == -1) 
            throw new ForbiddenException("User has not been added to the requested wishlist");

        // get item if it exists
        Document item = verifier.verifyItem(itemId);

        // make sure item is in wishlist specified 
        List<ObjectId> itemIds = (List<ObjectId>) wishlist.get(DbConstants.ITEMS);
        // safe to cast to ObjectId bc verifyItem called before
        if (itemIds.indexOf(new ObjectId(itemId)) == -1) 
            throw new NoSuchElementException("Requested item does not belong to specified wishlist");

        String gifter = item.getString(DbConstants.GIFTER);
        if (!(gifter == null || gifter.equals(email))) 
            throw new ForbiddenException("Item has already been bought");
        
        // if no one bought the item or the person with userId bought the item, they can change the bought status
        Item i = DataConverter.documentToItem(item);
        // no db change needed if the value requested equals the value in the db
        if ((!buy && gifter == null) || (buy && email.equals(gifter))) return i;
        
        // update item
        MongoCollection<Document> items = db.getCollection(DbConstants.ITEMS);
        Bson update = Updates.set(DbConstants.GIFTER, buy ? email : null);
        items.updateOne(Filters.eq(DbConstants.ID, new ObjectId(itemId)), update);
        
        // set gifter with correct value
        i.setGifter(buy ? email : null);

        return i;
    }

}
