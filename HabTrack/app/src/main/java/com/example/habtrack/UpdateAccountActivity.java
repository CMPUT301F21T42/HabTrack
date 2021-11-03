/*
 * UpdateAccountActivity
 *
 * This source file UpdateAccountActivity.java serves as certain state/ screen of the application
 * that handles the case of when a user attempts to update their account. It centers around a
 * update page containing EditTexts where their username and email can be changed as well as
 * buttons to save their changes or delete their account.
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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * UpdateAccountActivity centers around the update account screen of the HabTrack application. It
 * retrieves the current username & email from the DB to display. These values can be changed
 * and then saved if possible. There are button listening events for if the user saves their changes
 * or deletes the account altogether. The class contains two main methods, updateAccount() &
 * deleteAccount() that are called in response to button presses. For updateAccount() it validates
 * user inputs and then interacts witht the DB through an instance of the UpdateAccountHandler class.
 * DeleteAccount() method uses the same UpdateAccountHandler() instance to delete the user from the
 * DB.
 *
 * @author Jenish
 * @see CredentialVerifier
 * @see UpdateAccountHandler
 * @see MainActivity
 * @version 1.0
 * @since 1.0
 */
public class UpdateAccountActivity extends AppCompatActivity {

    Context context = this;

    EditText updateUserName;
    EditText updateEmail;

    ProgressBar progressBar;

    Button saveChanges;
    Button deleteAccount;

    com.example.habtrack.UpdateAccountHandler updateAccountHandler = new com.example.habtrack.UpdateAccountHandler();

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
        setContentView(R.layout.activity_update_account);

        updateUserName = findViewById(R.id.update_user_name);
        updateEmail = findViewById(R.id.update_email);

        progressBar = findViewById(R.id.progress_bar);

        saveChanges = findViewById(R.id.save_changes);
        deleteAccount = findViewById(R.id.delete_account);

        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = updateAccountHandler.getDatabaseReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                initializeView();
                com.example.habtrack.UserInfo userInfo = snapshot.getValue(com.example.habtrack.UserInfo.class);

                updateUserName.setText(userInfo.getUserName());
                updateEmail.setText(userInfo.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccount();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });
    }

    /**
     * This method retrieves the text from the userName and email EditText's, checks the validity
     * of them using the CredentialVerifier class and then updates the values in the DB.
     */
    public void updateAccount() {
        String userName = updateUserName.getText().toString().trim();
        String email = updateEmail.getText().toString().trim();

        if (!com.example.habtrack.CredentialVerifier.verifyUserNameField(userName)) {
            updateUserName.setError(com.example.habtrack.CredentialVerifier.getUserNameVerifyFailureMessage());
            updateUserName.requestFocus();
            return;
        } else if (!com.example.habtrack.CredentialVerifier.verifyEmailField(email)) {
            updateEmail.setError(com.example.habtrack.CredentialVerifier.getEmailVerifyFailureMessage());
            updateEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        updateAccountHandler.updateUserNameInFireBaseDatabase(userName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        updateAccountHandler.updateEmailInFireBaseDatabase(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    updateEmail.setError(task.getException().getMessage());
                    updateEmail.requestFocus();
                    return;
                }
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        updateAccountHandler.updateAuthentication(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(context, UserProfileActivity.class));
                } else {
                    progressBar.setVisibility(View.GONE);
                    updateUserName.setError(task.getException().getMessage());
                    updateUserName.requestFocus();
                    return;
                }
            }
        });
    }

    /**
     * This method attempts to delete the account that is currently signed in. It creates and
     * AlertDialog to prompt the user if they are certain they wish to delete their account. If
     * so the instance of updateAccountHandler is used to delete the account from the DB.
     */
    public void deleteAccount() {
        new AlertDialog.Builder(context)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        updateAccountHandler.deleteAccountFromDatabase().addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        progressBar.setVisibility(View.VISIBLE);
                        updateAccountHandler.deleteAccountFromAuthentication().addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public void initializeView() {
        saveChanges.setVisibility(View.VISIBLE);
        deleteAccount.setVisibility(View.VISIBLE);
    }
}