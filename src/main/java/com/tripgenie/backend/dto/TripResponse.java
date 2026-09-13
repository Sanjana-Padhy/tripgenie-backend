package com.tripgenie.backend.dto;

public class TripResponse {

    private Long id;
    private String destination;
    private String startDate;
    private String endDate;
    private Double budget;
    private String travelStyle;
    private String status;

    public TripResponse() {
    }

    public TripResponse(Long id,
                        String destination,
                        String startDate,
                        String endDate,
                        Double budget,
                        String travelStyle,
                        String status) {

        this.id = id;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.travelStyle = travelStyle;
        this.status = status;
    }

    public Long getId() {
        return id;
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

    public String getTravelStyle() {
        return travelStyle;
    }

    public String getStatus() {
        return status;
    }
}