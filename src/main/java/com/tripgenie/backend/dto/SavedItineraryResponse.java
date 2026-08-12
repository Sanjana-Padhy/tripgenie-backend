package com.tripgenie.backend.dto;

public class SavedItineraryResponse {

    private Long savedId;
    private Long tripId;
    private String destination;
    private String startDate;
    private String endDate;
    private Double budget;

    public SavedItineraryResponse() {
    }

    public SavedItineraryResponse(
            Long savedId,
            Long tripId,
            String destination,
            String startDate,
            String endDate,
            Double budget) {

        this.savedId = savedId;
        this.tripId = tripId;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
    }

    public Long getSavedId() {
        return savedId;
    }

    public Long getTripId() {
        return tripId;
    }

    public String getDestination() {
        return destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Double getBudget() {
        return budget;
    }
}