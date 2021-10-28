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
    }
}