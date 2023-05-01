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
import edu.sunyulster.genie.models.Item;
import edu.sunyulster.genie.utils.DataConverter;
import edu.sunyulster.genie.utils.Validator;
import edu.sunyulster.genie.utils.VerifyService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.ws.rs.ForbiddenException;


@ApplicationScoped
public class ItemService {
    @Inject
    MongoDatabase db;

    @Inject 
    VerifyService verifier;

    public Item create(String userId, String wishlistId, Item i) throws ForbiddenException, InvalidDataException, AuthenticationException{
        // checks if ids are valid objectids, user exists, wishlist exists, and wishlist belongs to user
        verifier.doesUserOwnWishlist(userId, wishlistId);
        ObjectId wId = new ObjectId(wishlistId);

        // validate information - only required field is item name
        if (!Validator.exists(i.getName())) 
            throw new InvalidDataException("Item must have non-empty name");

        // create item
        ObjectId itemId = new ObjectId();
        Document newItem = new Document()
            .append(DbConstants.ID, itemId)
            .append(DbConstants.NAME, i.getName())
            .append(DbConstants.GIFTER, null)
            .append(DbConstants.DATE_CREATED, new Date());

        // modifies newItem in place :(
        setOptionalItemData(i, newItem);

        // add to items collection
        MongoCollection<Document> items = db.getCollection(DbConstants.ITEMS);
        items.insertOne(newItem);

        // add item id to wishlist
        MongoCollection<Document> wishlists = db.getCollection(DbConstants.WISHLISTS);
        Bson update = Updates.addToSet(DbConstants.ITEMS, itemId);
        wishlists.updateOne(Filters.eq(DbConstants.ID, wId), update);

        return DataConverter.documentToItem(newItem);
    }

    public List<Item> getAll(String userId, String wishlistId, boolean isOwner) throws ForbiddenException, AuthenticationException, NoSuchElementException, InvalidDataException {
        Document wishlist;
        if (isOwner)
            // checks if ids are valid objectids, user exists, wishlist exists, and wishlist belongs to user
            wishlist = verifier.doesUserOwnWishlist(userId, wishlistId);
        else 
            // see if the user has been added to the wishlist
            wishlist = verifier.isWishlistSharedWithUser(userId, wishlistId);

        // get all items for the user 
        List<ObjectId> itemIds = (ArrayList<ObjectId>) wishlist.get(DbConstants.ITEMS);
        MongoCollection<Document> itemCol = db.getCollection(DbConstants.ITEMS);
        FindIterable<Document> matchingItems = itemCol.find(Filters.in(DbConstants.ID, itemIds))
            .sort(Sorts.ascending(DbConstants.DATE_CREATED));

        List<Item> items = new ArrayList<>(itemIds.size());
        for (Document i : matchingItems)
            items.add(DataConverter.documentToItem(i));
        
        return items;
    }

    public Item update(String userId, String wishlistId, Item newItem) throws InvalidDataException, AuthenticationException{
        // verify that all ids are valid, all resources exist, item belongs to wishlist belongs to user
        Document oldItem = verifier.doesUserOwnItem(userId, wishlistId, newItem.getId());
        ObjectId itemId = new ObjectId(newItem.getId());

         // validate information
         if (!Validator.exists(newItem.getName())) 
            throw new InvalidDataException("Item must have a non-empty name");

        // copy over datecreated
        // gifter can be updated but not with this method so copy unchanged
        // overwrite prev name wih new name
        Document updatedItem = new Document()
            .append(DbConstants.DATE_CREATED, oldItem.getDate(DbConstants.DATE_CREATED))
            .append(DbConstants.GIFTER, oldItem.getString(DbConstants.GIFTER))
            .append(DbConstants.NAME, newItem.getName());

        // modifies updatedItem in place :(
        setOptionalItemData(newItem, updatedItem);
        
        MongoCollection<Document> items = db.getCollection(DbConstants.ITEMS);
        items.replaceOne(Filters.eq(DbConstants.ID, itemId), updatedItem);
        
        return DataConverter.documentToItem(items.find(Filters.eq(DbConstants.ID, itemId)).first());
    }

    public void delete(String userId, String wishlistId, String itemId) throws AuthenticationException, NoSuchElementException, InvalidDataException {
        // verify that all ids are valid, all resources exist, item belongs to wishlist belongs to user
        verifier.doesUserOwnItem(userId, wishlistId, itemId);
        // remove item
        MongoCollection<Document> items = db.getCollection(DbConstants.ITEMS);
        items.deleteOne(Filters.eq(DbConstants.ID, new ObjectId(itemId)));
        // remove item id from wishlist
        MongoCollection<Document> wishlists = db.getCollection(DbConstants.WISHLISTS);
        wishlists.updateOne(Filters.eq(DbConstants.ID, new ObjectId(wishlistId)), Updates.pull(DbConstants.ITEMS, new ObjectId(itemId)));
    }


    private void setOptionalItemData(Item source, Document destination) throws InvalidDataException {
        // price is optional data
        if (source.getPrice() != null) {
            if (source.getPrice() < 0)
                throw new InvalidDataException("Price must be >= 0"); 
            else 
                destination.append(DbConstants.PRICE, source.getPrice());
        }

        // supplier is optional data
        if (Validator.exists(source.getSupplier()))
            destination.append(DbConstants.SUPPLIER, source.getSupplier());
    }

}
