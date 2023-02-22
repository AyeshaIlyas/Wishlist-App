package edu.sunyulster.genie.services;

import edu.sunyulster.genie.db.FakeDb;
import edu.sunyulster.genie.models.User;

public class RegisterService {
    // create custom RegistationException?
    public User register(User newUser) {
        // "connect to db"
        FakeDb db = new FakeDb();

        // VALIDATION 
        int PASSWORD_LENGTH = 7;
        User user = db.getUser(newUser.getEmail());
        // if user with email alr exists throw error
        if (user != null) 
            throw new IllegalArgumentException("email already exists");
        // if password not valid
        if (newUser.getPassword().length() < PASSWORD_LENGTH)
            throw new IllegalArgumentException("password must be 7 characters or more"); 
        
        // add user to db
        return db.createUser(newUser);
    }
    
}
