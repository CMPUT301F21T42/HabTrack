package com.example.habtrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SearchUsersActivity extends AppCompatActivity {

    Context context = this;

    EditText searchUserInput;
    Button searchUser;
    TextView noUsersFoundMessage;

    ListView usersList;
    ArrayAdapter usersAdapter;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUserID = mAuth.getCurrentUser().getUid();
    FriendsManager friendsManager = new FriendsManager(context);

    ArrayList<UserInfo> userDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        searchUserInput = findViewById(R.id.searchFriendInput);
        searchUser = findViewById(R.id.searchFriendBtn);
        noUsersFoundMessage = findViewById(R.id.noUsersFoundMessage);

        usersList = findViewById(R.id.usersListView);
        userDataList = new ArrayList<>();

        usersAdapter = new SearchUserListAdapter(this, userDataList);

        friendsManager.getAllUsersInDB();

        for (UserInfo user : userDataList) {
            Log.d("Sample", user.getUserName());
        }
        usersList.setAdapter(usersAdapter);

        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noUsersFoundMessage.setVisibility(View.INVISIBLE);
                updateUsersList();
                if (userDataList.isEmpty()) {
                    noUsersFoundMessage.setVisibility(View.VISIBLE);
                }
            }
        });

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserInfo user = userDataList.get(i);
                Intent intent = new Intent(context, FriendProfileActivity.class);
                intent.putExtra("userID", user.getUserID());
                startActivity(intent);

            }
        });

    }

    private void searchUserFromInput() {
        String input = searchUserInput.getText().toString().trim();
        if (input.isEmpty()) {
            return;
        } else {
            ArrayList<UserInfo> temp = new ArrayList<>(userDataList);
            for (UserInfo user : temp) {
                if (!input.equalsIgnoreCase(user.getUserName())) {
                    userDataList.remove(user);
                }
            }
        }
    }

    private void updateUsersList() {
        // If search input is given then narrow down userNameDataList
        searchUserFromInput();
        usersList.setAdapter(usersAdapter);
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
        friendsManager.removeFollower(targetUserId, currentUserID);
    }

    public boolean checkIfUserInOutgoingFriendRequest(UserInfo user) {
        ArrayList currentUserOutgoingFriendRequests = friendsManager.getOutgoingFriendRequestList(user.getUserID());

        if (currentUserOutgoingFriendRequests != null) {
            if (currentUserOutgoingFriendRequests.size() > 0) {
                if (currentUserOutgoingFriendRequests.contains(user.getUserID())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean checkIfUserIsInFollowing(UserInfo user) {
        ArrayList currentUserFollowings = friendsManager.getFollowingList(user.getUserID());

        if (currentUserFollowings != null) {
            if (currentUserFollowings.size() > 0) {
                if (currentUserFollowings.contains(user.getUserID())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

}
