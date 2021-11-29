/*
 * NotificationActivity
 *
 * NotificationActivity is the activity that centers around incoming follow requests. In this
 * activity a user can see the username of a user that has requested to follow them and either
 * grant or deny the follow request.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * November 28, 2021
 *
 * Copyright notice
 */

package com.example.habtrack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

/**
 * This activity centers around the Notification page of the application. Here the user can inspect
 * all their follow requests. They can click on a users name to be brought to the FriendsProfileActivity
 * of that user, and they can grant or deny the follow request of that user.
 *
 * @author Jenish
 * @see FriendProfileActivity
 * @version 1.0
 * @since 1.0
 */
public class NotificationActivity extends AppCompatActivity {

    Context context = this;

    ListView notificationsList;
    TextView noPendingFriendRequests;
    ArrayAdapter notificationsAdapter;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUserID = mAuth.getCurrentUser().getUid();
    FriendsManager friendsManager = FriendsManager.getInstance(currentUserID);

    ArrayList<String> incomingFriendRequests = new ArrayList<>();
    ArrayList<UserInfo> notificationsDataList;

    CollectionReference userCollection = FirebaseFirestore.getInstance().collection("Users");

    /**
     * Sets the listener for the backButton
     * @param item of type {@link MenuItem}
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noPendingFriendRequests = findViewById(R.id.no_pending_requests);
        notificationsList = findViewById(R.id.notifications_list_view);
        notificationsDataList = new ArrayList<>();

        notificationsAdapter = new NotificationListAdapter((NotificationActivity) context, notificationsDataList);
        getNotifications();

        notificationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserInfo user = notificationsDataList.get(i);
                Intent intent = new Intent(context, FriendProfileActivity.class);
                intent.putExtra("userID", user.getUserID()); // getText() SHOULD NOT be static!!!
                startActivity(intent);
            }
        });

    }


    public void acceptFriendRequest(UserInfo user) {
        // Add new user to followers
        friendsManager.addFollower(user.getUserID(), currentUserID);

        // Add current user to new user's followings
        friendsManager.addFollowing(currentUserID, user.getUserID());

        // Remove user from incoming friend request list
        friendsManager.removeIncomingFriendRequest(user.getUserID(), currentUserID);

        // Remove current user id from the other user's outgoing friend request list
        friendsManager.removeOutgoingFriendRequest(currentUserID, user.getUserID());
    }

    public void denyFriendRequest(UserInfo user) {
        // Remove user from incoming friend request
        friendsManager.removeIncomingFriendRequest(user.getUserID(), currentUserID);

        // Remove current user id from the other user's outgoing friend request list
        friendsManager.removeOutgoingFriendRequest(currentUserID, user.getUserID());
    }

    public void getNotifications() {
        DocumentReference userDocument = userCollection.document(currentUserID);
        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                incomingFriendRequests.clear();
                incomingFriendRequests = (ArrayList) value.getData().get("incomingFriendRequest");
                if (incomingFriendRequests == null) {
                    incomingFriendRequests = new ArrayList<>();
                }
            }
        });
        userCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                notificationsDataList.clear();
                for (QueryDocumentSnapshot doc: value) {
                    if (incomingFriendRequests.size() > 0) {
                        noPendingFriendRequests.setVisibility(View.INVISIBLE);
                        if (incomingFriendRequests.contains(doc.getId())) {
                            UserInfo user = new UserInfo();
                            user.setUserName(String.valueOf(doc.getData().get("userName")));
                            user.setEmail(String.valueOf(doc.getData().get("email")));
                            user.setUserID(doc.getId());
                            notificationsDataList.add(user);
                        }
                        notificationsList.setAdapter(notificationsAdapter);
                    } else {
                        Log.d("Sample", "No Notifications");
                        notificationsList.setAdapter(notificationsAdapter);
                        noPendingFriendRequests.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

    }
}