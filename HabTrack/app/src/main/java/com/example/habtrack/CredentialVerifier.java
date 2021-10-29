/*
 * CredentialVerifier
 *
 * This source file CredentialVerifier.java serves as a object that receives user input
 * that includes username, password & email and proceeds to check if it meets the criteria
 * to be considered valid. If a certain input is valid the method called simply returns
 * true, if the input fails a class variable String FailureMessage is updated depending on
 * why the input failed. It should also be noted all attributes and methods are declared
 * static, so this class is never instantiated.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * October 27, 2021
 *
 * Copyright notice
 */

package com.example.habtrack;

import android.util.Patterns;

/**
 * CredentialVerifier class contains static attributes and static methods that are used to
 * verify user inputted email, password and usernames. If a input is deemed to be invalid the class
 * will update static String attributes to hold an appropriate error message.
 *
 * @author Jenish
 * @see MainActivity
 * @see SignUpActivity
 * @version 1.0
 * @since 1.0
 */
public class CredentialVerifier {

    /**
     * This variable contains specific failure message for why username was invalid.
     * This var is of type {@link String}.
     */
    private static String userNameVerifyFailureMessage;

    /**
     * This variable contains specific failure message for why email was invalid.
     * This var is of type {@link String}.
     */
    private static String emailVerifyFailureMessage;

    /**
     * This variable contains specific failure message for why password was invalid.
     * This var is of type {@link String}.
     */
    private static String passwordVerifyFailureMessage;

    /**
     * Empty constructor
     */
    public CredentialVerifier() {
    }

    /**
     * This method returns a {@link boolean} dependent on if the userName meets the criteria
     * to be valid. It also updates userNameVerifyFailureMessage if the userName is invalid.
     * Method parameter userName is expected to be non null.
     *
     * @param userName  Takes input of userName of type {@link String}.
     * @return          The return type is {@link boolean}.
     */
    public static boolean verifyUserNameField(String userName) {
        if (userName.isEmpty()) {
            userNameVerifyFailureMessage = "Please enter your name";
            return false;
        } else if (userName.matches(".*\\d.*")) {
            userNameVerifyFailureMessage = "User name cannot contain numbers";
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method returns a {@link boolean} dependent on if the password meets the criteria
     * to be valid. It also updates passwordVerifyFailureMessage if the password is invalid.
     * Method parameter password is expected to be non null.
     *
     * @param password  Takes input of userName of type {@link String}.
     * @return          The return type is {@link boolean}.
     */
    public static boolean verifyPasswordFieldIsEmpty(String password) {
        if (password.isEmpty()) {
            passwordVerifyFailureMessage = "Please enter your password";
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method returns a {@link boolean} dependent on if the password length is considered
     * to be valid. It also updates passwordVerifyFailureMessage if the password is of an invalid
     * length. Method parameter password is expected to be non null.
     *
     * @param password  Takes input of userName of type {@link String}.
     * @return          The return type is {@link boolean}.
     */
    public static boolean verifyPasswordFieldLength(String password) {
        if (password.length() < 8) {
            passwordVerifyFailureMessage = "Minimum length of password is 8 characters";
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method returns a {@link boolean} dependent on if the email is considered
     * to be valid. It also updates emailVerifyFailureMessage if the password is of an invalid
     * criteria. Method parameter email is expected to be non null.
     *
     * @param email  Takes input of userName of type {@link String}.
     * @return       The return type is {@link boolean}.
     */
    public static boolean verifyEmailField(String email) {
        if (email.isEmpty()) {
            emailVerifyFailureMessage = "Please enter a valid email";
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailVerifyFailureMessage = "Please use a valid email address";
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method returns a {@link String} with the value of emailVerifyFailureMessage.
     *
     * @return  The return type is {@link String}.
     */
    public static String getEmailVerifyFailureMessage() {
        return emailVerifyFailureMessage;
    }

    /**
     * This method returns a {@link String} with the value of userNameVerifyFailureMessage.
     *
     * @return  The return type is {@link String}.
     */
    public static String getUserNameVerifyFailureMessage() {
        return userNameVerifyFailureMessage;
    }

    /**
     * This method returns a {@link String} with the value of passwordVerifyFailureMessage.
     *
     * @return  The return type is {@link String}.
     */
    public static String getPasswordVerifyFailureMessage() {
        return passwordVerifyFailureMessage;
    }

}
