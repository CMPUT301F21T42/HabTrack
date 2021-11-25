package com.example.habtrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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
import com.google.firebase.database.FirebaseDatabase;

import com.example.habtrack.ui.edithabit.AddhabitFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    FirebaseDatabase db;
    final String TAG = "Sample";

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
                R.id.nav_myday, R.id.nav_manage, R.id.nav_events,
                R.id.nav_following, R.id.nav_follower)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        BottomNavigationView bottom_nav_view = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottom_nav_view, navController);

        navigationView.setNavigationItemSelectedListener(this);

        bottom_nav_view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_friends) {
                    startFriendsActivity();
                }
                return false;
            }
        });
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
            finish();
        } else if (item.getItemId() == R.id.nav_follower) {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.nav_following) {
            Intent intent = new Intent(this, FollowingActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    public void startFriendsActivity() {
        Intent intent = new Intent(this, SearchUsersActivity.class);
        startActivity(intent);
    }

    // TODO: Exit app (finish()) only when the MainActivity is in "My Day" fragment
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finishAffinity();
//    }
}