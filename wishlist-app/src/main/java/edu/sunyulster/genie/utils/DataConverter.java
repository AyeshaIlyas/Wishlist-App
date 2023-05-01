package edu.sunyulster.genie.utils;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import edu.sunyulster.genie.db.DbConstants;
import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.models.User;
import edu.sunyulster.genie.models.Wishlist;

public class DataConverter {
    // user 
    public static Document userToDocument(User user, String authId) {
        Document userDoc = new Document()
            .append(DbConstants.ID, new ObjectId())
            .append(DbConstants.AUTH_ID, new ObjectId(authId))
            .append(DbConstants.EMAIL, user.getEmail())
            .append(DbConstants.FIRST_NAME, user.getFirstName())
            .append(DbConstants.LAST_NAME, user.getLastName())
            .append(DbConstants.WISHLISTS_SHARED_WITH_ME, new ArrayList<ObjectId>())
            .append(DbConstants.WISHLISTS, new ArrayList<ObjectId>());

        return userDoc;
    }

    public static User documentToUser(Document user) {
        return new User(user.getString(DbConstants.EMAIL), 
            user.getString(DbConstants.FIRST_NAME), 
            user.getString(DbConstants.LAST_NAME));
    }


    // wishlist
    public static Wishlist documentToWishlist(Document d) {
        int itemCount = ((ArrayList<ObjectId>) d.get(DbConstants.ITEMS)).size();
        Wishlist list =  new Wishlist(
            d.getString(DbConstants.OWNER),
            d.getObjectId(DbConstants.ID).toString(), 
            d.getString(DbConstants.NAME), 
            itemCount, 
            d.getDate(DbConstants.DATE_CREATED));
        list.setSharedWith((List<String>) d.get(DbConstants.SHARED_WITH));
        return list;
    }


    // item
    public static Item documentToItem(Document d) {
        return new Item(
            d.getObjectId(DbConstants.ID).toString(),
            d.getString(DbConstants.NAME),
            d.getDouble(DbConstants.PRICE),
            d.getString(DbConstants.SUPPLIER),
            d.getDate(DbConstants.DATE_CREATED),
            d.getString(DbConstants.GIFTER));
    }


    
    
}
