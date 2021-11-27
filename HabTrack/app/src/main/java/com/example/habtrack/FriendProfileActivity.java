package com.example.habtrack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FriendProfileActivity extends AppCompatActivity {

    Context context = this;

    TextView friendUserName;
    TextView friendEmail;
    TextView followToViewHabits;
    TextView habitsCount;
    TextView followersCount;
    TextView followingsCount;

    int habitsCountData = 0;
    int followersCountData = 0;
    int followingsCountData = 0;

    ListView habitList;

    ArrayList<Habit> habitDataList;
    ArrayAdapter habitAdapter;

    FriendsManager friendsManager = new FriendsManager(context);

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        userID = getIntent().getExtras().getString("userID");
        UserInfo user = friendsManager.getUserInfoObjectFromUserID(userID);

        friendUserName = findViewById(R.id.user_name_tag);
        friendEmail = findViewById(R.id.email_tag);
        followToViewHabits = findViewById(R.id.follow_to_view_habits);

        habitList = findViewById(R.id.friends_habit_list_view);
        habitDataList = new ArrayList<>();
        habitAdapter = new FriendProfileListAdapter(this, habitDataList);

        habitsCount = findViewById(R.id.habits_count);
        followersCount = findViewById(R.id.followers_count);
        followingsCount = findViewById(R.id.followings_count);

        habitDataList = friendsManager.getUserHabits(userID);
        habitList.setAdapter(habitAdapter);
        if (habitDataList.isEmpty());
        followToViewHabits.setVisibility(View.VISIBLE);

        habitsCount.setText(getHabitsCount());
        followersCount.setText(getFollowersCount());
        followingsCount.setText(getFollowingsCount());
    }

    public String getFollowersCount() {
        return String.valueOf(friendsManager.getFollowerCount(userID));
    }

    public String getFollowingsCount() {
        return String.valueOf(friendsManager.getFollowingCount(userID));
    }

    public String getHabitsCount() {
        return String.valueOf(friendsManager.getHabitCount(userID));
    }

}