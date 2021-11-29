/** Copyright 2021
 * Raunak Agarwal, Revanth Atmakuri, Mattheas Jamieson,
 * Jenish Patel, Jasmine Wadhwa, Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code Habit} class
 */

package com.example.habtrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.habtrack.ui.edithabit.AddhabitFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * MainActivity holds the navigation for the drawer element,
 * the {@link com.example.habtrack.ui.myday.MydayFragment},
 * the {@link com.example.habtrack.ui.friends.FriendsFragment},
 * the {@link com.example.habtrack.ui.manage.ManageFragment}, and
 * the {@link com.example.habtrack.ui.events.EventsFragment}.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private ImageView imageView;
    private TextView currentUserName;
    private TextView currentUserEmail;

    FirebaseDatabase db;
    final String TAG = "Sample";
    final String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

        View headerView = navigationView.getHeaderView(0);
        imageView = headerView.findViewById(R.id.user_image_view);
        currentUserName = headerView.findViewById(R.id.user_username);
        currentUserEmail = headerView.findViewById(R.id.user_email);
        setCurrentUserDetails();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_myday, R.id.nav_friends, R.id.nav_manage, R.id.nav_events,
                R.id.nav_following, R.id.nav_notifications, R.id.nav_explore)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        BottomNavigationView bottom_nav_view = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottom_nav_view, navController);

        TextView user_name = findViewById(R.id.user_name);
        TextView user_email = findViewById(R.id.user_email);
//        user_name.setText();
//        user_email.setText();

        navigationView.setNavigationItemSelectedListener(this);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            public void onDestinationChanged(NavController controller, NavDestination destination, Bundle arguments) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_person_24);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FriendProfileActivity.class);
                intent.putExtra("userID", currentUserID);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * This method starts the {@link UserProfileActivity}
     * when the "profile" option is selected from the navigation drawer
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_profile) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_notifications) {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_following) {
            Intent intent = new Intent(this, FollowingActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_explore) {
            Intent intent = new Intent(this, SearchUsersActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void startFriendsActivity() {
        Intent intent = new Intent(this, SearchUsersActivity.class);
        startActivity(intent);
    }

    public void setCurrentUserDetails() {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Users");
        DocumentReference userDocument = collectionReference.document(currentUserID);
        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                UserInfo friend = new UserInfo();
                friend.setUserName(String.valueOf(value.getData().get("userName")));
                friend.setEmail(String.valueOf(value.getData().get("email")));
                friend.setUserID(value.getId());

                currentUserName.setText(friend.getUserName());
                currentUserEmail.setText(friend.getEmail());
            }
        });
    }

    // TODO: Exit app (finish()) only when the MainActivity is in "My Day" fragment
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}