package edu.sunyulster.genie.models;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public User() {
        
    }

    public User(String email, String firstName, String lastName, String password){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
            "Name: %s %s%nEmail: %s%nPassword: %s", 
            firstName, lastName, email, password); 
    }
}