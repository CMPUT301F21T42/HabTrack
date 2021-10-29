/*
 * LoginHandler
 *
 * This file LoginHandler class handles the interaction between the application and the
 * FireStore DB. Specifically this class handles the interaction when an attempt is made to sign
 * into the HabTrack application. The class is instantiated with an email and password passed
 * to the constructor.
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

/**
 * LoginHandler class is a class that handles the interaction between the android application
 * and FireStore DB when an attempt to sign in is made.
 *
 * @author Jenish
 * @see MainActivity
 * @version 1.0
 * @since 1.0
 */
public class LoginHandler {
    /**
     * This variable contains the email that shall be used to attempt to sign in.
     * This var is of type {@link String}.
     */
    private String email;

    /**
     * This variable contains the password that shall be used to attempt to sign in.
     * This var is of type {@link String}.
     */
    private String password;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * Sole constructor of class that needs to be used or else the login() method cannot be used.
     *
     * @param email     Give name of the email that is being used to sign in with, which should
     *                  be of type {@link String}.
     * @param password  Give name of the password that is being used to sign in with, which
     *                  should be of type {@link String}.
     */
    public LoginHandler(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * This method attempts to login to the FireBase DB with the class variables email, password
     * and instance of the DB. It returns a task object that contains info about the DB interaction
     * like if it was successful.
     *
     * @return task     Returns a task object containing information about the status of the
     *                  database access.
     *
     */
    public Task<AuthResult> login() {
        Task<AuthResult> task = mAuth.signInWithEmailAndPassword(email, password);
        return task;
    }
}
