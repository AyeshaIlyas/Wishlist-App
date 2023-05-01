package edu.sunyulster.genie.services;


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

import edu.sunyulster.genie.db.DbConstants;
import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.Wishlist;
import edu.sunyulster.genie.utils.DataConverter;
import edu.sunyulster.genie.utils.Validator;
import edu.sunyulster.genie.utils.VerifyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;


@ApplicationScoped
public class WishlistService {
    @Inject
    private MongoDatabase db;

    @Inject
    private VerifyService verifier;

    public Wishlist create(String userId, Wishlist w) throws InvalidDataException, AuthenticationException {
        // needed to specify user for new wishlist
        // change later to userId?
        Document user = verifier.verifyUser(userId);
        String userEmail = user.getString(DbConstants.EMAIL);

        // validate information
        if (!Validator.exists(w.getName())) 
            throw new InvalidDataException("Wishlist must have a name");

        // create wishlist
        Document newWishlist = new Document()
            .append(DbConstants.ID, new ObjectId())
            .append(DbConstants.NAME, w.getName())
            .append(DbConstants.ITEMS, new ArrayList<ObjectId>())
            .append(DbConstants.SHARED_WITH, new ArrayList<String>())
            .append(DbConstants.DATE_CREATED, new Date())
            .append(DbConstants.OWNER, userEmail);

        // add to wishlist collection
        MongoCollection<Document> wishlists = db.getCollection(DbConstants.WISHLISTS);
        wishlists.insertOne(newWishlist);

        // add wishlist id to user
        MongoCollection<Document> users = db.getCollection(DbConstants.USERS);
        Bson update = Updates.addToSet(DbConstants.WISHLISTS, newWishlist.getObjectId(DbConstants.ID));
        users.updateOne(Filters.eq(DbConstants.AUTH_ID, new ObjectId(userId)), update);

        return DataConverter.documentToWishlist(newWishlist);
    }

    public List<Wishlist> getAll(String userId) throws AuthenticationException {
        // get all wishlists for the user 
        Document user = verifier.verifyUser(userId);

        List<ObjectId> wishlistIds = (ArrayList<ObjectId>) user.get(DbConstants.WISHLISTS);
        MongoCollection<Document> wishlistCol = db.getCollection(DbConstants.WISHLISTS);
        FindIterable<Document> matchingWishlists = wishlistCol.find(Filters.in(DbConstants.ID, wishlistIds))
            .sort(Sorts.ascending(DbConstants.DATE_CREATED));

        List<Wishlist> wishlists = new ArrayList<>(wishlistIds.size());
        for (Document w : matchingWishlists) 
            wishlists.add(DataConverter.documentToWishlist(w));
        
        return wishlists;
    }

    public Wishlist get(String userId, String wishlistId, boolean isOwner) throws AuthenticationException, NoSuchElementException {
        Document wishlist;
        if (isOwner)
            // checks if ids are valid objectids, user exists, wishlist exists, and wishlist belongs to user
            wishlist = verifier.doesUserOwnWishlist(userId, wishlistId);
        else 
            // see if the user has been added to the wishlist
            wishlist = verifier.isWishlistSharedWithUser(userId, wishlistId);

        return DataConverter.documentToWishlist(wishlist);
    }

