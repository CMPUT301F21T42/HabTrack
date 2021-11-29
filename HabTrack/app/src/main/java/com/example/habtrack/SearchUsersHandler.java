package com.example.habtrack;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchUsersHandler {

    final String TAG = "Sample";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    public ArrayList<String> usersDataList = new ArrayList<>();

    public SearchUsersHandler() {
    }

//    public ArrayList<String> searchAllFriends() {
//        usersDataList.clear();
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                for (QueryDocumentSnapshot doc: value) {
//                    String userName = String.valueOf(doc.getData().get("userName"));
//                    usersDataList.add(userName);
//                }
//            }
//        });
//
//    }
}
