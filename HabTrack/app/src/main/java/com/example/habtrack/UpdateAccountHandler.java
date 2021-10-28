package com.example.habtrack;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateAccountHandler {


    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String userID;

    public UpdateAccountHandler() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference.child(userID);
    }

    public Task<Void> updateUserNameInFireBaseDatabase(String userName) {
        Task<Void> task = databaseReference
                .child(userID)
                .child("userName")
                .setValue(userName);
        return task;
    }

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
