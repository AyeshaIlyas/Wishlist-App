package edu.sunyulster.genie.models;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private double price;
    private String supplier;
    private boolean purchased;

    public Item() {

    }

    public Item(String name, double price, String supplier) {
        this.name = name;
        this.price = price;
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    @Override
    public String toString() {
        return String.format(
            "Item Name: %s%nPrice: %.2d%nSupplier: %s%n", 
            name, price, supplier); 
    }
}
