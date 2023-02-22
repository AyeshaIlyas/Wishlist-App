package edu.sunyulster.genie.db;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sunyulster.genie.models.User;

// NOTE: this entire class is just to simulate having a real db. We'll get rid of it later.
public class FakeDb {
    private static final String FILE_NAME = "users.ser";
    private static  Map<String, User> users; 

    public FakeDb() {
        loadUsersFromFile();
    }

    private static void loadUsersFromFile() {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            users = (Map<String, User>) input.readObject();
            System.out.println("[INFO] loaded user data successfully");
        } catch (IOException e1) { // includes FileNotFoundException 
            e1.printStackTrace();
            FakeDb.users = seedUsers();
            System.out.println("[INFO] populating users for the first time serialized");
        } catch (ClassNotFoundException e2) {
            System.out.println("Class not found");
            e2.printStackTrace();
        }
    }

    private static Map<String, User> seedUsers() {
        String[] firstNames = {"Ayesha", "Wendy", "Lance", "Joshua"};
        String[] lastName = {"Ilyas", "Stewart", "Eisele", "Demskie"};
        String[] emails = {"ayesha@gmail.com", "wendy@gmail.com", "lance@gmail.com", "joshua@gmail.com"};
        String[] passwords = {"hello", "password", "hackme", "blahblah"};

        Map<String, User> users = new HashMap<>();

        for (int i = 0; i < emails.length; i++) {
            users.put(emails[i], 
            new User(emails[i], firstNames[i], 
            lastName[i], passwords[i]));
        }

        return users;
    }

    private static void save() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            if (users == null)
                output.writeObject(new HashMap<String, User>());
            else 
                output.writeObject(users);
            System.out.println("[INFO] users have been serialized");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUser(String email) {
        return users.containsKey(email) ? users.get(email) : null;
    }

    public User createUser(User user) {
        users.put(user.getEmail(), user);
        save();
        return user;
    }
    
}
