package shell_track.model;

import javafx.beans.property.*;

public class User {
    private StringProperty userName;
    private StringProperty userPassword;
    private StringProperty userAccess;
    private StringProperty userFirstName;
    private StringProperty userLastName;
    private StringProperty userAddress;
    private StringProperty userEmail;

    public User(String userName, String password, String access) {
        this.userName = new SimpleStringProperty(userName);
        this.userPassword = new SimpleStringProperty(password);
        this.userAccess = new SimpleStringProperty(access);
    }
    public User(String userName, String password, String access,String firstName,String lastName,String address, String email) {
        this.userName = new SimpleStringProperty(userName);
        this.userPassword = new SimpleStringProperty(password);
        this.userAccess = new SimpleStringProperty(access);
        this.userFirstName = new SimpleStringProperty(firstName);
        this.userLastName = new SimpleStringProperty(lastName);
        this.userAddress = new SimpleStringProperty(address);
        this.userEmail = new SimpleStringProperty(email);
    }


    /**
     * getters
     * @return
     */
    public StringProperty userNameProperty() {
        return userName;
    }
    public String getUserEmail() {
        return userEmail.get();
    }
    public String getUserLastName() {
        return userLastName.get();
    }

    public StringProperty userLastNameProperty() {
        return userLastName;
    }

    public StringProperty userFirstNameProperty() {
        return userFirstName;
    }

    public String getUserFirstName(){
        return userFirstName.get();
    }
    public String getUserAddress(){
        return userAddress.get();
    }

    public StringProperty userAddressProperty() {
        return userAddress;
    }

    public String getUserAccess() {
        return userAccess.get();
    }

    public StringProperty userAccessProperty() {
        return userAccess;
    }

    public String getUserName() {
        return userName.get();
    }
    public StringProperty getNameProperty(){
        return userName;
    }

    public StringProperty userPasswordProperty() {
        return userPassword;
    }

    public String getUserPassword() {
        return userPassword.get();
    }

    public void setUserEmail(String userEmail) {
        this.userEmail.set(userEmail);
    }

    /**
     * Setters
     * @param userAccess
     */

    public void setUserAccess(String userAccess) {
        this.userAccess.set(userAccess);
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public void setUserPassword(String userPassword) {
        this.userPassword.set(userPassword);
    }

    public void setUserAddress(String userAddress) {
        this.userAddress.set(userAddress);
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName.set(userFirstName);
    }

    public void setUserLastName(String userLastName) {
        this.userLastName.set(userLastName);
    }
}

