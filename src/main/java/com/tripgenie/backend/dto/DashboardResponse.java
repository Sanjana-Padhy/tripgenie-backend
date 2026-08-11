package com.tripgenie.backend.dto;

import java.util.List;

public class DashboardResponse {

    private long totalTrips;
    private long upcomingTrips;
    private long completedTrips;
    private double totalBudget;
    private List<TripResponse> recentTrips;

    public DashboardResponse() {
    }

    public DashboardResponse(
            long totalTrips,
            long upcomingTrips,
            long completedTrips,
            double totalBudget,
            List<TripResponse> recentTrips) {

        this.totalTrips = totalTrips;
        this.upcomingTrips = upcomingTrips;
        this.completedTrips = completedTrips;
        this.totalBudget = totalBudget;
        this.recentTrips = recentTrips;
    }

    public long getTotalTrips() {
        return totalTrips;
    }

    public long getUpcomingTrips() {
        return upcomingTrips;
    }

    public long getCompletedTrips() {
        return completedTrips;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public List<TripResponse> getRecentTrips() {
        return recentTrips;
    }
}