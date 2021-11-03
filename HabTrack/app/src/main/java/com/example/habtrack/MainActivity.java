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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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

import com.example.habtrack.ui.edithabit.AddhabitFragment;

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
        implements NavigationView.OnNavigationItemSelectedListener {

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
                new AddhabitFragment().show(getSupportFragmentManager(), "ADD_HABIT");
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_myday, R.id.nav_friends, R.id.nav_manage, R.id.nav_events,
                R.id.nav_following, R.id.nav_follower)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        BottomNavigationView bottom_nav_view = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottom_nav_view, navController);

        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_profile) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        }
        return true;
    }
}