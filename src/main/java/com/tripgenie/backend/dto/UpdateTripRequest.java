
package com.tripgenie.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UpdateTripRequest {

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotBlank(message = "Start date is required")
    @Pattern(
        regexp = "^\\d{4}-\\d{2}-\\d{2}$",
        message = "Start date must use YYYY-MM-DD format"
    )
    private String startDate;

    @NotBlank(message = "End date is required")
    @Pattern(
        regexp = "^\\d{4}-\\d{2}-\\d{2}$",
        message = "End date must use YYYY-MM-DD format"
    )
    private String endDate;

    @NotNull(message = "Budget is required")
    @Min(value = 0, message = "Budget cannot be negative")
    private Double budget;

    public UpdateTripRequest() {
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }
}
