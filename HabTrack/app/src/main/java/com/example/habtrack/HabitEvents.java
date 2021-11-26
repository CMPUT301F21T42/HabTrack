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


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class stores the all the information of a habit event
 */
public class HabitEvents implements Serializable {


    private String title;   // Storing the title of HabitEvent
    private String comment; // Storing the user comment on HabitEvent

//    private Boolean Photo;
//    private ImageData Photo;
    private String Photo;
    private Boolean location;
    String timeStamp;
    String HabitEventID;

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
     * @param habitTitle
     * @param comment
     * @param Photo
     * @param location
     */
    public HabitEvents(String habitTitle, String comment, String Photo,
                       Boolean location, String timeStamp) {



        this.title = habitTitle;
        this.comment = comment;
        this.Photo = Photo;
        this.location = location;
        this.timeStamp = timeStamp;
        this.HabitEventID = title+timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Boolean getLocation() {
        return location;
    }

    public void setLocation(Boolean location) {
        this.location = location;
    }
}