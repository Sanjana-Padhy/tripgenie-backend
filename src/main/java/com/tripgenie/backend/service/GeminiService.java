package com.tripgenie.backend.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Schema;
import com.tripgenie.backend.dto.GenerateItineraryRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;
public String generateItinerary(GenerateItineraryRequest request) {

    if (apiKey == null || apiKey.isBlank()) {
        throw new IllegalStateException(
                "Gemini API key is not configured. Please set GEMINI_API_KEY."
        );
    }

    try {

        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        String prompt =
                "Create a "
                        + request.getDays()
                        + "-day "
                        + request.getTravelStyle()
                        + " trip itinerary from "
                        + request.getSource()
                        + " to "
                        + request.getDestination()
                        + " for "
                        + request.getTravelers()
                        + " travelers within a total budget of ₹"
                        + request.getBudget()
                        + ".\n\n"
                        + "Consider the starting location when planning the journey. "
                        + "The itinerary should be practical for "
                        + request.getTravelers()
                        + " travelers. "
                        + "Consider transportation, food, activities and other reasonable travel expenses. "
                        + "Do not exceed the total budget. "
                        + "Create a practical itinerary suitable for the specified budget. "
                        + "Include activities in chronological order for each day. "
                        + "Include realistic estimated costs. "
                        + "Return ONLY valid JSON matching the provided schema. "
                        + "Do not include markdown, explanations, or text outside the JSON.";

        // Activity schema
        Schema activitySchema = Schema.builder()
                .type("OBJECT")
                .properties(Map.of(

                        "number", Schema.builder()
                                .type("INTEGER")
                                .build(),

                        "time", Schema.builder()
                                .type("STRING")
                                .build(),

                        "activity", Schema.builder()
                                .type("STRING")
                                .build(),

                        "location", Schema.builder()
                                .type("STRING")
                                .build(),

                        "estimatedCost", Schema.builder()
                                .type("NUMBER")
                                .build(),

                        "description", Schema.builder()
                                .type("STRING")
                                .build()
                ))
                .required(List.of(
                        "number",
                        "time",
                        "activity",
                        "location",
                        "estimatedCost",
                        "description"
                ))
                .build();

        // Day schema
        Schema daySchema = Schema.builder()
                .type("OBJECT")
                .properties(Map.of(

                        "day", Schema.builder()
                                .type("INTEGER")
                                .build(),

                        "title", Schema.builder()
                                .type("STRING")
                                .build(),

                        "activities", Schema.builder()
                                .type("ARRAY")
                                .items(activitySchema)
                                .build()
                ))
                .required(List.of(
                        "day",
                        "title",
                        "activities"
                ))
                .build();

        // Complete itinerary schema
        Schema itinerarySchema = Schema.builder()
                .type("OBJECT")
                .properties(Map.of(

                        "source", Schema.builder()
                                .type("STRING")
                                .build(),

                        "destination", Schema.builder()
                                .type("STRING")
                                .build(),

                        "travelers", Schema.builder()
                                .type("INTEGER")
                                .build(),

                        "totalBudget", Schema.builder()
                                .type("NUMBER")
                                .build(),

                        "days", Schema.builder()
                                .type("ARRAY")
                                .items(daySchema)
                                .build()
                ))
                .required(List.of(
                        "source",
                        "destination",
                        "travelers",
                        "totalBudget",
                        "days"
                ))
                .build();

        GenerateContentConfig config =
                GenerateContentConfig.builder()
                        .responseMimeType("application/json")
                        .responseSchema(itinerarySchema)
                        .build();

        System.out.println("==========================================");
        System.out.println("CALLING GEMINI API");
        System.out.println("Model: gemini-3.5-flash");
        System.out.println("==========================================");

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.5-flash",
                        prompt,
                        config
                );

        System.out.println("==========================================");
        System.out.println("GEMINI API RESPONSE RECEIVED");
        System.out.println("==========================================");

        String result = response.text();

        System.out.println("Generated itinerary:");
        System.out.println(result);

        return result;

    } catch (Exception e) {

        System.err.println("==========================================");
        System.err.println("GEMINI API ERROR");
        System.err.println("==========================================");

        e.printStackTrace();

        throw new RuntimeException(
                "Failed to generate itinerary using Gemini: "
                        + e.getMessage(),
                e
        );
    }
}
}