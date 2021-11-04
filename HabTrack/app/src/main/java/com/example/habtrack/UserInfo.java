/*
 * UserInfo
 *
 * This file contains the UserInfo.class which is exclusively used for storing, retrieving
 * and updating information about a user. That information is userName, email & password.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * October 28, 2021
 *
 * Copyright notice
 */

package com.example.habtrack;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserInfo {

    public final String userNameKey = "userName";
    public final String emailKey = "email";
    public final String passwordKey = "password";

    /**
     * This variable contains the userName that shall be used to store the userName of a user.
     * This var is of type {@link String}.
     */
    public String userName;

    /**
     * This variable contains the email that shall be used to store the email of a user.
     * This var is of type {@link String}.
     */
    public String email;

    /**
     * This variable contains the password that shall be used to store the password of a user.
     * This var is of type {@link String}.
     */
    public String password;

    /**
     * Empty Constructor. Should not be used or else calling instance methods will throw
     * exceptions.
     */
    public UserInfo() {
    }

    /**
     * Constructor of class that needs to be used or else no instance methods
     * will throw Exceptions.
     *
     * @param userName  userName is given by the user and shall be the userName assigned to a
     *                  account, and is of type {@link String}.
     * @param email     email is given by the user and shall be the email assigned to a
     *                  account, and is of type {@link String}.
     * @param password  password is given by the user and shall be the password assigned to a
     *                  account, and is of type {@link String}.
     */
    public UserInfo(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public HashMap<String, String> convertToKeyValuePair() {
        HashMap<String, String> userInfoData = new HashMap<>();

        userInfoData.put(userNameKey, this.userName);
        userInfoData.put(emailKey, this.email);
        userInfoData.put(passwordKey, this.password);

        return userInfoData;
    }

    public void convertFromKeyValuePair(HashMap<String, String> userInfoData) {
        this.userName = userInfoData.get(userNameKey);
        this.email = userInfoData.get(emailKey);
        this.password = userInfoData.get(passwordKey);
    }

    /**
     * This method returns the userName of a given UserInfo object.
     *
     * @return  The return type is {@link String}.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method sets the userName of a given object to the provided userName.
     *
     * @param userName  Takes input of userName of type {@link String}.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method returns the email of a given UserInfo object.
     *
     * @return  The return type is {@link String}.
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method sets the email of a given object to the provided email.
     *
     * @param email  Takes input of userName of type {@link String}.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method returns the password of a given UserInfo object.
     *
     * @return  The return type is {@link String}.
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method sets the password of a given object to the provided password.
     *
     * @param password  Takes input of userName of type {@link String}.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method returns a {@link DocumentReference} to document under "Users" collection
     * corresponding to the provided {@link String}
     *
     * @param userId  Takes input of userId of type {@link String}.
     */
    public static DocumentReference getUserDocumentReference(String userId) {
        return FirebaseFirestore.getInstance().collection("Users").document(userId);
    }
}
