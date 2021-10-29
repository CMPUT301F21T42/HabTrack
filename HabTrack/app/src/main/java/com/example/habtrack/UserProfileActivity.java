/*
 * UserProfileActivity
 *
 * This source file UserProfileActivity.java serves as the the user profile screen of the app.
 * It contains listening events for button components in the .xml layout if the user wants to
 * logout or update their account, and will then switch to MainActivity or UpdateAccountActivity
 * respectively. This activity also accesses the FireBase database to retrieve current username
 * and email to display. When this activity is opened it will check for any change in the user's
 * info, e.g the user has just updated their account username and/or password and will update
 * the display appropriately.
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * UserProfileActivity centers around the user profile of the HabTrack application. This class
 * contains onClick button listening events for the "logout" and "update account" buttons. And
 * lastly when the activity is created the class will ensure the username and email displayed
 * are the most up to date version.
 *
 * @author Jenish
 * @see MainActivity
 * @see UpdateAccountActivity
 * @version 1.0
 * @since 1.0
 */
public class UserProfileActivity extends AppCompatActivity {

    Context context = this;

    ImageView imageView;

    TextView userName;
    TextView email;

    Button logout;
    Button updateAccount;

    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String userID;

    /**
     * onCreate() method is called when the screen of the application is changed to the
     * user profile. It finds the views of all components defined in the xml file. It will
     * also retrieve username and email of the current user from the DB to display.
     * Button listening events are also present that changes the state/ screen of
     * dependent on how the user interacts with the screen.
     *
     * @param  savedInstanceState state of application.
     */
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

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                initializeView();
                com.example.habtrack.UserInfo userInfo = snapshot.getValue(com.example.habtrack.UserInfo.class);

                if (userInfo != null) {
                    userName.setText(userInfo.userName);
                    email.setText(userInfo.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(context, MainActivity.class));
            }
        });

        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, com.example.habtrack.UpdateAccountActivity.class));
            }
        });

    }

    // Possibly convert to an interface
    public void initializeView() {
        logout.setVisibility(View.VISIBLE);
        updateAccount.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }
}