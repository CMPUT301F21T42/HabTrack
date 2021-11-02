/*
 * UpdateAccountHandler
 *
 * This source file contains the class definition of the UpdateAccount handler. This class
 * is instanitaed and its methods are used to interact with the DB to either update a users
 * email or username, or delete a users account from the DB altogether.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * October 29, 2021
 *
 * Copyright notice
 */

package com.example.habtrack;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * UpdateAccountHandler class is a class that handles the interaction between the android application
 * and FireStore DB when an request to edit a users profile is made, or a request to delete a users
 * account from the DB.
 *
 * @author Jenish
 * @see UpdateAccountActivity
 * @version 1.0
 * @since 1.0
 */
public class UpdateAccountHandler {


    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String userID;

    /**
     * Sole constructor of class that needs to be used, or else instance methods will throw
     * exceptions because no instance of the DB exists and the object is also not associated with
     * a user.
     */
    public UpdateAccountHandler() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
    }

    /**
     * This method returns a reference to the DB associated with this object.
     *
     * @return DatabaseReference     Returns a DatabaseReference object.
     */
    public DatabaseReference getDatabaseReference() {
        return databaseReference.child(userID);
    }

    /**
     * This method attempts to update a userName for the current user in the DB. It returns a task
     * object that contains info about the DB interaction and if it was successful or not.
     *
     * @param userName  A {@link String} that represents a new userName that the user
     *                  wants associated with their account, replacing the current userName.
     * @return task     Returns a task object containing information about the status of the
     *                  database access.
     */
    public Task<Void> updateUserNameInFireBaseDatabase(String userName) {
        Task<Void> task = databaseReference
                .child(userID)
                .child("userName")
                .setValue(userName);
        return task;
    }

    /**
     * This method attempts to update a email for the current user in the DB. It returns a task
     * object that contains info about the DB interaction and if it was successful or not.
     *
     * @param email  A {@link String} that represents a new email that the user
     *                  wants associated with their account, replacing the current email.
     * @return task     Returns a task object containing information about the status of the
     *                  database access.
     */
    public Task<Void> updateEmailInFireBaseDatabase(String email) {
        Task<Void> task = databaseReference
                .child(userID)
                .child("email")
                .setValue(email);
        return task;
    }

    public Task<Void> updateAuthentication(String email) {
        return user.updateEmail(email);
    }

    public Task<Void> deleteAccountFromAuthentication() {
        return user.delete();
    }

    public Task<Void> deleteAccountFromDatabase() {
        return databaseReference.child(userID).removeValue();
    }
}
