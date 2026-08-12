package com.tripgenie.backend.dto;

import java.util.List;

public class DayResponse {

    private int day;
    private String title;
    private List<ActivityResponse> activities;

    public DayResponse() {
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ActivityResponse> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityResponse> activities) {
        this.activities = activities;
    }
}