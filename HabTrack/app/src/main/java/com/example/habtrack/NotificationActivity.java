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

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    Context context = this;

    ListView notificationsList;
    TextView noPendingFriendRequests;
    ArrayAdapter notificationsAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("Users");
    DocumentReference documentReference = db.document("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

    ArrayList incomingFriendRequests;
    ArrayList<UserInfo> notificationsDataList;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        noPendingFriendRequests = findViewById(R.id.no_pending_requests);
        notificationsList = findViewById(R.id.notifications_list_view);
        notificationsDataList = new ArrayList<>();
        notificationsAdapter = new NotificationListAdapter((NotificationActivity) context, notificationsDataList);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                incomingFriendRequests = (ArrayList) value.getData().get("incomingFriendRequest");
                if (incomingFriendRequests != null) {
                    if (incomingFriendRequests.size() > 0) {
                        noPendingFriendRequests.setVisibility(View.INVISIBLE);
                        obtainFriendInfoFromUserID();
                    } else {
                        Log.d("Sample", "No Notifications");
                        noPendingFriendRequests.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d("Error", "array is null");
                }

            }
        });

//        notificationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                int userIndex = i;
//
//                new AlertDialog.Builder(context)
//                        .setTitle("User")
//                        .setMessage("Do you want to accept follow request from " + notificationsDataList.get(i).getEmail() + " ?")
//                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Log.d("Sample", "Success accept");
//                                acceptFriendRequest(notificationsDataList.get(userIndex));
//                            }
//                        })
//                        .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Log.d("Sample", "Success deny");
//                                denyFriendRequest(notificationsDataList.get(userIndex));
//                            }
//                        })
//                        .show();
//            }
//        });

    }

    public void obtainFriendInfoFromUserID() {
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                notificationsDataList.clear();
                for (QueryDocumentSnapshot doc: value) {
                    if (incomingFriendRequests.contains(doc.getId())) {
                        UserInfo friend = new UserInfo();
                        friend.setUserName(String.valueOf(doc.getData().get("userName")));
                        friend.setEmail(String.valueOf(doc.getData().get("email")));
                        friend.setUserID(doc.getId());
                        notificationsDataList.add(friend);
                    }
                }
                notificationsList.setAdapter(notificationsAdapter);
            }
        });
    }

    public void acceptFriendRequest(UserInfo user) {
        // Add new user to followers
        collectionReference
                .document(mAuth.getCurrentUser().getUid())
                .update("follower", FieldValue.arrayUnion(user.getUserID()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", user.getUserName() + " is a friend");
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // Remove user from incoming friend request list
        collectionReference
                .document(mAuth.getCurrentUser().getUid())
                .update("incomingFriendRequest", FieldValue.arrayRemove(user.getUserID()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", user.getUserName() + " removed from incoming friend requests");
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // Remove current user id from the other user's outgoing friend request list
        collectionReference
                .document(user.getUserID())
                .update("outgoingFriendRequest", FieldValue.arrayRemove(mAuth.getCurrentUser().getUid()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", mAuth.getCurrentUser().getUid() + " removed from outgoing friend requests");
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void denyFriendRequest(UserInfo user) {
        // Remove user from incoming friend request
        collectionReference
                .document(mAuth.getCurrentUser().getUid())
                .update("incomingFriendRequest", FieldValue.arrayRemove(user.getUserID()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", user.getUserName() + " removed from incoming friend requests");
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // Remove current user id from the other user's outgoing friend request list
        collectionReference
                .document(user.getUserID())
                .update("outgoingFriendRequest", FieldValue.arrayRemove(mAuth.getCurrentUser().getUid()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", mAuth.getCurrentUser().getUid() + " removed from outgoing friend requests");
                        } else {
                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(context, MainActivity.class));
    }
}