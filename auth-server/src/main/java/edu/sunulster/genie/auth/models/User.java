package edu.sunulster.genie.auth.models;

public class User {

    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public User() {

    }

    public User(String email, String password, String firstName, String lastName){
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public void updateUser(String email, String password, String firstname, String lastName){
        this.email = email;
        this.password = password;
        this.firstName = firstname;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("Name: %s %s%nEmail: %s%nPassword: %s", 
            firstName, lastName, email, password);
    }
}