    public Wishlist update(String userId, Wishlist newWishlist) throws InvalidDataException, AuthenticationException {
        verifier.doesUserOwnWishlist(userId, newWishlist.getId());
        ObjectId wishlistId = new ObjectId(newWishlist.getId());

        MongoCollection<Document> wishlists = db.getCollection(DbConstants.WISHLISTS);
        Bson update = null;

         // validate information
        if (Validator.exists(newWishlist.getName())) {
            update = Updates.set(DbConstants.NAME, newWishlist.getName());
        }
        
        // share the wishlist with Max ONE person
        // TODO: change to enable the addition of multiple people
        if (newWishlist.getSharedWith() != null && newWishlist.getSharedWith().size() > 0) {
            String emailToAdd = newWishlist.getSharedWith().get(0);
            // is email in valid email format
            if(!Validator.isEmailValid(emailToAdd))
                throw new InvalidDataException("Invalid email.");
            // check that user is registered for this app
            MongoCollection<Document> users = db.getCollection(DbConstants.USERS);
            Document userToShareWith = users.find(Filters.eq(DbConstants.EMAIL, emailToAdd)).first();
            if (userToShareWith == null)
                throw new InvalidDataException("User must be registered to share with.");

            // get wishlist owner email
            Document wishlistOwner = verifier.verifyUser(userId); // check if user exists
            String ownerEmail = wishlistOwner.getString(DbConstants.EMAIL);

            // check to see if user is trying to share their own wishlist with themselves
            if (emailToAdd.equals(ownerEmail))
                throw new InvalidDataException("Owner cannot share wishlist with themselves. That would spoil the surprise silly :>");
    
            Bson emailUpdate = Updates.addToSet(DbConstants.SHARED_WITH, emailToAdd);
            update = update == null ? emailUpdate : Updates.combine(emailUpdate, update);
            
            // add the user to wishlist id to the person we are sharing the wishlist with
            Bson personUpdate = Updates.addToSet(DbConstants.WISHLISTS_SHARED_WITH_ME, wishlistId);
            users.updateOne(Filters.eq(DbConstants.EMAIL, emailToAdd), personUpdate);
        }

        //combine updates
        if (update != null) {
            Bson filter = Filters.eq(DbConstants.ID, wishlistId);
            wishlists.updateOne(filter, update);
        }
    
        return DataConverter.documentToWishlist(wishlists.find(Filters.eq(DbConstants.ID, wishlistId)).first());
    }   

    public void delete(String userId, String wishlistId) throws AuthenticationException, NoSuchElementException {
        Document wishlist = verifier.doesUserOwnWishlist(userId, wishlistId);
        ObjectId wId = new ObjectId(wishlistId);

        // delete all items from wishlist
        MongoCollection<Document> items = db.getCollection(DbConstants.ITEMS);
        List<ObjectId> itemsToDelete = (List<ObjectId>) wishlist.get(DbConstants.ITEMS);
        items.deleteMany(Filters.in(DbConstants.ID, itemsToDelete));
        
        // delete wishlist
        MongoCollection<Document> wishlists = db.getCollection(DbConstants.WISHLISTS);
        wishlists.deleteOne(Filters.eq(DbConstants.ID, wId));

        // delete wishlist object id from user
        MongoCollection<Document> users = db.getCollection(DbConstants.USERS);
        users.updateOne(Filters.eq(DbConstants.AUTH_ID, new ObjectId(userId)), Updates.pull(DbConstants.WISHLISTS, wId));
    
        // delete wishlist from people it has been shared with
        Bson update = Updates.pull(DbConstants.WISHLISTS_SHARED_WITH_ME, wId);
        users.updateMany(Filters.in(DbConstants.WISHLISTS_SHARED_WITH_ME, wId), update);
    }

    public void unshareWishlist(String emailToRemove, String wishlistId, String userId) throws AuthenticationException, NoSuchElementException{
        verifier.doesUserOwnWishlist(userId, wishlistId);

        // check that user is added to the wishlist
        MongoCollection<Document> users = db.getCollection(DbConstants.USERS);
        Document userToRemoveFrom = users.find(Filters.eq(DbConstants.EMAIL, emailToRemove)).first();
        ObjectId removeId = userToRemoveFrom.getObjectId(DbConstants.AUTH_ID);
        verifier.isWishlistSharedWithUser(removeId.toHexString(), wishlistId);

        // remove wishlist id from user
        Bson filter = Filters.eq(DbConstants.AUTH_ID, removeId);
        Bson update = Updates.pull(DbConstants.WISHLISTS_SHARED_WITH_ME, new ObjectId(wishlistId));
        users.updateOne(filter, update);

        // remove user's email from wishlist
        MongoCollection<Document> wishlists = db.getCollection(DbConstants.WISHLISTS);
        filter = Filters.eq(DbConstants.ID, new ObjectId(wishlistId));
        update = Updates.pull(DbConstants.SHARED_WITH, emailToRemove);
        wishlists.updateOne(filter, update);
 
    }

}
