package edu.sunyulster.genie.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import edu.sunyulster.genie.db.DbConstants;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.ForbiddenException;

@ApplicationScoped
public class VerifyService {

    @Inject 
    private MongoDatabase db;

    // typical use case will allow the ExceptionMappers to handle the InvalidDataExcpetion
    public static ObjectId checkObjectId(String id) throws NoSuchElementException {
        if (id == null || !ObjectId.isValid(id))
            throw new NoSuchElementException(String.format("Object id (%s) is not valid", id));
        return new ObjectId(id);
    }

    public Document verifyUser(String userId) throws AuthenticationException {
        MongoCollection<Document> users = db.getCollection(DbConstants.USERS);
        Document user = users.find(Filters.eq(DbConstants.AUTH_ID, new ObjectId(userId))).first();
        // if user was deleted but token is still valid this is possible:
        if (user == null) 
            throw new AuthenticationException("User does not exist");
        return user;
    }

    public Document verifyWishlist(String wishlistId) throws NoSuchElementException {
        ObjectId wId = checkObjectId(wishlistId);
        MongoCollection<Document> wishlists = db.getCollection(DbConstants.WISHLISTS);
        Document wishlist = wishlists.find(Filters.eq(DbConstants.ID, wId)).first();
        if (wishlist == null) 
            throw new NoSuchElementException("Wishlist does not exist");
        return wishlist;
    }
    
    public Document verifyItem(String itemId)  {
        ObjectId iId = checkObjectId(itemId);
        MongoCollection<Document> items = db.getCollection(DbConstants.ITEMS);
        Document item = items.find(Filters.eq(DbConstants.ID, iId)).first();
        if (item == null) 
            throw new NoSuchElementException("Item does not exist");
        return item;
    }


    public Document doesUserOwnWishlist(String userId, String wishlistId) throws AuthenticationException, NoSuchElementException, ForbiddenException {
        Document user = verifyUser(userId); // does user exist
        Document wishlist = verifyWishlist(wishlistId); // does wishlist exist
        
        // does wishlist belong to user
        List<ObjectId> wishlistIds = (ArrayList<ObjectId>) user.get(DbConstants.WISHLISTS);
        if (!wishlistIds.contains(new ObjectId(wishlistId)))
            throw new ForbiddenException("User cannot access this wishlist");
        return wishlist;
    }

    public Document doesUserOwnItem(String userId, String wishlistId, String itemId) throws AuthenticationException, NoSuchElementException, ForbiddenException {
        // verify ids are valid, user and wishlist exist, and user owns wishlist
        Document wishlist = doesUserOwnWishlist(userId, wishlistId);
        // checks if item id is valid and item exists
        Document item = verifyItem(itemId); 

        // is item with itemId in wishlist?
        List<ObjectId> itemIds = (ArrayList<ObjectId>) wishlist.get(DbConstants.ITEMS);
        if (!itemIds.contains(new ObjectId(itemId)))
            throw new ForbiddenException("User cannot access this item");
        return item;
    }
    

    public Document isWishlistSharedWithUser(String userId, String wishlistId) throws AuthenticationException, NoSuchElementException {
        Document user = verifyUser(userId); // does user exist
        Document wishlist = verifyWishlist(wishlistId); // does wishlist exist
        
        List<ObjectId> wishlistIds = (ArrayList<ObjectId>) user.get(DbConstants.WISHLISTS_SHARED_WITH_ME);
        if(!wishlistIds.contains(new ObjectId(wishlistId)))
            throw new ForbiddenException("Wishlist is not shared with this user");
        return wishlist;
    }


}
