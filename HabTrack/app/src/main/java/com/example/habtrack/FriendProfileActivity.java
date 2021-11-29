package com.example.habtrack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    Button follow;

    int habitsCountData = 0;
    int followersCountData = 0;
    int followingsCountData = 0;

    ListView habitList;

    ArrayList<Habit> habitDataList;
    ArrayAdapter habitAdapter;

    FriendsManager friendsManager;

    private String userID;
    private String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    UserInfo user;
    ArrayList currentUserFollowings;

    CollectionReference userCollection = FirebaseFirestore.getInstance().collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        userID = getIntent().getExtras().getString("userID");
        Log.d("friendsprofile", String.valueOf(userID));
        getUserInfoObjectFromUserID(userID);
        getFollowersFollowingsCount(userID);
        friendsManager = FriendsManager.getInstance(userID);

        friendUserName = findViewById(R.id.user_name_tag);
        friendEmail = findViewById(R.id.email_tag);
        followToViewHabits = findViewById(R.id.follow_to_view_habits);
        follow = findViewById(R.id.follow_button);

        habitsCount = findViewById(R.id.habits_count);
        followersCount = findViewById(R.id.followers_count);
        followingsCount = findViewById(R.id.followings_count);

        habitList = findViewById(R.id.friends_habit_list_view);
        habitDataList = new ArrayList<>();
        habitAdapter = new FriendProfileListAdapter(this, habitDataList);

        getHabits(userID);

        if (userID.equals(currentUserID))
            follow.setVisibility(View.INVISIBLE);

        setFollowButton(userID);

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (follow.getText() == "Follow") {
                    sendFriendRequest(user.getUserID());
                } else if (follow.getText() == "Requested") {
                    unSendFriendRequest(user.getUserID());
                } else if (follow.getText() == "Unfollow") {
                    unfollow(user.getUserID());
                }
            }
        });
    }

    public void getFollowersFollowingsCount(String userID) {
        DocumentReference userDocument = userCollection.document(userID);
        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList targetUserFollowers = (ArrayList) value.getData().get("follower");
                if (targetUserFollowers != null) {
                    followersCountData = targetUserFollowers.size();
                    Log.d("Sample", String.valueOf(followersCount));
                    followersCount.setText(String.valueOf(followersCountData));
                }
                ArrayList targetUserFollowings = (ArrayList) value.getData().get("following");
                if (targetUserFollowings != null) {
                    followingsCountData = targetUserFollowers.size();
                    Log.d("Sample", String.valueOf(followersCount));
                    followingsCount.setText(String.valueOf(followingsCountData));
                }
            }
        });
    }

    public void getUserInfoObjectFromUserID(String userID) {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Users");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc: value) {
                    if (doc.getId().equals(userID)) {
                        user = new UserInfo();
                        user.setUserName(String.valueOf(doc.getData().get("userName")));
                        user.setEmail(String.valueOf(doc.getData().get("email")));
                        user.setUserID(doc.getId());
                        friendUserName.setText(user.getUserName());
                        friendEmail.setText(user.getEmail());
                    }
                }
            }
        });
    }

    public void getFollowingsList() {
        DocumentReference userDocument = userCollection.document(currentUserID);
        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                currentUserFollowings = (ArrayList) value.getData().get("following");
            }
        });
    }

    public void getHabits(String userID) {
        getFollowingsList();
        DocumentReference userDocument = userCollection.document(userID);
        CollectionReference habitsCollection = userDocument.collection("Habits");
        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (currentUserFollowings != null) {
                    if (currentUserFollowings.contains(userID) || userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Log.d("Sample", "in");
                        habitsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                habitsCountData = value.size();
                                habitsCount.setText(String.valueOf(habitsCountData));
                                for (QueryDocumentSnapshot doc : value) {
                                    Log.d("Sample", doc.getId());
                                    Habit habit = new Habit();
                                    habit.setTitle(String.valueOf(doc.getData().get("title")));
                                    habit.setProgressNumerator(Integer.parseInt(doc.getData().get("progressNumerator").toString()));
                                    habit.setProgressDenominator(Integer.parseInt(doc.getData().get("progressDenominator").toString()));
                                    // habit.setPublic((boolean) doc.getData().get("public"));
                                    // Only public habits are visible
                                    if (habit.isPublic()) {
                                        habitDataList.add(habit);
                                    }
                                    habitDataList.add(habit);
                                }
                                if (habitDataList.isEmpty()) {
                                    followToViewHabits.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    } else {
                        followToViewHabits.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        habitList.setAdapter(habitAdapter);
    }

    public void sendFriendRequest(String targetUserId) {
        // Add sent friend request
        friendsManager.addOutgoingFriendRequest(targetUserId, currentUserID);

        // Load friend request to target user id
        friendsManager.addIncomingFriendRequest(currentUserID, targetUserId);
    }

    public void unSendFriendRequest(String targetUserId) {
        // Remove friend request from current user's outgoing friend request list
        friendsManager.removeOutgoingFriendRequest(targetUserId, currentUserID);

        // Remove incoming friend request from target user id
        friendsManager.removeIncomingFriendRequest(currentUserID, targetUserId);
    }

    public void unfollow(String targetUserId) {
        // Remove current user ID from target user's follower list
        friendsManager.removeFollower(currentUserID, targetUserId);

        // Remove target user from current user's following list
        friendsManager.removeFollowing(targetUserId, currentUserID);
    }

    public void setFollowButton(String userID) {
        follow.setBackgroundColor(context.getResources().getColor(R.color.ocean_dark));
        follow.setText("Follow");
        follow.setTextColor(Color.WHITE);

        DocumentReference userDocument = userCollection.document(currentUserID);
        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList temp = (ArrayList) value.getData().get("outgoingFriendRequest");
                if (temp != null) {
                    if (temp.size() > 0) {
                        if (temp.contains(userID)) {
                            Log.d("HELLLLO", "YESSS");
                            follow.setBackgroundColor(Color.WHITE);
                            follow.setText("Requested");
                            follow.setTextColor(Color.BLACK);
                        }
                    }
                }
                temp = (ArrayList) value.getData().get("following");
                if (temp != null) {
                    if (temp.size() > 0) {
                        if (temp.contains(userID)) {
                            Log.d("HELLLLO", "YESSS2");
                            follow.setBackgroundColor(Color.RED);
                            follow.setText("Unfollow");
                            follow.setTextColor(Color.WHITE);
                        }
                    }
                }
            }
        });
    }

}