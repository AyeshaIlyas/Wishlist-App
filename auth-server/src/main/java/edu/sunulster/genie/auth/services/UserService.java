package edu.sunulster.genie.auth.services;

import static com.mongodb.client.model.Filters.eq;

import java.util.regex.Pattern;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;


@ApplicationScoped
public class UserService {

    @Inject
    MongoDatabase db;

    public String getUser() {
        System.out.println(db);
        MongoCollection<Document> collection = db.getCollection("users");
        collection.insertOne(new Document());
        System.out.println(collection);
        for (Document d : collection.find()) {
            System.out.println(d.toJson());
        }
        return "";
    }

    public void login(){

    }

    public void register(User user) {
        // - - - - - - - - - -  VALIDATION - - - - - - - - - - // 
        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty()) {
            throw new AuthenticationException("Name is not valid");
        }

        // check if email is uniqiue
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(eq("email", user.getEmail()));
        if (match != null) {
            throw new AuthenticationException("email already exists");
        }

        if (!isPasswordValid(user.getPassword())) {
            throw new AuthenticationException("Password is not valid");
        }

        if (!isEmailValid(user.getEmail())) {
            throw new AuthenticationException("Email is not valid");
        }

        addToDb(user);
    }

    private boolean isEmailValid(String email) {
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@" 
        + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$")
        .matcher(email)
        .matches();
    }

    private boolean isPasswordValid(String password) {
        final int MIN_PS_LENGTH = 5;
        return password.length() >= MIN_PS_LENGTH;
    }

    private Document addToDb(User user) {
        Document userDoc = new Document();
        userDoc.append("firstName", user.getFirstName());
        userDoc.append("lastName", user.getLastName());
        userDoc.append("email", user.getEmail());
        userDoc.append("password", user.getPassword());

        users.insertOne(userDoc);
    }
}
