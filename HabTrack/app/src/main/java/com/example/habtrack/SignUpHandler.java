package com.example.habtrack;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpHandler {
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
        com.example.habtrack.UserInfo userInfo = new com.example.habtrack.UserInfo(userName, email, password);
        Task<Void> task = FirebaseDatabase.getInstance().getReference("Users")
                .child(mAuth.getCurrentUser().getUid())
                .setValue(userInfo);
        return task;
    }

}