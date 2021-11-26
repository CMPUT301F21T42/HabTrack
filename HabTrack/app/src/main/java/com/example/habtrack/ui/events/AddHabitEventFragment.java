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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.habtrack.Habit;
import com.example.habtrack.HabitEvents;
import com.example.habtrack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;

/**
 * This class contains functionality for adding an event fro a completed habit
 * It allows the user to log the event and add an optional comment with it
 */

public class AddHabitEventFragment extends DialogFragment {
    // TODO: Need to set this title to the habit title finished
    private EditText title;     // To set the title for HabitEvent
    private EditText comment;   // To get the Users comment on HabitEvent
    private ImageButton imageButton;
    private Habit habit;
    private String photoGraph = null;

    public  AddHabitEventFragment(Habit habit){
        this.habit = habit;
    }

    public void setphotoGraph(String photoGraph) { this.photoGraph = photoGraph; }

    public void addToHabitEventClass(DataSnapshot snapshot) {
        HabitEvents newHabitEvent = (HabitEvents) snapshot.getValue();
//        eventAdapter.add(newHabitEvent);
    }


    public void GotImage(String photoGraph) {
        setphotoGraph(photoGraph);
    }

    /**
     * This method saves the data added by the user to the firebase
     * @param habitEventTitle
     * @param comment
     * @param photo
     * @param location
     */
    public void onOkPressed(String habitEventTitle, String comment, String photo, Boolean location){


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

        HabTrackDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("HabitEvents")
                .document(HabitEventId).set(new HabitEvents(habitEventTitle, comment, photo, location, timestamp));

        Log.d("Firestore", "new HabitEvent is added to FireStore");
    }

    /**
     * This method creates the dialogue for adding a habit event.
     * It displays the habit title and stores the comment entered by the user.
     * @param savedInstanceState: Bundle
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_habit_event, null);
        title = view.findViewById(R.id.habit_event_title);
        comment = view.findViewById(R.id.habit_event_comment);
        imageButton = view.findViewById(R.id.OpenCamera);

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();

                        Bitmap someImage = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        someImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] ImageArr = stream.toByteArray();


                        String ImgString = new String(ImageArr, StandardCharsets.UTF_8);

                        setphotoGraph(ImgString);
                    }
                }
        );

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_Camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                someActivityResultLauncher.launch(open_Camera);
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
                .setView(view)
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

                        if (user_comment.length() > 30) {
                            Toast.makeText(getContext(),
                                    "Comment should be less than 30 characters", Toast.LENGTH_SHORT).show();
                        }
                        else {
//                            HabitEvents newHabitEvent = new HabitEvents(bundle.getParcelable("HabitDone"),
//                                    user_comment, false, false);

                            onOkPressed(habit.getTitle(),
                                    user_comment, photoGraph, false);
                        }

                    }
                }).create();
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        this.photoGraph = (Bitmap) data.getExtras().get("data");
//    }

}
