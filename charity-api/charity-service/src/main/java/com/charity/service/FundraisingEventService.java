package com.charity.service;

import com.charity.dto.CreateFundraisingEventRequest;
import com.charity.model.FundraisingEvent;
import com.charity.model.Money;
import com.charity.repository.FundraisingEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

@Slf4j
@Service
@RequiredArgsConstructor
public class FundraisingEventService {

    private final FundraisingEventRepository fundraisingEventRepository;

    @Transactional
    public void createFundraisingEvent(CreateFundraisingEventRequest request) {
        Money amount = new Money(ZERO, request.code());
        FundraisingEvent event = new FundraisingEvent(request.name(), amount);

        FundraisingEvent newEvent = fundraisingEventRepository.save(event);

        log.info("Fundraising event created with ID: {}", newEvent.getId());
    }

    FundraisingEvent getEventById(Long id) {
        if (id == null) {
            log.info("Fundraising event with ID is null");
            return null;
        }

        return fundraisingEventRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Fundraising event with ID {} not found", id);
                    return new IllegalArgumentException("Fundraising event not found");
                });
    }
}
