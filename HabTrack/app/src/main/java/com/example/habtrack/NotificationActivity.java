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

public class NotificationActivity extends AppCompatActivity {

    Context context = this;

    ListView notificationsList;
    TextView noPendingFriendRequests;
    ArrayAdapter notificationsAdapter;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUserID = mAuth.getCurrentUser().getUid();
    FriendsManager friendsManager = new FriendsManager(context);

    ArrayList<String> incomingFriendRequests;
    ArrayList<UserInfo> notificationsDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        noPendingFriendRequests = findViewById(R.id.no_pending_requests);
        notificationsList = findViewById(R.id.notifications_list_view);
        notificationsDataList = new ArrayList<>();
        notificationsAdapter = new NotificationListAdapter((NotificationActivity) context, notificationsDataList);

        if (incomingFriendRequests != null) {
            if (incomingFriendRequests.size() > 0) {
                noPendingFriendRequests.setVisibility(View.INVISIBLE);
                notificationsDataList = friendsManager.getNotifications(currentUserID);
            } else {
                Log.d("Sample", "No Notifications");
                noPendingFriendRequests.setVisibility(View.VISIBLE);
            }
        } else {
            Log.d("Error", "array is null");
        }

        notificationsList.setAdapter(notificationsAdapter);

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(context, MainActivity.class));
    }
}