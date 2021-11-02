/** Copyright 2021 Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code HabitListAdapter} class
 * To enable the list view of habits with checkbox
 */

package com.example.habtrack;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class FirestoreManager {
    private FirebaseDatabase db;
    final String TAG = "Sample";

    /**
     * This variable contains a unique identifier for the user.
     * This variable is of type {@link String}.
     */
    private String userId;

    /**
     * This variable contains a {@link HashMap} of habitId : Habit.
     * This variable is a local cache of all habits.
     */
    private HashMap<String, Habit> habitCacheMap = new HashMap<>();

    /**
     * This variable contains a {@link ArrayList} of {@link Habit}s.
     * This variable is a local cache of all habits.
     */
    private ArrayList<Habit> habitCacheList = new ArrayList<>();

    /**
     * This variable contains a {@link FirestoreManager.OnDataChangeListener}
     */
    private FirestoreManager.OnDataChangeListener listener;

    /**
     * This variable contains a single instance of the {@link FirestoreManager}
     */
    private static FirestoreManager instance;

    /**
     * This variable contains a {@link UserPreference}
     */
    private UserPreference preference = new UserPreference();

    /**
     * The {@link OnDataChangeListener} interface
     */
    public interface OnDataChangeListener {
        void onDataChange();
    }

    /**
     * Constructs a {@link FirestoreManager} of a given user
     * @param userId the unique identifier of the user
     */
    private FirestoreManager(String userId) {
        this.userId = userId;
        this.db = FirebaseDatabase.getInstance();
//        this.db = FirebaseFirestore.getInstance();
        initRanking();
    }

    /**
     * Gets the current instance of {@link FirestoreManager}
     * Creates a new one if the current instance is not initialized
     * @param userId the unique identifier of the user
     * @return a {@link FirestoreManager} object
     */
    public static FirestoreManager getInstance(String userId) {
        if (FirestoreManager.instance == null)
            FirestoreManager.instance = new FirestoreManager(userId);

        return FirestoreManager.instance;
    }

    /**
     * Sets the listener
     * @param listener the {@link FirestoreManager.OnDataChangeListener}
     */
    public void setOnDataChangeListener(FirestoreManager.OnDataChangeListener listener){
        this.listener = listener;
    }

    /**
     * Initializes the {@link ValueEventListener}
     * Pull data from firestore to fill {@link FirestoreManager#habitCacheMap}
     * and {@link FirestoreManager#habitCacheList}
     */
    private void initListener(){
        db.getReference("Users")
                .child(userId)
                .child("habit")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        habitCacheMap.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Habit habit = snapshot.getValue(Habit.class);
                            habitCacheMap.put(habit.getId(), habit);
                        }
                        if (preference.getHabitRanking().size() != habitCacheMap.size())
                            return;

                        habitCacheList.clear();
                        for (String item : preference.getHabitRanking())
                            habitCacheList.add(habitCacheMap.get(item));
                        if (listener != null)
                            listener.onDataChange();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//        db = FirebaseFirestore.getInstance();
//        final CollectionReference collectionReference = db.collection("Habits");
//
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//                    FirebaseFirestoreException error) {
//                dataList.clear();
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    //Log.d(TAG, String.valueOf(doc.getData().get("Habit")));
//                    Habit habit = doc.toObject(Habit.class);
//                    dataList.add(habit); // Adding the habit from FireStore
//                }
//                habitAdapter.notifyDataSetChanged();
//            }
//        });
    }

    /**
     * Returns the list of {@link Habit}s sorted by user preference ranking
     * @return habits of type {@link ArrayList}
     */
    public ArrayList<Habit> getHabits() {
        return habitCacheList;
    }

    /**
     * Checks whether a {@link Habit} exists
     * @param habit of type {@link Habit}
     * @return true if the {@link Habit} exists, false otherwise
     */
    public boolean hasHabit(Habit habit) {
        return habitCacheMap.containsKey(habit.getId());
    }

    /**
     * Adds a new {@link Habit} to firestore
     * @param newHabit of type {@link Habit}
     */
    public void addHabit(Habit newHabit) {
        addRanking(newHabit.getId()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    db.getReference("Users")
                            .child(userId)
                            .child("habit")
                            .child(newHabit.getId())
                            .setValue(newHabit);
                } else {
                    return;
                }
            }
        });

