package edu.sunyulster.genie.models;

import java.util.Date;

public class Item {
    private String id;
    private String name;
    private Double price;
    private String supplier;
    private boolean purchased;
    private Date dateCreated;

    public Item() {

    }

    public Item(String id, String name, Double price, 
        String supplier, boolean purchased, Date dateCreated) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.supplier = supplier;
        this.purchased = purchased;
        this.dateCreated = dateCreated;
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

    public void setPerchased(boolean purchased) {
        this.purchased = purchased;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return String.format(
            "Item Name: %s", 
            name); 
    }
}
