package com.tripgenie.backend.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.tripgenie.backend.dto.GenerateItineraryRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateItinerary(GenerateItineraryRequest request) {

        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        String prompt =
                "Create a "
                        + request.getDays()
                        + "-day "
                        + request.getTravelStyle()
                        + " trip itinerary for "
                        + request.getDestination()
                        + " within a budget of ₹"
                        + request.getBudget();

        GenerateContentResponse response =
                client.models.generateContent(
                          "gemini-2.5-pro",
                         prompt,
                        null
                );

        return response.text();
    }
}