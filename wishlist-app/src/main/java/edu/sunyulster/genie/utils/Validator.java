package edu.sunyulster.genie.utils;

import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import edu.sunyulster.genie.exceptions.InvalidDataException;
import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.models.Wishlist;

public class Validator {
    public static boolean isNameValid(String name) {
        return name != null && !name.isEmpty();
    }

    public static boolean isEmailValid(String email) {
        return email != null && Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@" 
        + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$")
        .matcher(email)
        .matches();
    }

    public static boolean isWishlistValid(Wishlist w) {
        return w.getName() != null && !w.getName().isEmpty();
    }

    

    public static boolean isItemValid(Item i) {
        return i.getName() != null && !i.getName().isEmpty();
    }

    public static boolean exists(String s) {
        return s != null && !s.isEmpty();
    }

    public static void isObjectId(String id) throws InvalidDataException {
        if (id == null || !ObjectId.isValid(id))
            throw new InvalidDataException(String.format("Object id (%s) is not valid", id));
    }
}