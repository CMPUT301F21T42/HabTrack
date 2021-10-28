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
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}