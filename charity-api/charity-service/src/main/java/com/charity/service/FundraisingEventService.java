package com.charity.service;

import com.charity.dto.CreateFundraisingEventRequest;
import com.charity.dto.FundraisingEventDto;
import com.charity.model.FundraisingEvent;
import com.charity.model.Money;
import com.charity.repository.CollectionBoxRepository;
import com.charity.repository.FundraisingEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.math.BigDecimal.ZERO;

@Slf4j
@Service
@RequiredArgsConstructor
public class FundraisingEventService {

    private final FundraisingEventRepository fundraisingEventRepository;
    private final CollectionBoxRepository collectionBoxRepository;

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

    public List<FundraisingEventDto> getAllFundraisingEvents() {
        log.info("Fetching all fundraising events");

        return fundraisingEventRepository.findAll()
                .stream()
                .map(event -> new FundraisingEventDto(
                        event.getName(),
                        collectionBoxRepository.countByFundraisingEventId(event.getId()),
                        event.getAmount().amount(),
                        event.getAmount().currencyCode()
                )).toList();
    }
}
