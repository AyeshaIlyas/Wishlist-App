package edu.sunyulster.genie.services;

import java.util.List;

import edu.sunyulster.genie.db.FakeDb;
import edu.sunyulster.genie.models.User;
import jakarta.security.enterprise.AuthenticationException;

public class LoginService {
    
    public String login(String email, String password) throws AuthenticationException {
        // "connect" to db - THIS IS SUPER FAKE
        FakeDb db = new FakeDb();
        
        // check db for user with the email 
        User user = db.getUser(email);
        // if email does not exist, throw error
        if (user == null) 
            throw new AuthenticationException("Email does not exist");
        
        // if password doesnt match, throw error
        if (!password.equals(user.getPassword()))
            throw new AuthenticationException("Password does not match");

        // otherwise, create and return a JWT token
        return "jwt_token";
    }

    public List<User> getUsers() {
        FakeDb db = new FakeDb();
        return db.getUsers();
    }
}
