package edu.sunyulster.genie.models;

import java.io.Serializable;
import java.util.LinkedList;

public class User implements Serializable {
    private String email;
    private String firstName;
    private String lastName;
    private LinkedList<Wishlist> sharedLists;

    public User() {
        
    }

    public User(String email, String firstName, String lastName){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        sharedLists = new LinkedList<Wishlist>();
    }

    public LinkedList<Wishlist> getSharedLists() {
        return sharedLists;
    }

    public void setSharedLists(LinkedList<Wishlist> newLists) {
        sharedLists = newLists;
    }

    public void addSharedList(Wishlist wishlist) {
        sharedLists.add(wishlist);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
            "Name: %s %s%nEmail: %s", 
            firstName, lastName, email); 
    }
}