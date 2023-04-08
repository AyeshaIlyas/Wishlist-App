package edu.sunyulster.genie.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Wishlist {

    private String id;
    private String name;
    private int itemCount;
    private Date dateCreated;
    private List<String> sharedWith;

    public Wishlist() {

    }

    public Wishlist(String id, String name, int itemCount, Date dateCreated) {
        this.id = id;
        this.name = name;
        this.itemCount = itemCount;
        this.dateCreated = dateCreated;
        this.sharedWith = new ArrayList<>();
    }

    public List<String> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(List<String> sharedWith) {
        this.sharedWith = sharedWith;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date date) {
        this.dateCreated = date;
    }

    @Override
    public String toString() {
        return String.format(
            "Wishlist %s%nTotal Items: %s%nCreated on: %s", 
            name, itemCount, dateCreated.toString()); 
    }
}
