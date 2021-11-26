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
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;

public class SearchUsersActivity extends AppCompatActivity {

    Context context = this;

    EditText searchUserInput;
    Button searchUser;
    TextView noUsersFoundMessage;

    ListView usersList;
    ArrayAdapter usersAdapter;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("Users");
    DocumentReference documentReference = db.document("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

    ArrayList<UserInfo> userDataList;
    ArrayList currentUserOutgoingFriendRequests;
    ArrayList currentUserFollowings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        searchUserInput = findViewById(R.id.searchFriendInput);
        searchUser = findViewById(R.id.searchFriendBtn);
        noUsersFoundMessage = findViewById(R.id.noUsersFoundMessage);

        usersList = findViewById(R.id.usersListView);
        userDataList = new ArrayList<>();
        currentUserOutgoingFriendRequests = new ArrayList<>();
        currentUserFollowings = new ArrayList();

        usersAdapter = new SearchUserListAdapter(this, userDataList);
        usersList.setAdapter(usersAdapter);

        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        noUsersFoundMessage.setVisibility(View.INVISIBLE);
                        userDataList.clear();
                        for (QueryDocumentSnapshot doc: value) {
                            UserInfo user = new UserInfo();
                            user.setEmail(String.valueOf(doc.getData().get("email")));
                            user.setUserName(String.valueOf(doc.getData().get("userName")));
                            user.setUserID(doc.getId());
                            userDataList.add(user);
                        }
                        updateUsersList();
                        if (userDataList.isEmpty()) {
                            noUsersFoundMessage.setVisibility(View.VISIBLE);
                        }
                    }
                });
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
        collectionReference
                .document(mAuth.getCurrentUser().getUid())
                .update("outgoingFriendRequest", FieldValue.arrayUnion(targetUserId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Requested to follow " + targetUserId, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // Load friend request to target user id
        collectionReference
                .document(targetUserId)
                .update("incomingFriendRequest", FieldValue.arrayUnion(mAuth.getCurrentUser().getUid()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", "Started following " + mAuth.getCurrentUser().getUid());
                        } else {
                            Log.d("Sample", task.getException().getMessage());
                        }
                    }
                });
    }

    public void unSendFriendRequest(String targetUserId) {
        // Remove friend request from outgoing friend request list
        collectionReference
                .document(mAuth.getCurrentUser().getUid())
                .update("outgoingFriendRequest", FieldValue.arrayRemove(targetUserId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Unsend friend request " + targetUserId, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // Remove incoming friend request from target user id
        collectionReference
                .document(targetUserId)
                .update("incomingFriendRequest", FieldValue.arrayRemove(mAuth.getCurrentUser().getUid()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", "Removed from incoming friend request list " + mAuth.getCurrentUser().getUid());
                        } else {
                            Log.d("Sample", task.getException().getMessage());
                        }
                    }
                });
    }

    public void unfollow(String targetUserId) {
        collectionReference
                .document(targetUserId)
                .update("follower", FieldValue.arrayRemove(mAuth.getCurrentUser().getUid()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", targetUserId + " has removed you from its follower list");
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // Remove user from incoming friend request list
        collectionReference
                .document(mAuth.getCurrentUser().getUid())
                .update("following", FieldValue.arrayRemove(targetUserId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", targetUserId + " removed from incoming friend requests");
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public boolean checkIfUserInOutgoingFriendRequest(UserInfo user) {
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                currentUserOutgoingFriendRequests = (ArrayList) value.getData().get("outgoingFriendRequest");
            }
        });

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
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                currentUserFollowings = (ArrayList) value.getData().get("following");
            }
        });

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
