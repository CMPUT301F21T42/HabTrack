/*
 * SearchUsersActivity
 *
 * SearchUsersActivity is the activity that centers around searching for other users to send follow
 * requests to.
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import org.w3c.dom.Document;

import java.util.ArrayList;

/**
 * This class contains onClick listening events for the search button that fetches a list of users
 * that matches the entered search criteria. As well as a listener for the list of users itself that
 * will change the activity to the FriendProfileActivity of the selected user. It also contains
 * methods to update the user list and search the user list for a given input.
 *
 * @author Jenish
 * @see FriendProfileActivity
 * @version 1.0
 * @since 1.0
 */
public class SearchUsersActivity extends AppCompatActivity {

    Context context = this;

    EditText searchUserInput;
    Button searchUser;
    TextView noUsersFoundMessage;

    ListView usersList;
    ArrayAdapter usersAdapter;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUserID = mAuth.getCurrentUser().getUid();
    FriendsManager friendsManager = FriendsManager.getInstance(currentUserID);

    CollectionReference userCollection = FirebaseFirestore.getInstance().collection("Users");

    ArrayList<UserInfo> userDataList;

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
        setContentView(R.layout.activity_search_users);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchUserInput = findViewById(R.id.searchFriendInput);
        searchUser = findViewById(R.id.searchFriendBtn);
        noUsersFoundMessage = findViewById(R.id.noUsersFoundMessage);

        usersList = findViewById(R.id.usersListView);
        userDataList = new ArrayList<>();

        userCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                userDataList.clear();
                for (QueryDocumentSnapshot doc: value) {
                    UserInfo user = new UserInfo();
                    user.setEmail(String.valueOf(doc.getData().get("email")));
                    user.setUserName(String.valueOf(doc.getData().get("userName")));
                    user.setUserID(doc.getId());
                    userDataList.add(user);
                }

                Log.d("friendsManager", String.valueOf(userDataList.size()));
            }
        });

        usersAdapter = new SearchUserListAdapter(this, userDataList);
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

    private void refreshUserList() {
        usersList.setAdapter(usersAdapter);
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

}