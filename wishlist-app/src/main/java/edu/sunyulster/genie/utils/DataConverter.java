package edu.sunyulster.genie.utils;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;

import edu.sunyulster.genie.db.DbConstants;
import edu.sunyulster.genie.models.User;

public class DataConverter {

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
    
    
}
