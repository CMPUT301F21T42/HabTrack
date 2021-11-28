package com.example.habtrack;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
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
import java.util.Collections;
import java.util.Date;

public class FriendsManager {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userCollection = db.collection("Users");

    private static String userID;

    private ArrayList<UserInfo> allUsersList = new ArrayList<>();
    private ArrayList<UserInfo> followingList = new ArrayList<>();
    private ArrayList<UserInfo> followerList = new ArrayList<>();
    private ArrayList<String> incomingFriendRequestList = new ArrayList<>();
    private ArrayList<String> outgoingFriendRequestList= new ArrayList<>();

    private ArrayList<UserInfo> notificationList = new ArrayList<>();
    private ArrayList<Habit> habitList= new ArrayList<>();

    private int followersCount;
    private int followingsCount;
    private int habitsCount;


    private static FriendsManager instance;
    private OnDataChangeListener listener;

    public interface OnDataChangeListener {
        void onDataChange();
    }
    public void setOnDataChangeListener(FriendsManager.OnDataChangeListener listener){
        this.listener = listener;
    }

    public FriendsManager(String userID) {
        this.userID = userID;
        this.notificationList = new ArrayList<>();
        this.habitList = new ArrayList<>();
        // initListener();
    }

    public static FriendsManager getInstance(String userID) {
        if (FriendsManager.instance == null || !FriendsManager.userID.equals(userID)) {
            FriendsManager.instance = new FriendsManager(userID);
        }

        return FriendsManager.instance;
    }

//    public void initListener() {
//        userCollection.document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(Task task) {
//                        if (!task.isSuccessful()) {
//                            Log.e("firebase", "Error getting data", task.getException());
//                        }
//                        else {
//                            Log.d("firebase", String.valueOf(task.getResult()));
//
//                            DocumentReference userDocument = userCollection.document(userID);
//                            userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    followerList.clear();
//                                    followerList = (ArrayList) value.getData().get("follower");
//                                    followingList.clear();
//                                    followingList = (ArrayList) value.getData().get("following");
//                                    Log.d("friendManager2", String.valueOf(followingList.size()));
//                                    incomingFriendRequestList.clear();
//                                    incomingFriendRequestList = (ArrayList) value.getData().get("incomingFriendRequest");
//                                    outgoingFriendRequestList.clear();
//                                    outgoingFriendRequestList = (ArrayList) value.getData().get("outgoingFriendRequest");
//                                }
//                            });
//
//
//                            CollectionReference habitsCollection = userDocument.collection("Habits");
//                            userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    ArrayList targetUserFollowers = getFollowerList();
//                                    if (targetUserFollowers != null) {
//                                        if (targetUserFollowers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid()) || userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                                            Log.d("Sample", "in");
//                                            habitsCount = targetUserFollowers.size();
//                                            habitsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                                @Override
//                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                                    for (QueryDocumentSnapshot doc : value) {
//                                                        Habit habit = new Habit();
//                                                        habit.setTitle(String.valueOf(doc.getData().get("title")));
////                                    habit.setProgress(Integer.parseInt(doc.getData().get("progress").toString()));
////                                    habit.setIsPublic(Boolean.parseBoolean(doc.getData().get("isPublic").toString()));
////                                    // Only public habits are visible
////                                    if (habit.getIsPublic()) {
////                                        habitDataList.add(habit);
////                                    }
//                                                        habitList.add(habit);
//                                                    }
//
////                                                    if (listener != null) {
////                                                        listener.onDataChange();
////                                                    }
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
//                            });
//
//
//                            userCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    notificationList.clear();
//                                    for (QueryDocumentSnapshot doc: value) {
//                                        if (incomingFriendRequestList.contains(doc.getId())) {
//                                            UserInfo user = new UserInfo();
//                                            user.setUserName(String.valueOf(doc.getData().get("userName")));
//                                            user.setEmail(String.valueOf(doc.getData().get("email")));
//                                            user.setUserID(doc.getId());
//                                            notificationList.add(user);
//                                        }
//                                    }
//
////                                    if (listener != null) {
////                                        listener.onDataChange();
////                                    }
//                                }
//                            });
//
//
//                            userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    ArrayList targetUserFollowers = (ArrayList) value.getData().get("follower");
//                                    if (targetUserFollowers != null) {
//                                        followersCount = targetUserFollowers.size();
//                                        Log.d("Sample", String.valueOf(followersCount));
//                                    }
//                                    ArrayList targetUserFollowings = (ArrayList) value.getData().get("following");
//                                    if (targetUserFollowings != null) {
//                                        followingsCount = targetUserFollowers.size();
//                                        Log.d("Sample", String.valueOf(followersCount));
//                                    }
//
////                                    if (listener != null) {
////                                        listener.onDataChange();
////                                    }
//                                }
//                            });
//
//                            if (listener != null) {
//                                listener.onDataChange();
//                            }
//
//                        }
//                    }
//                });
//    }





