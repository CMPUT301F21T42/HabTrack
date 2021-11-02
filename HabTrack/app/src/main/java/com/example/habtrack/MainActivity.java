/*
 * MainActivity
 *
 * This source file MainActivity.java serves as the entry point of the HabTrack app. It creates
 * a login/ signup page along with its Buttons, progressBar, editText, etc. It waits for user
 * input, i.e signup or login and then acts on the information/ action provided to either
 * start a new activity (SignUpActivity or UserProfileActivity) or remain on the current activity
 * and re-prompt user to enter valid information.
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

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.habtrack.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import com.example.habtrack.ui.edithabit.EdithabitFragment;

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
public class MainActivity extends AppCompatActivity
        implements EdithabitFragment.OnFragmentInteractionListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    FirebaseDatabase db;
    final String TAG = "Sample";

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

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EdithabitFragment().show(getSupportFragmentManager(), "ADD_HABIT");
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_myday, R.id.nav_friends, R.id.nav_manage, R.id.nav_events,
                R.id.nav_profile, R.id.nav_following, R.id.nav_follower)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        BottomNavigationView bottom_nav_view = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottom_nav_view, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            public void onDestinationChanged(NavController controller, NavDestination destination, Bundle arguments) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_person_24);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onOkPressed(Habit newHabit) {
        db = FirebaseDatabase.getInstance();
        final String habitTitle = newHabit.getTitle();

        if (newHabit.getTitle().length() > 0) {
            db.getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("habit")
                    .child(habitTitle)
                    .setValue(newHabit)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log.d(TAG, "Data has been added successfully!");
                            //Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log.d(TAG, "Data could not be added!" + e.toString());
                            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
//        final CollectionReference collectionReference = db.collection("Habits");
//        final String habitTitle = newHabit.getTitle();
//
//        if (newHabit.getTitle().length() > 0) {
//            collectionReference
//                    .document(habitTitle)
//                    .set(newHabit)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            // These are a method which gets executed when the task is succeeded
//                            Log.d(TAG, "Data has been added successfully!");
//                            //Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            // These are a method which gets executed if there’s any problem
//                            Log.d(TAG, "Data could not be added!" + e.toString());
//                            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
    }

    @Override
    public void onOkPressed(Habit selectedHabit, Habit newHabit) {
    }

    @Override
    public void onDeletePressed(Habit selectedHabit) {
//        db = FirebaseFirestore.getInstance();
//        final CollectionReference collectionReference = db.collection("Habits");

        db = FirebaseDatabase.getInstance();
        String title = selectedHabit.getTitle();

        db.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("habit")
                .child(title)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been deleted successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be deleted!" + e.toString());
                    }
                });
    }
}