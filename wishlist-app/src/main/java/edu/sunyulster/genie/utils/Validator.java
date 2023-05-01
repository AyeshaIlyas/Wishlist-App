package edu.sunyulster.genie.utils;

import java.util.regex.Pattern;

public class Validator {

    public static boolean isEmailValid(String email) {
        return email != null && Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@" 
        + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$")
        .matcher(email)
        .matches();
    }

    public static boolean exists(String s) {
        return s != null && !s.isEmpty();
    }

}