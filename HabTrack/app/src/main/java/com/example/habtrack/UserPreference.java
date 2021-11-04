/** Copyright 2021 Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code UserPreference} class
 */

package com.example.habtrack;

/**
 * This class represents a user preference
 */
import java.util.ArrayList;
import java.util.Collections;

public class UserPreference {

    /**
     * This variable contains a {@link ArrayList} of {@link String}s
     * that are keys (unique identifiers) used to retrieve {@link Habit}s
     * in a user defined order, resulted from the drag and drop action.
     */
    private ArrayList<String> habitRanking = new ArrayList<>();

    /**
     * Returns the user defined order of habits
     * @return the ordered {@link ArrayList} of keys
     */
    public ArrayList<String> getHabitRanking() {
        return habitRanking;
    }

    /**
     * Sets the default (chronological)/user defined order of habits
     * @param habitRanking
     */
    public void setHabitRanking(ArrayList<String> habitRanking) {
        this.habitRanking = habitRanking;
    }

    /**
     * Reorders the item responding to a user action
     * @param fromPosition the initial position of the impacted item
     * @param toPosition the final position of the impacted item
     */
    public void swapRanking(int fromPosition, int toPosition) {
        Collections.swap(habitRanking, fromPosition, toPosition);
    }

    /**
     * Updates the list when a new {@link Habit} is added
     * @param str the unique identifier of the new {@link Habit}
     */
    public void addRanking(String str) {
        habitRanking.add(str);
    }

    /**
     * Updates the list when a selected {@link Habit} is deleted
     * @param str the unique identifier of the {@link Habit}
     */
    public void deleteRanking(String str) {
        habitRanking.remove(str);
    }
    
    /**
     * Updates the list when a selected {@link Habit} is deleted
     * @param position the position of the {@link Habit}
     */
    public void deleteRanking(int position) {
        habitRanking.remove(position);
    }
}