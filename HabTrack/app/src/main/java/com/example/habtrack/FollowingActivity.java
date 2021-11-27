package com.example.habtrack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class FollowingActivity extends AppCompatActivity {

    Context context = this;

    ListView followingList;
    TextView noFollowing;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FriendsManager friendsManager = new FriendsManager(context);
    String currentUserID = mAuth.getCurrentUser().getUid();

    ArrayList<UserInfo> followingDataList;
    FollowingListAdapter followingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        noFollowing = findViewById(R.id.no_following);
        followingList = findViewById(R.id.following_list_view);

        followingDataList = new ArrayList<>();
        followingAdapter = new FollowingListAdapter((FollowingActivity) this, followingDataList);

        followingDataList = friendsManager.getFollowingList(currentUserID);
        if (followingDataList != null) {
            if (followingDataList.size() > 0) {
                noFollowing.setVisibility(View.INVISIBLE);
            } else {
                Log.d("Sample", "No Followings");
                noFollowing.setVisibility(View.VISIBLE);
            }
        } else {
            Log.d("Error", "array is null");
        }

        followingList.setAdapter(followingAdapter);

        followingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserInfo user = followingDataList.get(i);
                Intent intent = new Intent(context, FriendProfileActivity.class);
                intent.putExtra("userID", user.getUserID()); // getText() SHOULD NOT be static!!!
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}