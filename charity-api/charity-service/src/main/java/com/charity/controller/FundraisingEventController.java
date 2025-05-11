package com.charity.controller;

import com.charity.dto.CreateFundraisingEventRequest;
import com.charity.service.FundraisingEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/events")
public class FundraisingEventController {

    private final FundraisingEventService fundraisingEventService;

    @PostMapping
    public void createFundraisingEvent(@RequestBody CreateFundraisingEventRequest request) {
        fundraisingEventService.createFundraisingEvent(request);
    }
}
