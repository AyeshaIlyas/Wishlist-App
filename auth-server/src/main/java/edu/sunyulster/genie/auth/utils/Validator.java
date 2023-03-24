package edu.sunyulster.genie.auth.utils;

import java.util.regex.Pattern;

public class Validator {

    public static boolean isEmailValid(String email) {
        return email != null && Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@" 
        + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$")
        .matcher(email)
        .matches();
    }

    public static boolean isPasswordValid(String password) {
        final int MIN_LENGTH = 5;
        return password != null && password.length() >= MIN_LENGTH;
    }

    public static boolean areRolesValid(String[] roles) {
        return roles != null;
    }
    
}
