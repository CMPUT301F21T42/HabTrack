/*
 * LoginActivity
 *
 * This source file LoginActivity.java serves as the entry point of the HabTrack app. It creates
 * a login/ signup page along with its Buttons, progressBar, editText, etc. It waits for user
 * input, i.e signup or login and then acts on the information/ action provided to either
 * start a new activity (SignUpActivity or UserProfileActivity) or remain on the current activity
 * and re-prompt user to enter valid information. It checks if the
 * the inputted credentials is valid, by authenticating it with Firebase authentication APIs.
 * If the login is successful then the application returns to the MainActivity, Else it will
 * notify the user about wrong email or password.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * October 27, 2021
 *
 * Copyright notice
 */

package com.example.habtrack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * This class contains onClick button listening events for the "login" and "signup" button. It also
 * contains method calls to verify user inputs of email and password if the user attempts to login.
 * And lastly it contains an instance of the LoginHandler in case of an attempted login with
 * potentially valid credentials. Depending on the user inputs/ actions a UserProfileActivity or
 * SignUpActivity activity may be started.
 *
 * @author Jenish
 * @see UserProfileActivity
 * @see LoginHandler
 * @see SignUpActivity
 * @see CredentialVerifier
 * @version 1.0
 * @since 1.0
 */
public class LoginActivity extends AppCompatActivity {

    final Context context = this;

    EditText loginEmail;
    EditText loginPassword;

    Button login;
    TextView signUp;

    ProgressBar progressBar;

    /**
     * onCreate() method is *generally* only called once for the lifetime off the app, i.e when
     * the Activity is created, it finds the ID's of the different views and sets up listening
     * events for when the user touches the screen/ button. It can be thought of in some sense
     * as a constructor for the class.
     *
     * @param  savedInstanceState state of application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.email);
        loginPassword = findViewById(R.id.password);

        login = findViewById(R.id.log_in);
        signUp = findViewById(R.id.sign_up);

        progressBar = findViewById(R.id.progress_bar_main);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SignUpActivity.class));
            }
        });
    }

    /**
     * Reads user input for email and password then checks the validity of inputs, setting
     * appropriate error messages and snapping cursor to appropriate EditText if any are invalid.
     * Then attempts to log-in user via Firebase Authentication APIs and returns to MainActivity
     * if successful, else warns user of incorrect email or password
     */
    public void loginUser() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (!CredentialVerifier.verifyEmailField(email)) {
            loginEmail.setError(CredentialVerifier.getEmailVerifyFailureMessage());
            loginEmail.requestFocus();
            return;
        } else if (!CredentialVerifier.verifyPasswordFieldIsEmpty(password)) {
            loginPassword.setError(CredentialVerifier.getPasswordVerifyFailureMessage());
            loginPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        LoginHandler loginHandler = new LoginHandler(email, password);
        loginHandler.login().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    goToMainActivity();
                } else {
                    progressBar.setVisibility(View.GONE);
                    loginEmail.setError(task.getException().getMessage());
                    loginEmail.requestFocus();
                    return;
                }
            }
        });
    }

    /**
     * This method starts the main activity and finishes current
     * activity
     */
    public void goToMainActivity() {
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}