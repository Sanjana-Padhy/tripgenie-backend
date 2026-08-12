package com.tripgenie.backend.dto;

import java.util.List;

public class ItineraryResponse {

    private String destination;
    private double totalBudget;
    private List<DayResponse> days;

    public ItineraryResponse() {
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public List<DayResponse> getDays() {
        return days;
    }

    public void setDays(List<DayResponse> days) {
        this.days = days;
    }
}