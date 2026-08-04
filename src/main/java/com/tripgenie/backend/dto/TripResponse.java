package com.tripgenie.backend.dto;

public class TripResponse {

    private Long id;
    private String destination;
    private String startDate;
    private String endDate;
    private Double budget;

    public TripResponse() {
    }

    public TripResponse(Long id,
                        String destination,
                        String startDate,
                        String endDate,
                        Double budget) {

        this.id = id;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
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
}