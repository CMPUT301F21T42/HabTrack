/** Copyright 2021 Wendy Zhang
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class represents a habit
 */
public class Habit implements Serializable, Comparable<Habit> {
    private HabitHandler hh = new HabitHandler();

    /**
     * This variable contains a unique identifier for this {@link Habit} object.
     * It is generated at the time of construction and cannot be edited.
     * This variable is of type {@link String}.
     */
    private String id;

    /**
     * This variable is an indicator for whether the {@link Habit} object
     * is public or not (private).
     * This variable is of type boolean.
     */
    private boolean isPublic;

    /**
     * This variable contains the title for this {@link Habit} object.
     * This variable is of type {@link String} with up to 20 characters.
     */
    private String title;


    /**
     * This variable contains the reason for this {@link Habit} object.
     * This variable is of type {@link String} with up to 30 characters.
     */
    private String reason;

    /**
     * This variable contains the start date for this {@link Habit} object.
     * This variable is of type {@link Date}.
     */
    private Date startDate;

    /**
     * This variable contains a plan for what days of the week
     * it should regularly occur. [US 01.02.01]
     * The ArrayList index indicates days of a week
     * 0 : SUN; 1 : MON; 2 : TUE; 3 : WED; 4 : THU; 5 : FRI; 6 : SAT
     * The value at a specific index is {@link Boolean#TRUE} if the habit is planned for the day;
     * {@link Boolean#FALSE} otherwise.
     * This variable if of type {@link ArrayList}.
     */
    private ArrayList<Boolean> plan;

    /**
     * This variable contains a indicator (percentage of event over total scheduled occurrences)
     * to show how closely the user is following its plan over time.
     * This variable is of type int.
     */
    private int progress;

    /**
     * This variable contains the count of events completed.
     * This variable is of type int.
     */
    private int progressNumerator;

    /**
     * This variable contains the count of total scheduled occurrences of this {@link Habit}.
     * This variable is of type int.
     */
    private int progressDenominator;

    public Habit() {}

    /**
     * Constructs a {@link Habit} object with the given title, reason, startDate and plan
     * @param title a {@link String} with up to 20 characters
     * @param reason a {@link String} with up to 30 characters
     * @param startDate the {@link Date} to start the habit
     * @param plan the {@link ArrayList} that stores {@link Boolean} values
     *             on weekly occurrence of the habit
     * @throws IllegalArgumentException
     * This constructor can throw an {@link IllegalArgumentException}
     * if the title {@link String} or the reason {@link String} is illegal
     */
    public Habit(String title, String reason, Date startDate, ArrayList<Boolean> plan, boolean isPublic) {
        if (!hh.isLegalTitle(title)) {
            throw new IllegalArgumentException("a habit title has up to 20 characters and cannot be empty");
        } else if (!hh.isLegalReason(reason)) {
            throw new IllegalArgumentException("a habit reason has up to 30 characters");
        }
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.plan = plan;
        this.isPublic = isPublic;
        this.progress = 0;
        this.progressNumerator = 0;
        this.progressDenominator = 0;
        this.id = Calendar.getInstance().getTime().toString();
    }

    /**
     * Returns the {@link Habit#id} of this {@link Habit} object
     * @return id of type {@link String}
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the {@link Habit#title} of this {@link Habit} object
     * @return title of type {@link String}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the {@link Habit#title} of this {@link Habit} object
     * @param title of type {@link String}
     * @throws IllegalArgumentException
     * This method can throw an {@link IllegalArgumentException}
     * if the title {@link String} is empty or over 20 characters
     */
    public void setTitle(String title) {
        if (!hh.isLegalTitle(title))
            throw new IllegalArgumentException("a habit title has up to 20 characters and cannot be empty");
        this.title = title;
    }

    /**
     * Returns the {@link Habit#reason} of this {@link Habit} object
     * @return reason of type {@link String}
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the {@link Habit#reason} of this {@link Habit} object
     * @param reason of type {@link String}
     * @throws IllegalArgumentException
     * This method can throw an {@link IllegalArgumentException}
     * if the reason {@link String} is over 30 characters
     */
    public void setReason(String reason) {
        if (!hh.isLegalReason(reason))
            throw new IllegalArgumentException("a habit reason has up to 30 characters");
        this.reason = reason;
    }

