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

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FirestoreManager {
    private FirebaseFirestore db;
    final String TAG = "Sample";

    /**
     * This variable contains a unique identifier for the user.
     * This variable is of type {@link String}.
     */
    public static String userId;

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
     * This variable contains a {@link UserPreference}.
     */
    private UserPreference preference = new UserPreference();

    /**
     * This variable contains the user's last app logon of type {@link Date}.
     */
    private Date lastLogon;

    /**
     * This variable contains a reference to the user firestore {@link DocumentReference}
     */
    private final DocumentReference userDocument;

    /**
     * This variable contains a reference to the habit firestore {@link CollectionReference}
     */
    private final CollectionReference habitCollection;

    /**
     * This variable contains a reference to the event firestore {@link CollectionReference}
     */
    private final CollectionReference eventCollection;

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
        FirestoreManager.userId = userId;
        this.db = FirebaseFirestore.getInstance();
        this.userDocument = db.collection("Users").document(userId);
        this.habitCollection = userDocument.collection("Habits");
        this.eventCollection = userDocument.collection("HabitEvents");
        initRanking();
    }

    /**
     * Gets the current instance of {@link FirestoreManager}
     * Creates a new one if the current instance is not initialized
     * or if the userId is updated with a new login
     * @param userId the unique identifier of the user
     * @return a {@link FirestoreManager} object
     */
    public static FirestoreManager getInstance(String userId) {
        if (FirestoreManager.instance == null || !FirestoreManager.userId.equals(userId))
            FirestoreManager.instance = new FirestoreManager(userId);

        return FirestoreManager.instance;
    }

    public static void setInstance(String userId, FirestoreManager instance) {
        FirestoreManager.userId = userId;
        FirestoreManager.instance = instance;
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
        habitCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException error) {
                habitCacheMap.clear();
                for (DocumentSnapshot doc: queryDocumentSnapshots) {
                    Habit habit = doc.toObject(Habit.class);
                    habitCacheMap.put(habit.getId(), habit);
                }

                if (preference.getHabitRanking().size() < habitCacheMap.size()) {
                    for (Habit habit : habitCacheMap.values()) {
                        if (!preference.getHabitRanking().contains(habit.getId()))
                            addRanking(habit.getId()); // if a habit has no ranking, add default ranking
                    }
                }

                habitCacheList.clear();
                for (String id : preference.getHabitRanking())
                    if(!habitCacheMap.containsKey(id)){
                        deleteRanking(id); // if a ranking doesn't correspond to any habit - remove it
                    } else {
                        habitCacheList.add(habitCacheMap.get(id));
                    }

                if (listener != null)
                    listener.onDataChange();
            }
        });
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
     * Checks whether there is a {@link Habit} with a given title
     * @param habitTitle title of type {@link String}
     * @return true if the {@link Habit} exists, false otherwise
     */
    public boolean hasHabit(String habitTitle) {
        for (Habit habit : habitCacheList)
            if (habit.getTitle() == habitTitle) return true;
        return false;
    }

    /**
     * Adds a new {@link Habit} to firestore
     * @param newHabit of type {@link Habit}
     */
    public void addHabit(Habit newHabit) {
        Task task = addRanking(newHabit.getId()); // first add a ranking

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                habitCollection
                        .document(newHabit.getId())
                        .set(newHabit)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // These are a method which gets executed when the task is succeeded
                                Log.d(TAG, "Data has been added successfully!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // These are a method which gets executed if there’s any problem
                                Log.d(TAG, "Data could not be added!" + e.toString());
                                // If ranking added successfully but habit failed to add, should remove ranking
                            }
                        });
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // These are a method which gets executed if there’s any problem
                Log.d(TAG, "Data could not be added!" + e.toString());
            }
        });
    }

    /**
     * Removes an existing {@link Habit} from firestore
     * @param selectedHabit of type {@link Habit}
     */
    public void deleteHabit(Habit selectedHabit) {
        Task task = deleteRanking(selectedHabit.getId()); // first delete a ranking

        eventCollection
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (QueryDocumentSnapshot hevent: value)  {
                            HabitEvents hEvent = hevent.toObject(HabitEvents.class);
                            if (hEvent.getHabitId().equals(selectedHabit.getId()))
                                eventCollection.document(hEvent.getHabitEventID()).delete();
                        }
                    }
                });

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                habitCollection
                        .document(selectedHabit.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // These are a method which gets executed when the task is succeeded
                                Log.d(TAG, "Data has been added successfully!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // These are a method which gets executed if there’s any problem
                                Log.d(TAG, "Data could not be added!" + e.toString());
                                // If ranking delete successfully but habit failed to delete, what to do here?
                            }
                        });
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // These are a method which gets executed if there’s any problem
                Log.d(TAG, "Data could not be added!" + e.toString());
            }
        });
    }

    /**
     * Edits an existing {@link Habit} from firestore
     * @param updatedHabit of type {@link Habit}
     */
    public void editHabit(Habit updatedHabit) {
        habitCollection.document(updatedHabit.getId()).set(updatedHabit);
    }

    /**
     * Initializes the user defined preference ranking (habit order)
     * Pull data from firestore to fill {@link FirestoreManager#preference}
     */
    private void initRanking() {
        userDocument
                .get()
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(Task task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult()));
                            DocumentSnapshot snapshot = (DocumentSnapshot) task.getResult();
                            UserPreference p = snapshot.get("preference", UserPreference.class);
                            if (p != null)
                                preference.setHabitRanking(p.getHabitRanking());
                            Date ll = snapshot.get("lastLogon", Date.class);
                            setLastLogon(ll == null ? new Date() : ll);
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
    private Task<Void> addRanking(String habitId) {
        preference.addRanking(habitId);
        return userDocument.update("preference", preference);
    }

    /**
     * Removes a habitId from the preference ranking in firestore
     * @param habitId of type {@link String}
     * @return a task object containing information about the status of the database access
     */
    private Task<Void> deleteRanking(String habitId) {
        preference.deleteRanking(habitId);
        return userDocument.update("preference", preference);
    }

    /**
     * Removes a habitId from the preference ranking in firestore
     * @param position of type int
     * @return a task object containing information about the status of the database access
     */
    private Task<Void> deleteRanking(int position) {
        String id = preference.getHabitRanking().get(position);
        return deleteRanking(id);
    }

    /**
     * Reorders the preference ranking in firestore
     * @param fromPosition the initial position of the impacted item
     * @param toPosition the final position of the impacted item
     */
    public Task<Void> swapRanking(int fromPosition, int toPosition) {
        preference.swapRanking(fromPosition, toPosition);
        return userDocument.update("preference", preference);
    }

    /**
     * Returns a {@link Habit} with its unique identifier
     * @param habitId of type {@link String}
     * @return a {@link Habit}
     */
    public Habit getHabit(String habitId) {
        return habitCacheMap.get(habitId);
    }

    /**
     * Returns the {@link FirestoreManager#lastLogon} of this {@link FirestoreManager} object
     * @return the user's last app logon of type {@link Date}
     */
    public Date getLastLogon() {
        return lastLogon;
    }

    /**
     * Sets the {@link FirestoreManager#lastLogon} of this {@link FirestoreManager} object
     * @param lastLogon of type {@link Date}
     */
    public void setLastLogon(Date lastLogon) {
        this.lastLogon = lastLogon;
        userDocument.update("lastLogon", lastLogon);
    }
}
