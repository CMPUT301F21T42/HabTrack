package com.example.habtrack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserProfileActivity extends AppCompatActivity {

    Context context = this;

    ImageView imageView;

    TextView userName;
    TextView email;

    Button logout;
    Button updateAccount;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        progressBar = findViewById(R.id.progress_bar_user_profile);

        imageView =findViewById(R.id.avatar);

        userName = findViewById(R.id.display_user_name);
        email = findViewById(R.id.display_email);

        logout = findViewById(R.id.log_out);
        updateAccount = findViewById(R.id.update_account);

        progressBar.setVisibility(View.VISIBLE);
        DocumentReference userDocumentReference = UserInfo.getUserDatabaseReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userDocumentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                progressBar.setVisibility(View.GONE);
                userName.setText((String) value.getData().get("userName"));
                email.setText((String) value.getData().get("email"));
                initializeView();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                goToLoginActivity();
            }
        });

        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUpdateAccountActivity();
            }
        });

    }

    // Possibly convert to an interface
    public void initializeView() {
        logout.setVisibility(View.VISIBLE);
        updateAccount.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }

    public void goToUpdateAccountActivity() {
        startActivity(new Intent(context, UpdateAccountActivity.class));
    }

    public void goToLoginActivity() {
        startActivity(new Intent(context, LoginActivity.class));
        // Once signed out, and in sign in activity, then on back press
        // user should not be able to go back
        finish();
    }
}