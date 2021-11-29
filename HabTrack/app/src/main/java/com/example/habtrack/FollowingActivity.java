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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FollowingActivity extends AppCompatActivity {

    Context context = this;

    ListView followingList;
    TextView noFollowing;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUserID = mAuth.getCurrentUser().getUid();
    FriendsManager friendsManager = FriendsManager.getInstance(currentUserID);

    ArrayList<UserInfo> followingDataList;
    ArrayList followingUserIDList;
    FollowingListAdapter followingAdapter;

    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Users");
    DocumentReference documentReference = FirebaseFirestore.getInstance().document("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        noFollowing = findViewById(R.id.no_following);
        followingList = findViewById(R.id.following_list_view);

        followingDataList = new ArrayList<>();

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                followingUserIDList = (ArrayList) value.getData().get("following");
                if (followingUserIDList != null) {
                    if (followingUserIDList.size() > 0) {
                        noFollowing.setVisibility(View.INVISIBLE);
                        obtainFriendInfoFromUserID();
                    } else {
                        Log.d("Sample", "No Notifications");
                        noFollowing.setVisibility(View.VISIBLE);
                    }
                } else {
                    noFollowing.setVisibility(View.VISIBLE);
                    Log.d("Error", "array is null");
                }

            }
        });

        followingAdapter = new FollowingListAdapter((FollowingActivity) this, followingDataList);
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

    public void obtainFriendInfoFromUserID() {
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                followingDataList.clear();
                for (QueryDocumentSnapshot doc: value) {
                    if (followingUserIDList.contains(doc.getId())) {
                        UserInfo friend = new UserInfo();
                        friend.setUserName(String.valueOf(doc.getData().get("userName")));
                        friend.setEmail(String.valueOf(doc.getData().get("email")));
                        friend.setUserID(doc.getId());
                        followingDataList.add(friend);
                    }
                }
                followingList.setAdapter(followingAdapter);
            }
        });
    }
}