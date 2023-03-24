package edu.sunyulster.genie.auth.models;

import java.util.Arrays;

public class User {

    private String email;
    private String password;
    private String[] roles;

    public User() {

    };

    public User(String email, String password, String[] roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setRoles(String[] roles) {
        this.roles = roles;
    }


    @Override
    public String toString() {
        return String.format("Email: %s%nPassword: %s%nRoles: %s", 
            email, password, Arrays.toString(roles));
    }
}
