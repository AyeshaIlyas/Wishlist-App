package edu.sunulster.genie.auth.util;

import java.util.regex.Pattern;

public class Validator {

    public static boolean isEmailValid(String email) {
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@" 
        + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$")
        .matcher(email)
        .matches();
    }

    public static  boolean isPasswordValid(String password) {
        final int MIN_PS_LENGTH = 5;
        return password.length() >= MIN_PS_LENGTH;
    }
    
}
