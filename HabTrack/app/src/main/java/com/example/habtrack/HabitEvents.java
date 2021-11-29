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
package com.example.habtrack;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class stores the all the information of a habit event
 */
public class HabitEvents implements Serializable {

    private String habitId;   // Storing the title of HabitEvent
    private String comment; // Storing the user comment on HabitEvent
    private String Photo = "DefaultValue";
    private ArrayList<Double> location;
    private String timeStamp;
    private String HabitEventID;
    private FirestoreManager firestoreManager;

    public HabitEvents() {}

    public String getHabitEventID() {
        return HabitEventID;
    }

    public void setHabitEventID(String HabitEventID) {
        this.HabitEventID = HabitEventID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * This constructor is used to initialise the attributes
     * @param habitId
     * @param comment
     * @param Photo
     * @param location
     */
    public HabitEvents(String habitId, String comment, String Photo,
                       @Nullable ArrayList<Double> location, String timeStamp) {

        this.habitId = habitId;
        this.comment = comment;
        this.Photo = Photo;
        this.location = location;
        this.timeStamp = timeStamp;
        this.HabitEventID = habitId+timeStamp;
    }

    public String getHabitId() {
        return habitId;
    }

    public String getTitle() {
        return FirestoreManager.getInstance(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .getHabit(getHabitId()).getTitle();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        this.Photo = photo;
    }

    public ArrayList<Double> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<Double> location) {
        this.location = location;
    }
}