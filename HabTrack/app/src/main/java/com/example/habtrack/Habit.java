package com.example.habtrack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Habit implements Serializable {
    private String title;
    private String reason;
    private Date startDate;
    private ArrayList<Boolean> plan;
    // private int[] plan;
    private int progress;

    public Habit() {}

    public Habit(String title, String reason, Date startDate, ArrayList<Boolean> plan) {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.plan = plan;
        this.progress = 100;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ArrayList<Boolean> getPlan() {
        return plan;
    }

    public void setPlan(ArrayList<Boolean> plan) {
        this.plan = plan;
    }

    public Boolean getPlan(int index) {
        return plan.get(index);
    }

    public void setPlan(int index, Boolean value) {
        this.plan.set(index, value);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
