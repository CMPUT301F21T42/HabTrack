/** Copyright 2021
 * Raunak Agarwal, Revanth Atmakuri, Mattheas Jamieson,
 * Jenish Patel, Jasmine Wadhwa, Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code Habit} class
 */

package com.example.habtrack.ui.events;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habtrack.Habit;
import com.example.habtrack.HabitEvents;
import com.example.habtrack.MapsActivity;
import com.example.habtrack.R;
import com.example.habtrack.databinding.FragmentAddHabitEventBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class contains functionality for adding an event fro a completed habit
 * It allows the user to log the event and add an optional comment with it
 */

public class AddHabitEventFragment extends DialogFragment {
    private FragmentAddHabitEventBinding binding;

    private ArrayList<Double> location = new ArrayList<>();

    // TODO: Need to set this title to the habit title finished
    private EditText title;     // To set the title for HabitEvent
    private EditText comment;   // To get the Users comment on HabitEvent
    private Habit habit;
    private TextView latlng;

    public  AddHabitEventFragment(Habit habit){
        this.habit = habit;
    }

    public void addToHabitEventClass(DataSnapshot snapshot) {
        HabitEvents newHabitEvent = (HabitEvents) snapshot.getValue();
//        eventAdapter.add(newHabitEvent);
    }

    /**
     * This method saves the data added by the user to the firebase
     * @param habitEventTitle
     * @param comment
     * @param photo
     */
    public void onOkPressed(String habitEventTitle, String comment, Boolean photo){


        FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();

        // If there is no object with HabitEvents for this User this would create new one
        CollectionReference HabitEvents = HabTrackDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("HabitEvents");

        // The Id for this new HabitEvent would be "Workout2021-09-29" if title for habit is "Workout"
        // and if the date user finished this habit is 2021/09/29
        String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String HabitEventId = habitEventTitle + timestamp;

        DocumentReference newHabitEvent = HabTrackDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("HabitEvents")
                .document(HabitEventId);

        HabitEvents he = new HabitEvents(habitEventTitle, comment, photo, location, timestamp);
//        HabitEvents he = new HabitEvents(habitEventTitle, comment, photo, null, timestamp);
        HabTrackDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("HabitEvents")
                .document(HabitEventId).set(he)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "new HabitEvent is added to FireStore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "NOT ADDED");
                    }
                });

    }

    /**
     * This method creates the dialogue for adding a habit event.
     * It displays the habit title and stores the comment entered by the user.
     * @param savedInstanceState: Bundle
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        binding = FragmentAddHabitEventBinding.inflate(LayoutInflater.from(getContext()));
        View root = binding.getRoot();

        title = binding.habitEventTitle;
        comment = binding.habitEventComment;

        latlng = binding.latlng;

        ActivityResultLauncher<Intent> locationGetContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            location.clear();
                            Double lat = data.getDoubleExtra("lat", 53.526681512750336);
                            Double lng = data.getDoubleExtra("lng", -113.52975698826533);
                            location.add(lat);
                            location.add(lng);
                            latlng.setText("(" + location.get(0) + ", " + location.get(1) + ")");
                        }
                    }
                });

        ImageButton ib = binding.imageButton;
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                locationGetContent.launch(intent);
            }
        });


        String habitTitle = "TempTitle";

        // This displays the current title of the habit
        if (habitTitle.length() > 0){
            title.setText(habitTitle);
            Log.d(TAG, "Got the habit Title");
        }
        title.setText(habit.getTitle());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(root)
                .setTitle("Congratulations")
                // Cancel button in case a user don't want to add this habit event

                // TODO: Need to setTitle to the activity title
                .setNegativeButton("Cancel", null)
                // Add button for user to add a new habit event
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // To get the user input for Comment
                        String user_comment = comment.getText().toString();

                        // TODO: Need to store all the habitevent information in an habitevent object

                        if (user_comment.length() > 30) {
                            Toast.makeText(getContext(),
                                    "Comment should be less than 30 characters", Toast.LENGTH_SHORT).show();
                        }
                        else {
//                            HabitEvents newHabitEvent = new HabitEvents(bundle.getParcelable("HabitDone"),
//                                    user_comment, false, false);
                            // TODO: need to store this object in customList

                            onOkPressed(habit.getTitle(),
                                    user_comment, false);

                        }

                    }
                }).create();
    }

}
