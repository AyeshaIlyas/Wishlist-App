package edu.sunyulster.genie.models;

import java.util.Date;

public class Item {
    private String id;
    private String name;
    private Double price;
    private String supplier;
    private boolean purchased;
    private String gifter;
    private Date dateCreated;

    public Item() {

    }

    public Item(String id, String name, Double price, 
        String supplier, Date dateCreated, String gifter) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.supplier = supplier;
        this.gifter = gifter; 
        this.purchased = !(gifter == null);
        this.dateCreated = dateCreated;
    }

    public void setGifter(String gifter) {
        this.gifter = gifter; // null or value
        this.purchased = !(gifter == null); // if null gifter, item it not purchased otherwise it is purchased
    }

    public String getGifter() {
        return gifter;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public boolean isPurchased() {
        return purchased;
    }

    // public void setPerchased(boolean purchased) {
    //     this.purchased = purchased;
    // }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return String.format(
            "Item"+
            "%n Name : %s"+
            "%n Price : $%.2f"+
            "%n Supplier : %s", 
            name,price,supplier); 
    }
}
