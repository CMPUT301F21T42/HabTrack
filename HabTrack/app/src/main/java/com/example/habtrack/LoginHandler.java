package com.example.habtrack;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginHandler {

    private String email, password;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public LoginHandler(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Task<AuthResult> login() {
        Task<AuthResult> task = mAuth.signInWithEmailAndPassword(email, password);
        return task;
    }
}
