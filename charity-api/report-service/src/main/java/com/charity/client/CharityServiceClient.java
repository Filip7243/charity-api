package com.charity.client;

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

        if (events == null || events.length == 0) {
            log.error("Failed to fetch fundraising events");
            throw new IllegalStateException("Failed to fetch fundraising events");
        }

        log.info("Fetched {} fundraising events", events.length);

        return events;
    }
}
