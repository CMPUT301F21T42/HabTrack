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

public class CredentialVerifier {

    private static String userNameVerifyFailureMessage;
    private static String emailVerifyFailureMessage;
    private static String passwordVerifyFailureMessage;

    public CredentialVerifier() {
    }

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

    public static boolean verifyPasswordFieldIsEmpty(String password) {
        if (password.isEmpty()) {
            passwordVerifyFailureMessage = "Please enter your password";
            return false;
        } else {
            return true;
        }
    }

    public static boolean verifyPasswordFieldLength(String password) {
        if (password.length() < 8) {
            passwordVerifyFailureMessage = "Minimum length of password is 8 characters";
            return false;
        } else {
            return true;
        }
    }

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

    public static String getEmailVerifyFailureMessage() {
        return emailVerifyFailureMessage;
    }

    public static String getUserNameVerifyFailureMessage() {
        return userNameVerifyFailureMessage;
    }

    public static String getPasswordVerifyFailureMessage() {
        return passwordVerifyFailureMessage;
    }

}