//        final CollectionReference collectionReference = db.collection("Habits");
//        final String habitTitle = newHabit.getTitle();
//
//        collectionReference
//                .document(habitTitle)
//                .set(newHabit)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // These are a method which gets executed when the task is succeeded
//                        Log.d(TAG, "Data has been added successfully!");
//                        Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // These are a method which gets executed if there’s any problem
//                        Log.d(TAG, "Data could not be added!" + e.toString());
//                        Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    /**
     * Removes an existing {@link Habit} from firestore
     * @param selectedHabit of type {@link Habit}
     */
    public void deleteHabit(Habit selectedHabit) {
        deleteRanking(selectedHabit.getId()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    db.getReference("Users")
                            .child(userId)
                            .child("habit")
                            .child(selectedHabit.getId())
                            .removeValue();
                } else {
                    return;
                }
            }
        });
    }

    /**
     * Edits an existing {@link Habit} from firestore
     * @param selectedHabit of type {@link Habit}
     * @param updatedHabit of type {@link Habit}
     */
    public void editHabit(Habit selectedHabit, Habit updatedHabit) {
        selectedHabit.setTitle(updatedHabit.getTitle());
        selectedHabit.setReason(updatedHabit.getReason());
        selectedHabit.setPlan(updatedHabit.getPlan());

        db.getReference("Users")
                .child(userId)
                .child("habit")
                .child(selectedHabit.getId())
                .setValue(selectedHabit);
    }

    /**
     * Initializes the user defined preference ranking (habit order)
     * Pull data from firestore to fill {@link FirestoreManager#preference}
     */
    public void initRanking() {
        db.getReference("Users")
                .child(userId)
                .child("preference")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            DataSnapshot snapshot = (DataSnapshot) task.getResult();
                            UserPreference p = snapshot.getValue(UserPreference.class);
                            if (p != null)
                                preference.setHabitRanking(p.getHabitRanking());
                            initListener();
                        }
                    }
                });
    }

    /**
     * Appends a habitId to the preference ranking in firestore
     * @param habitId of type {@link String}
     * @return a task object containing information about the status of the database access
     */
    public Task<Void> addRanking(String habitId) {
        preference.addRanking(habitId);
        Task<Void> task = db.getReference("Users")
                .child(userId)
                .child("preference")
                .setValue(preference);
        return task;
    }

    /**
     * Removes a habitId from the preference ranking in firestore
     * @param habitId of type {@link String}
     * @return a task object containing information about the status of the database access
     */
    public Task<Void> deleteRanking(String habitId) {
        preference.deleteRanking(habitId);
        Task<Void> task = db.getReference("Users")
                .child(userId)
                .child("preference")
                .setValue(preference);
        return task;
    }

    /**
     * Removes a habitId from the preference ranking in firestore
     * @param position of type int
     * @return a task object containing information about the status of the database access
     */
    public void deleteRanking(int position) {
//        loadRanking();
        preference.deleteRanking(position);
        db.getReference("Users")
                .child(userId)
                .child("preference")
                .setValue(preference);
    }

    /**
     * Reorders the preference ranking in firestore
     * @param fromPosition the initial position of the impacted item
     * @param toPosition the final position of the impacted item
     */
    public void swapRanking(int fromPosition, int toPosition) {
        preference.swapRanking(fromPosition, toPosition);
        db.getReference("Users")
                .child(userId)
                .child("preference")
                .setValue(preference);
        return;
    }


    /**
     * Returns a {@link Habit} with its unique identifier
     * @param habitId of type {@link String}
     * @return a {@link Habit}
     */
    public Habit getHabit(String habitId) {
        return habitCacheMap.get(habitId);
    }

}