    //    public ArrayList<UserInfo> getAllUsersInDB() {
//        Log.d("friendsManager1", String.valueOf(allUsersList.size()));
//        return allUsersList;
//    }
//
//
//
//
//    public ArrayList<UserInfo> getFollowerList(String userID) {
//        DocumentReference userDocument = userCollection.document(userID);
//        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                followerList = (ArrayList) value.getData().get("followers");
//            }
//        });
//
//        return followerList;
//    }
//
//    public ArrayList<UserInfo> getFollowingList(String userID) {
//        DocumentReference userDocument = userCollection.document(userID);
//        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                followingList = (ArrayList) value.getData().get("following");
//            }
//        });
//
//        return followingList;
//    }
//
//    public ArrayList<String> getIncomingFriendRequestList(String userID) {
//        DocumentReference userDocument = userCollection.document(userID);
//        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                incomingFriendRequestList = (ArrayList) value.getData().get("incomingFriendRequest");
//            }
//        });
//
//        return incomingFriendRequestList;
//    }
//
//    public ArrayList<String> getOutgoingFriendRequestList(String userID) {
//        DocumentReference userDocument = userCollection.document(userID);
//        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                outgoingFriendRequestList = (ArrayList) value.getData().get("outgoingFriendRequest");
//            }
//        });
//
//        return outgoingFriendRequestList;
//    }
//
//    public ArrayList<Habit> getUserHabits(String userID) {
//        DocumentReference userDocument = userCollection.document(userID);
//        CollectionReference habitsCollection = userDocument.collection("Habits");
//        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                ArrayList targetUserFollowers = getFollowerList(userID);
//                if (targetUserFollowers != null || userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                    if (targetUserFollowers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid()) || userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                        Log.d("Sample", "in");
//                        habitsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                for (QueryDocumentSnapshot doc : value) {
//                                    Habit habit = new Habit();
//                                    habit.setTitle(String.valueOf(doc.getData().get("title")));
////                                    habit.setProgress(Integer.parseInt(doc.getData().get("progress").toString()));
////                                    habit.setIsPublic(Boolean.parseBoolean(doc.getData().get("isPublic").toString()));
////                                    // Only public habits are visible
////                                    if (habit.getIsPublic()) {
////                                        habitDataList.add(habit);
////                                    }
//                                    habitList.add(habit);
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//        });
//        return habitList;
//    }
//
//    public ArrayList<UserInfo> getNotifications(String userID) {
//        userCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                notificationList.clear();
//                for (QueryDocumentSnapshot doc: value) {
//                    if (incomingFriendRequestList.contains(doc.getId())) {
//                        UserInfo user = new UserInfo();
//                        user.setUserName(String.valueOf(doc.getData().get("userName")));
//                        user.setEmail(String.valueOf(doc.getData().get("email")));
//                        user.setUserID(doc.getId());
//                        notificationList.add(user);
//                    }
//                }
//            }
//        });
//
//        return notificationList;
//    }
//
//    public int getFollowerCount(String userID) {
//        DocumentReference documentReference = userCollection.document(userID);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                ArrayList targetUserFollowers = (ArrayList) value.getData().get("follower");
//                if (targetUserFollowers != null) {
//                    followersCount = targetUserFollowers.size();
//                    Log.d("Sample", String.valueOf(followersCount));
//                }
//
//            }
//        });
//
//        return followersCount;
//    }
//
//    public int getFollowingCount(String userID) {
//        DocumentReference documentReference = userCollection.document(userID);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                ArrayList targetUserFollowers = (ArrayList) value.getData().get("following");
//                if (targetUserFollowers != null) {
//                    followingsCount = targetUserFollowers.size();
//                    Log.d("Sample", String.valueOf(followersCount));
//                }
//
//            }
//        });
//
//        return followingsCount;
//    }
//
//    public int getHabitCount(String userID) {
//        CollectionReference habitsCollection = db.collection("Users").document(userID).collection("Habits");
//        habitsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                ArrayList targetUserFollowers = getFollowingList(userID);
//                if (targetUserFollowers != null || userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                    if (targetUserFollowers.contains(FirebaseAuth.getInstance().getCurrentUser().getUid()) || userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
//                        habitsCount = targetUserFollowers.size();
//                        Log.d("Sample", String.valueOf(followersCount));
//                    }
//                }
//            }
//        });
//
//        return habitsCount;
//    }

//    public UserInfo getUserInfoObjectFromUserID(String userID) {
//
//        for (UserInfo user : temp) {
//            if (userID.equals(user.getUserID()));
//                return user;
//        }
//        return null;
//    }



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
                .update("following", FieldValue.arrayRemove(userID))
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
                .update("follower", FieldValue.arrayUnion(userID))
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
