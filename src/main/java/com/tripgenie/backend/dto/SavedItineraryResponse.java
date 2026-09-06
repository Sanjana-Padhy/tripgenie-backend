package com.tripgenie.backend.dto;

public class SavedItineraryResponse {

    private Long id;
    private Long tripId;
    private String destination;
    private double budget;
    private String travelStyle;
    private String status;
    private boolean saved;

    public SavedItineraryResponse() {
    }

    public SavedItineraryResponse(
            Long id,
            Long tripId,
            String destination,
            double budget,
            String travelStyle,
            String status,
            boolean saved) {

        this.id = id;
        this.tripId = tripId;
        this.destination = destination;
        this.budget = budget;
        this.travelStyle = travelStyle;
        this.status = status;
        this.saved = saved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getTravelStyle() {
        return travelStyle;
    }

    public void setTravelStyle(String travelStyle) {
        this.travelStyle = travelStyle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}