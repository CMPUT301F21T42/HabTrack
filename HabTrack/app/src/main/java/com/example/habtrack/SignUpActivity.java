/*
 * SignUpActivity
 *
 * This source file SignUpActivity.java serves as certain state/ screen of the application that
 * handles the case of when a user attempts to create a profile. It centers around a signup page
 * that contains EditTexts where a user enters information from email to password, as well as
 * a Button for when they are ready to submit or want to return to the login page (MainActivity).
 * Textviews are present to help guide the user & the page has a progress bar too. If the user
 * presses the sign up button and attempts to register themselves. If registration is successful
 * then the application returns to the MainActivity. Else it will notify the user about
 * invalid inputs.
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * SignUpActivity centers around the signup page of the HabTrack Application. This class contains
 * onClick button listening events for the "login" and "signup" button. It also contains method
 * calls to verify user inputs of email and password if the user attempts to login. And lastly it
 * contains an instance of the LoginHandler in case of an attempted login with potentially valid
 * credentials. Depending on the user inputs/ actions a UserProfileActivity or SignUpActivity
 * activity may be started.
 *
 * @author Jenish
 * @see MainActivity
 * @see CredentialVerifier
 * @see SignUpHandler
 * @version 1.0
 * @since 1.0
 */
public class SignUpActivity extends AppCompatActivity {

    final Context context = this;

    TextView banner;
    TextView goToLoginActivity;

    EditText newUserName;
    EditText newEmail;
    EditText newPassword;
    EditText confirmNewPassword;

    ProgressBar progressBar;

    Button confirmSignUp;

    /**
     * onCreate() is called when Activity is created (similar to a constructor) and it finds
     * the ID's of different views and sets up listening events for the user to touch the
     * screen/ buttons.
     *
     * @param  savedInstanceState state of application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        banner = findViewById(R.id.banner);

        newUserName = findViewById(R.id.new_username);
        newEmail = findViewById(R.id.new_email);
        newPassword = findViewById(R.id.new_password);
        confirmNewPassword = findViewById(R.id.confirm_password);

        progressBar = findViewById(R.id.progress_bar_sign_up);

        confirmSignUp = findViewById(R.id.confirm_sign_up);
        goToLoginActivity = findViewById(R.id.log_in);

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MainActivity.class));
            }
        });

        goToLoginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MainActivity.class));
            }
        });

        confirmSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    /**
     * Reads user input for username, email, password & confirmed password then checks the
     * validity of inputs, setting appropriate error messages and snapping cursor to
     * appropriate EditText if any are invalid. Then attempts to add user profile to database
     * and returns to MainActivity if successful.
     */
    private void registerUser() {
        String userName = newUserName.getText().toString().trim();
        String email = newEmail.getText().toString().trim();
        String password = newPassword.getText().toString().trim();
        String confirmPassword = confirmNewPassword.getText().toString().trim();

        if (!com.example.habtrack.CredentialVerifier.verifyUserNameField(userName)) {
            newUserName.setError(com.example.habtrack.CredentialVerifier.getUserNameVerifyFailureMessage());
            newUserName.requestFocus();
            return;
        } else if (!com.example.habtrack.CredentialVerifier.verifyEmailField(email)) {
            newEmail.setError(com.example.habtrack.CredentialVerifier.getEmailVerifyFailureMessage());
            newEmail.requestFocus();
            return;
        } else if (!com.example.habtrack.CredentialVerifier.verifyPasswordFieldIsEmpty(password) || !com.example.habtrack.CredentialVerifier.verifyPasswordFieldLength(password)) {
            newPassword.setError(com.example.habtrack.CredentialVerifier.getPasswordVerifyFailureMessage());
            newPassword.requestFocus();
            return;
        } else if (!com.example.habtrack.CredentialVerifier.verifyPasswordFieldIsEmpty(confirmPassword)) {
            newPassword.setError("Please confirm your password");
            newPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmNewPassword.setError("Please make sure your passwords match");
            confirmNewPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        SignUpHandler signUpHandler = new SignUpHandler(userName, email, password);
        signUpHandler.register().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    signUpHandler.addUserInfoToFirebaseDatabase().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(context, MainActivity.class));
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    newEmail.setError(task.getException().getMessage());
                    newEmail.requestFocus();
                }
            }
        });
    }

}