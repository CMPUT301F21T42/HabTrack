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

        obtainHabitsFromUser();
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
                if (targetUserFollowers != null) {
                    if (targetUserFollowers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Log.d("Sample", "in");
                        collectionReferenceToHabit.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for (QueryDocumentSnapshot doc : value) {
                                    Habit habit = new Habit();
                                    habit.setTitle(String.valueOf(doc.getData().get("title")));
                                    habit.setProgress(Integer.parseInt(doc.getData().get("progress").toString()));
                                    habitDataList.add(habit);
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

}