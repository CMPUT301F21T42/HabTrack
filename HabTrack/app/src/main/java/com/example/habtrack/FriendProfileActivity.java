package com.example.habtrack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReferenceToUser = db.collection("Users");

    private String userID;
    private UserInfo friend;

    CollectionReference collectionReferenceToHabit;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        userID = getIntent().getExtras().getString("userID");
        obtainFriendInfoFromUserID();
        collectionReferenceToHabit = db.collection("Users").document(userID).collection("Habits");

        friendUserName = findViewById(R.id.user_name_tag);
        friendEmail = findViewById(R.id.email_tag);
        followToViewHabits = findViewById(R.id.follow_to_view_habits);

        habitList = findViewById(R.id.friends_habit_list_view);
        habitDataList = new ArrayList<>();
        habitAdapter = new FriendProfileListAdapter(this, habitDataList);

        habitsCount = findViewById(R.id.habits_count);
        followersCount = findViewById(R.id.followers_count);
        followingsCount = findViewById(R.id.followings_count);

        obtainHabitsFromUser();
        getFollowersCount();
        getFollowingsCount();
    }

    public void obtainFriendInfoFromUserID() {
        collectionReferenceToUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc: value) {
                    if (userID.equals(doc.getId())) {
                        friend = new UserInfo();
                        friend.setUserName(String.valueOf(doc.getData().get("userName")));
                        friend.setEmail(String.valueOf(doc.getData().get("email")));
                        friend.setUserID(doc.getId());
                    }
                }
                friendUserName.setText(friend.getUserName());
                friendEmail.setText(friend.getEmail());
            }
        });
    }

    public void obtainHabitsFromUser() {
        documentReference = db.document("Users/" + userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList targetUserFollowers = (ArrayList) value.getData().get("follower");
                if (targetUserFollowers != null || userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    if (targetUserFollowers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid()) || userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Log.d("Sample", "in");
                        collectionReferenceToHabit.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                habitsCountData = value.size();
                                habitsCount.setText(String.valueOf(habitsCountData));
                                for (QueryDocumentSnapshot doc : value) {
                                    Habit habit = new Habit();
                                    habit.setTitle(String.valueOf(doc.getData().get("title")));
                                    habit.setProgress(Integer.parseInt(doc.getData().get("progress").toString()));
                                    habit.setIsPublic(Boolean.parseBoolean(doc.getData().get("isPublic").toString()));
                                    // Only public habits are visible
                                    if (habit.getIsPublic()) {
                                        habitDataList.add(habit);
                                    }
                                }
                                habitList.setAdapter(habitAdapter);
                            }
                        });
                    } else {
                        followToViewHabits.setVisibility(View.VISIBLE);
                    }
                } else {
                    followToViewHabits.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    public void getFollowersCount() {
        documentReference = db.document("Users/" + userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList targetUserFollowers = (ArrayList) value.getData().get("follower");
                if (targetUserFollowers != null) {
                    followersCountData = targetUserFollowers.size();
                    Log.d("Sample", String.valueOf(followersCountData));
                    followersCount.setText(String.valueOf(followersCountData));
                }

            }
        });
    }

    public void getFollowingsCount() {
        documentReference = db.document("Users/" + userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList targetUserFollowings = (ArrayList) value.getData().get("following");
                if (targetUserFollowings != null) {
                    followingsCountData = targetUserFollowings.size();
                    Log.d("Sample", String.valueOf(followingsCountData));
                    followingsCount.setText(String.valueOf(followingsCountData));
                }
            }
        });
    }

}