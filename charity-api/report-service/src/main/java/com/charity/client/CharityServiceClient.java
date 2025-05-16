package com.charity.client;

import com.charity.exception.InvalidDataException;
import com.charity.exception.ResourceNotFound;
import com.charity.model.FundraisingEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class CharityServiceClient {

    private final WebClient.Builder webClientBuilder;

    public FundraisingEventDto[] getAllFundraisingEvents() {
        log.info("Fetching all fundraising events from charity-service");

        FundraisingEventDto[] events = webClientBuilder.build()
                .get()
                .uri("lb://charity-service/v1/events")
                .retrieve()
                .bodyToMono(FundraisingEventDto[].class)
                .block();

        if (events == null) {
            log.error("Failed to fetch fundraising events - response is null");
            throw new IllegalStateException("Failed to fetch fundraising events - response is null");
        }

        if (events.length == 0) {
            log.warn("No fundraising events found");
            throw new ResourceNotFound("Failed to fetch fundraising events - response is empty");
        }

        log.info("Fetched {} fundraising events", events.length);

        return events;
    }
}
