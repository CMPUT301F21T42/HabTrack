package com.example.habtrack;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpHandler {
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String userName;
    private String email;
    private String password;

    public SignUpHandler(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public Task<AuthResult> register() {
        Task<AuthResult> task = mAuth.createUserWithEmailAndPassword(email, password);
        return task;
    }

    public Task<Void> addUserInfoToFirebaseDatabase() {
        UserInfo userInfo = new UserInfo(userName, email, password);
        return db.collection("Users").document(mAuth.getCurrentUser().getUid())
                .set(userInfo.convertToKeyValuePair());
    }

}