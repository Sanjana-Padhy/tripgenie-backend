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
                        + " within a total budget of ₹"
                        + request.getBudget()
                        + ".\n\n"
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

                        "destination", Schema.builder()
                                .type("STRING")
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
                        "destination",
                        "totalBudget",
                        "days"
                ))
                .build();

        // Tell Gemini to return JSON according to our schema
        GenerateContentConfig config =
                GenerateContentConfig.builder()
                        .responseMimeType("application/json")
                        .responseSchema(itinerarySchema)
                        .build();

        // Call Gemini
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.5-flash",
                        prompt,
                        config
                );

        // Return the generated JSON
        return response.text();
    }
}