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

public class UpdateAccountActivity extends AppCompatActivity {

    Context context = this;

    EditText updateUserName;
    EditText updateEmail;

    ProgressBar progressBar;

    Button saveChanges;
    Button deleteAccount;

    com.example.habtrack.UpdateAccountHandler updateAccountHandler = new com.example.habtrack.UpdateAccountHandler();

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