    /**
     * Returns the {@link Habit#startDate} of this {@link Habit} object
     * @return startDate of type {@link Date}
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the {@link Habit#startDate} of this {@link Habit} object
     * @param startDate of type {@link Date}
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the {@link Habit#isPublic} of this {@link Habit} object
     * @return true if public, false otherwise
     */
    public boolean getIsPublic() {
        return isPublic;
    }

    /**
     * Sets the {@link Habit#isPublic} of this {@link Habit} object
     * @param isPublic true if public, false otherwise
     */
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * Returns the {@link Habit#plan} of this {@link Habit} object
     * @return plan of type {@link ArrayList}
     */
    public ArrayList<Boolean> getPlan() {
        return plan;
    }

    /**
     * Returns the {@link Habit#plan} of this {@link Habit} object
     * for a given index indicating day of a week
     * @param index an int between 0 and 6 -
     *              0 : SUN; 1 : MON; 2 : TUE; 3 : WED; 4 : THU; 5 : FRI; 6 : SAT
     * @return {@link Boolean#TRUE} if the habit is planned for the day;
     * {@link Boolean#FALSE} otherwise
     */
    public Boolean getPlan(int index) {
        return plan.get(index);
    }

    /**
     * Sets the {@link Habit#plan} of this {@link Habit} object
     * @param plan of type {@link ArrayList}
     */
    public void setPlan(ArrayList<Boolean> plan) {
        this.plan = plan;
    }

    /**
     * Sets the {@link Habit#plan} of this {@link Habit} object
     * for a given index indicating day of a week
     * @param index an int between 0 and 6 -
     *              0 : SUN; 1 : MON; 2 : TUE; 3 : WED; 4 : THU; 5 : FRI; 6 : SAT
     * @param value {@link Boolean#TRUE} if the habit is planned for the day;
     * {@link Boolean#FALSE} otherwise
     */
    public void setPlan(int index, Boolean value) {
        this.plan.set(index, value);
    }

    /**
     * Returns the {@link Habit#progress} of this {@link Habit} object
     * @return progress of type int
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Sets the {@link Habit#progress} of this {@link Habit} object
     * @param progress of type int
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * Returns the {@link Habit#progressNumerator} of this {@link Habit} object
     * @return progressNumerator of type int
     */
    public int getProgressNumerator() {
        return progressNumerator;
    }

    /**
     * Sets the {@link Habit#progressNumerator} of this {@link Habit} object
     * @param progressNumerator of type int
     */
    public void setProgressNumerator(int progressNumerator) {
        this.progressNumerator = progressNumerator;
    }

    /**
     * Returns the {@link Habit#progressDenominator} of this {@link Habit} object
     * @return progressDenominator of type int
     */
    public int getProgressDenominator() {
        return progressDenominator;
    }

    /**
     * Sets the {@link Habit#progressDenominator} of this {@link Habit} object
     * @param progressDenominator of type int
     */
    public void setProgressDenominator(int progressDenominator) {
        this.progressDenominator = progressDenominator;
    }

    /**
     * Indicates whether the given {@link Object} is "equal to" this {@link Habit} object
     * @param obj the reference {@link Object} with which to compare
     * @return {@link Boolean#TRUE} if this {@link Habit} is the same as the argument {@link Object};
     * {@link Boolean#FALSE} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Habit)) {
            return false;
        }

        Habit h = (Habit) obj;
        return this.getTitle().equals(h.getTitle());
    }

    /**
     * Compare two Habits for ordering
     * @param habit the {@link Habit} to be compared
     * @return the int 0 if this {@link Habit} is equal to the argument Habit;
     * a value less than 0 if this {@link Habit} is before the argument Habit;
     * a value greater than 0 if this {@link Habit} is after the argument Habit
     */
    @Override
    public int compareTo(Habit habit) {
        return this.getTitle().compareTo(habit.getTitle());
        // order by startDate instead
        // return this.getStartDate().compareTo(habit.getStartDate());
    }

    /**
     * Converts this {@link Habit} object to a {@link String}
     * @return {@link String}
     */
    @Override
    public String toString() {
        return this.getTitle();
        // return this.getTitle() + " " + this.getStartDate().toString();
    }
}