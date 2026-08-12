package com.tripgenie.backend.dto;

public class ActivityResponse {

    private int number;
    private String time;
    private String activity;
    private String location;
    private double estimatedCost;
    private String description;

    public ActivityResponse() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}