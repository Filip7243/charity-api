package com.charity.controller;

import com.charity.dto.CreateFundraisingEventRequest;
import com.charity.dto.FundraisingEventDto;
import com.charity.service.FundraisingEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/events")
public class FundraisingEventController {

    private final FundraisingEventService fundraisingEventService;

    @PostMapping
    public void createFundraisingEvent(@RequestBody CreateFundraisingEventRequest request) {
        fundraisingEventService.createFundraisingEvent(request);
    }

    @GetMapping
    public List<FundraisingEventDto> getAllFundraisingEvents() {
        return fundraisingEventService.getAllFundraisingEvents();
    }
}
