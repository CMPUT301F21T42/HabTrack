/*
 * SignUpHandler
 *
 * This file SignUpHandler class handles the interaction between the application and the
 * FireStore DB, specifically when a user attempts to register an account in the HabTrack
 * application. This class is instantiated with an email, password and username.
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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * SignUpHandler class is a class that handles the interaction between the android application
 * and FireStore DB when an attempt to sign up is made.
 *
 * @author Jenish
 * @see UserInfo
 * @see SignUpActivity
 * @version 1.0
 * @since 1.0
 */
public class SignUpHandler {
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * This variable contains the userName that shall be used to attempt to register user.
     * This var is of type {@link String}.
     */
    private String userName;

    /**
     * This variable contains the email that shall be used to attempt to register user.
     * This var is of type {@link String}.
     */
    private String email;

    /**
     * This variable contains the password that shall be used to attempt to register user.
     * This var is of type {@link String}.
     */
    private String password;

    /**
     * Sole constructor of class that needs to be used or else no instance methods can be used.
     *
     * @param userName  userName is given by the user and shall be the userName assigned to a
     *                  potentially new account, and is of type {@link String}.
     * @param email     email is given by the user and shall be the email assigned to a
     *                  potentially new account, and is of type {@link String}.
     * @param password  password is given by the user and shall be the password assigned to a
     *                  potentially new account, and is of type {@link String}.
     */
    public SignUpHandler(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    /**
     * This method attempts to register an email and password to the DB. It returns a task object
     * that contains info about the DB interaction and if it was successful or not.
     *
     * @return task     Returns a task object containing information about the status of the
     *                  database access.
     */
    public Task<AuthResult> register() {
        Task<AuthResult> task = mAuth.createUserWithEmailAndPassword(email, password);
        return task;
    }

    public Task<Void> addUserInfoToFirebaseDatabase() {
        com.example.habtrack.UserInfo userInfo = new com.example.habtrack.UserInfo(userName, email, password);
        Task<Void> task = FirebaseDatabase.getInstance().getReference("Users")
                .child(mAuth.getCurrentUser().getUid())
                .setValue(userInfo);
        return task;
    }

}