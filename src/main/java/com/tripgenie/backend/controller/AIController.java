package com.tripgenie.backend.controller;

import com.tripgenie.backend.dto.GenerateItineraryRequest;
import com.tripgenie.backend.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private GeminiService geminiService;

    @PostMapping("/generate")
    public String generateItinerary(
            @RequestBody GenerateItineraryRequest request) {

        System.out.println("AI Controller Hit");

        return geminiService.generateItinerary(request);
    }
}