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

public class LoginActivity extends AppCompatActivity {

    final Context context = this;

    EditText loginEmail;
    EditText loginPassword;

    Button login;
    TextView signUp;

    ProgressBar progressBar;

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
//                    startActivity(new Intent(context, UserProfileActivity.class));
                    startActivity(new Intent(context, MainActivity.class));
                } else {
                    progressBar.setVisibility(View.GONE);
                    loginEmail.setError(task.getException().getMessage());
                    loginEmail.requestFocus();
                    return;
                }
            }
        });

    }
}