/** Copyright 2021 Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code HabitHandler} class
 */

package com.example.habtrack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class represents a habit handler that includes various methods
 * to check if a habit attribute is legal
 */
public class HabitHandler {

    /**
     * Checks whether the given {@link String} is a legal attribute for {@link Habit} title
     * @param title the title {@link String}
     * @return true if the {@link String} argument has up to 20 characters and is not empty;
     * false otherwise
     */
    public boolean isLegalTitle(String title) {
        return title.trim().length() > 0 && title.trim().length() <= 20;
    }

    /**
     * Checks whether the given {@link String} is a legal attribute for {@link Habit} reason
     * @param reason the reason {@link String}
     * @return true if the {@link String} argument has up to 30 characters;
     * false otherwise
     */
    public boolean isLegalReason(String reason) {
        return reason.trim().length() <= 30;
    }

    /**
     * Checks whether the given {@link String} can be parsed into a valid Date object
     * @param date the date {@link String}
     * @return true if the {@link String} argument can be parsed; false otherwise
     */
    public boolean isLegalDate(String date) {
        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        inFormat.setLenient(false);
        try {
            inFormat.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
