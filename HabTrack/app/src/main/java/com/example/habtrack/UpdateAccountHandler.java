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

public class UpdateAccountHandler {


    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    String userID;

    public UpdateAccountHandler() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userID = user.getUid();
    }

    public Task<Void> updateUserNameInFireBaseDatabase(String userName) {
        Task<Void> task = db.collection("Users")
                .document(userID)
                .update("userName", userName);
        return task;
    }

    public Task<Void> updateEmailInFireBaseDatabase(String email) {
        Task<Void> task = db.collection("Users")
                .document(userID)
                .update("email", email);
        return task;
    }

    public Task<Void> updateAuthentication(String email) {
        return user.updateEmail(email);
    }

    public Task<Void> deleteAccountFromAuthentication() {
        return user.delete();
    }

    public Task<Void> deleteAccountFromDatabase() {
        Task<Void> task = db.collection("Users")
                .document(userID)
                .delete();
        return task;
    }
}
