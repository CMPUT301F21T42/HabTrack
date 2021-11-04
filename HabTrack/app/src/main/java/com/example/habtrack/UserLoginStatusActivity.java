/*
 * SignUpHandler
 *
 * This file UserLoginStatusActivity.class handles the initial control block when app
 * is launch. It checks if an user session is active, if so then the user is taken to
 * the MainActivity.java, otherwise the user is taken to the loginActivity.java.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * November 3rd, 2021
 *
 * Copyright notice
 */
package com.example.habtrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserLoginStatusActivity extends AppCompatActivity {

    Context context = this;

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_status);

        if (user != null) {
            startActivity(new Intent(context, MainActivity.class));
        } else {
            startActivity(new Intent(context, LoginActivity.class));
        }
        finish();
    }

}