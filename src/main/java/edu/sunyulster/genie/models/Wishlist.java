package edu.sunyulster.genie.models;

import java.io.Serializable;

public class Wishlist implements Serializable {

    private String name;
    private int itemCount;
    private int itemsBought;

    public Wishlist() {

    }

    public Wishlist(String name, int itemCount, int itemsBought) {
        this.name = name;
        this.itemCount = itemCount;
        this.itemsBought = itemsBought;
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

    public int getItemsBought() {
        return itemsBought;
    }

    public void setItemsBought(int itemsBought) {
        this.itemsBought = itemsBought;
    }

    @Override
    public String toString() {
        return String.format(
            "Wishlist %s%nItems Bought: %s%nTotal Items: %s", 
            name, itemsBought, itemCount); 
    }
}
