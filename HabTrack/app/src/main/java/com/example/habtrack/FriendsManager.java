/*
 * FriendsManager
 *
 * FriendsManager class handles friend requests and followers between two users. It has an
 * aggregation relationship where the FriendProfileActivity, where FriendProfileActivity will create
 * an instance of a FriendsManager class if one does not exist, else it uses the created instance. This class has methods that
 * can add/ remove outgoing follow requests, grants/ denys incoming follow requests, also removes/
 * adds followers and remove/adds followings. All these methods take two userID's as parameters to
 * reflect that all the methods have a current userID and targer userID.
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

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class uses the singleton design pattern, meaning at most only one instance of the class
 * will exist. This class is created by the FriendsProfileAcitivity and handles all the interactions
 * between two users pertaining to following/ follwers.
 *
 * @author Jenish
 * @see FriendProfileActivity
 * @version 1.0
 * @since 1.0
 */
public class FriendsManager {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollection = db.collection("Users");

    private static String userID;

    private static FriendsManager instance;

    public FriendsManager(String userID) {
        this.userID = userID;
    }

    public static FriendsManager getInstance(String userID) {
        if (FriendsManager.instance == null || !FriendsManager.userID.equals(userID)) {
            FriendsManager.instance = new FriendsManager(userID);
        }

        return FriendsManager.instance;
    }

    public void addFollower(String userID, String targetUserID) {
        userCollection
                .document(targetUserID)
                .update("follower", FieldValue.arrayUnion(userID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", userID + " added to followers");
                        } else {
                            Log.d("Sample", task.getException().getMessage());
                        }
                    }
                });
    }

    public void removeFollower(String userID, String targetUserID) {
        userCollection
                .document(targetUserID)
                .update("follower", FieldValue.arrayRemove(userID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", userID + " removed from followers");
                        } else {
                            Log.d("Sample", task.getException().getMessage());
                        }
                    }
                });
    }

    public void addFollowing(String userID, String targetUserID) {
        userCollection
                .document(targetUserID)
                .update("following", FieldValue.arrayUnion(userID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", userID + " added to followings");
                        } else {
                            Log.d("Sample", task.getException().getMessage());
                        }
                    }
                });
    }

    public void removeFollowing(String userID, String targetUserID) {
        userCollection
                .document(targetUserID)
                .update("following", FieldValue.arrayRemove(userID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", userID + " removed from followings");
                        } else {
                            Log.d("Sample", task.getException().getMessage());
                        }
                    }
                });
    }

    public void addIncomingFriendRequest(String userID, String targetUserID) {
        userCollection
                .document(targetUserID)
                .update("incomingFriendRequest", FieldValue.arrayUnion(userID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", userID + " added to incoming friend requests");
                        } else {
                            Log.d("Sample", task.getException().getMessage());
                        }
                    }
                });
    }

    public void removeIncomingFriendRequest(String userID, String targetUserID) {
        userCollection
                .document(targetUserID)
                .update("incomingFriendRequest", FieldValue.arrayRemove(userID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", userID + " removed from incoming friend requests");
                        } else {
                            Log.d("Sample", task.getException().getMessage());
                        }
                    }
                });
    }

    public void addOutgoingFriendRequest(String userID, String targetUserID) {
        userCollection
                .document(targetUserID)
                .update("outgoingFriendRequest", FieldValue.arrayUnion(userID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", userID + " added to incoming friend requests");
                        } else {
                            Log.d("Sample", task.getException().getMessage());
                        }
                    }
                });
    }

    public void removeOutgoingFriendRequest(String userID, String targetUserID) {
        userCollection
                .document(targetUserID)
                .update("outgoingFriendRequest", FieldValue.arrayRemove(userID))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sample", userID + " removed from incoming friend requests");
                        } else {
                            Log.d("Sample", task.getException().getMessage());
                        }
                    }
                });
    }

}
