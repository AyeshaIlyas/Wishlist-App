package edu.sunulster.genie.auth.services;

import static com.mongodb.client.model.Filters.eq;

import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import edu.sunulster.genie.auth.models.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;


@ApplicationScoped
public class RegisterService {

    @Inject
    MongoDatabase db;

    public String register(User user) throws AuthenticationException {
        // - - - - - - - - - -  VALIDATION - - - - - - - - - - // 
        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty()) {
            throw new AuthenticationException("Name is not valid");
        }

        // check if email is unique
        MongoCollection<Document> users = db.getCollection("users");
        Document match = users.find(eq("email", user.getEmail())).first();
        if (match != null) {
            throw new AuthenticationException("email already exists");
        }

        if (!isPasswordValid(user.getPassword())) {
            throw new AuthenticationException("Password is not valid");
        }

        if (!isEmailValid(user.getEmail())) {
            throw new AuthenticationException("Email is not valid");
        }

        InsertOneResult result = users.insertOne(convertUserToDocument(user));
        System.out.printf("User registration:%n- - - - - - - - - - -User: %s%nResult: %s%n",
            user.toString(), result.toString());
        return result.getInsertedId().asObjectId().getValue().toString();
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

    private Document convertUserToDocument(User user) {
        Document userDoc = new Document()
            .append("_id", new ObjectId())
            .append("firstName", user.getFirstName())
            .append("lastName", user.getLastName())
            .append("email", user.getEmail())
            .append("password", user.getPassword());
        return userDoc;
    }
    
}
