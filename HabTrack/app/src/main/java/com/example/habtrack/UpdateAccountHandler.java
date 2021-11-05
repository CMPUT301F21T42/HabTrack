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

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Document;

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
    FirebaseFirestore db;
    String userID;

    /**
     * Sole constructor of class that needs to be used, or else instance methods will throw
     * exceptions because no instance of the DB exists and the object is also not associated with
     * a user.
     */
    public UpdateAccountHandler() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userID = user.getUid();
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
    public Task<Void> updateUserNameInFirestoreDatabase(String userName) {
        Task<Void> task = db.collection("Users")
                .document(userID)
                .update("userName", userName);
        return task;
    }

    /**
     * This method attempts to update a email for the current user in the DB. It returns a task
     * object that contains info about the DB interaction and if it was successful or not.
     *
     * @param email     A {@link String} that represents a new email that the user
     *                  wants associated with their account, replacing the current email.
     * @return task     Returns a task object containing information about the status of the
     *                  database access.
     */
    public Task<Void> updateEmailInFirestoreDatabase(String email) {
        Task<Void> task = db.collection("Users")
                .document(userID)
                .update("email", email);
        return task;
    }

    /**
     * This method attempts to update the email of the current user in Firebase Authentication.
     * It returns a task object that contains info about the authentication interaction and if it was
     * successful or not.
     *
     * @param email  A {@link String} that represents a new email that the user
     *                  wants associated with their account, replacing the current email.
     * @return task     Returns a task object containing information about the status of the
     *                  authentication access.
     */
    public Task<Void> updateAuthentication(String email) {
        return user.updateEmail(email);
    }

    /**
     * This method attempts to delete the account of the current user from the Firebase Authentication.
     * It returns a task object that contains info about the authentication interaction and if it was
     * successful or not.
     *
     * @return task     Returns a task object containing information about the status of the
     *                  deletion of user account from Firebase authentication
     */
    public Task<Void> deleteAccountFromAuthentication() {
        return user.delete();
    }

    /**
     * This method attempts to delete the account of the current user from the Firestore db.
     * It returns a task object that contains info about the authentication interaction and if it was
     * successful or not.
     *
     * @return task     Returns a task object containing information about the status of the
     *                  deletion of user account from Firestore db
     */
    public Task<Void> deleteAccountFromFirestoreDatabase() {
        Task<Void> task = db.collection("Users")
                .document(userID)
                .delete();
        return task;
    }
}
