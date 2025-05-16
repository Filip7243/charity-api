package com.charity.service;

import com.charity.client.CurrencyServiceClient;
import com.charity.dto.AddMoneyRequest;
import com.charity.dto.CollectionBoxDto;
import com.charity.dto.CreateCollectionBoxRequest;
import com.charity.exception.*;
import com.charity.model.CollectionBox;
import com.charity.model.CurrencyCode;
import com.charity.model.Money;
import com.charity.repository.CollectionBoxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectionBoxService {

    private final CollectionBoxRepository collectionBoxRepository;
    private final FundraisingEventService fundraisingEventService;
    private final CurrencyServiceClient currencyServiceClient;

    @Transactional
    public void createCollectionBox(CreateCollectionBoxRequest request) {
        List<Money> moneys = request.currencyCodes()
                .stream()
                .map(code -> new Money(ZERO, code))
                .toList();

        // Exception will be thrown if the event does not exist
        var event = fundraisingEventService.getEventById(request.fundraisingEventId());

        var collectionBox = new CollectionBox(moneys, event);

        var newCollectionBox = collectionBoxRepository.save(collectionBox);

        log.info("Collection box created with ID: {}", newCollectionBox.getId());
    }

    public List<CollectionBoxDto> getAllCollectionBoxes() {
        log.info("Fetching all collection boxes");

        var boxes = collectionBoxRepository.findAll();

        return boxes.stream()
                .map(this::mapToDto)
                .toList();
    }

    public CollectionBoxDto getCollectionBoxById(Long id) {
        log.info("Fetching box with ID: {}", id);

        var box = collectionBoxRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Collection box with ID {} not found", id);
                    return new ResourceNotFound("Collection box not found");
                });

        return mapToDto(box);
    }

    @Transactional
    public void deleteCollectionBox(Long id) {
        log.info("Deleting box with ID: {}", id);

        if (!collectionBoxRepository.existsById(id)) {
            log.error("Collection box with ID {} not found - cannot delete", id);
            throw new ResourceNotFound("Collection box not found");
        }

        collectionBoxRepository.deleteById(id);
    }

    @Transactional
    public CollectionBoxDto assignBoxToEvent(Long boxId, Long eventId) {
        log.info("Assigning box with ID: {} to event with ID: {}", boxId, eventId);

        var collectionBox = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> {
                    log.error("Collection box with ID {} not found - while assigning", boxId);
                    return new ResourceNotFound("Collection box not found");
                });

        if (collectionBox.getFundraisingEvent() != null) {
            log.error("Collection box with ID {} is already assigned to an event", boxId);
            throw new BoxAlreadyAssignedException("Collection box is already assigned to an event");
        }

        double moneyInBox = collectionBox.getMoneys()
                .stream()
                .mapToDouble(money -> money.amount().doubleValue())
                .sum();
        if (moneyInBox > 0) {
            log.error("Collection box with ID {} is not empty", boxId);
            throw new BoxNotEmptyException("Collection box is not empty");
        }

        var fundraisingEvent = fundraisingEventService.getEventById(eventId);
        collectionBox.setFundraisingEvent(fundraisingEvent);

        CollectionBox updatedCollectionBox = collectionBoxRepository.save(collectionBox);

        log.info("Collection box with ID: {} assigned to event with ID: {}", boxId, eventId);

        return mapToDto(updatedCollectionBox);
    }

    @Transactional
    public void addMoneyToBox(AddMoneyRequest request) {
        log.info("Adding money to box with ID: {}", request.boxId());

        var collectionBox = collectionBoxRepository.findById(request.boxId())
                .orElseThrow(() -> {
                    log.error("Collection box with ID {} not found - while adding money", request.boxId());
                    return new ResourceNotFound("Collection box not found");
                });

        Money money = new Money(request.amount(), request.code());
        Money currentBoxMoney = collectionBox.getMoneys()
                .stream()
                .filter(m -> m.currencyCode().equals(request.code()))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Collection box with ID {} does not contain currency {}",
                            request.boxId(), request.code());
                    return new IllegalCurrencyCodeException("Collection box does not contain this currency");
                });

        Money updatedMoney = currentBoxMoney.add(money);

        List<Money> updatedMoneys = collectionBox.getMoneys()
                .stream()
                .map(m -> m.currencyCode().equals(request.code()) ? updatedMoney : m)
                .toList();
        collectionBox.setMoneys(updatedMoneys);

        log.info("Money added to box with ID: {}", request.boxId());
    }

    @Transactional
    public CollectionBoxDto transferMoneyToFundraisingEvent(Long boxId, Long eventId) {
        log.info("Transferring money from box with ID: {} to event with ID: {}", boxId, eventId);

        if (boxId == null || eventId == null) {
            log.error("Box ID or Event ID is null");
            throw new IllegalArgumentException("Box ID or Event ID cannot be null");
        }

        var collectionBox = collectionBoxRepository.findById(boxId)
                .orElseThrow(() -> {
                    log.error("Collection box with ID {} not found - while transferring money", boxId);
                    return new ResourceNotFound("Collection box not found");
                });

        if (collectionBox.getFundraisingEvent() == null) {
            log.error("Collection box with ID {} is not assigned to an event", boxId);
            throw new BoxNotAssignedException("Collection box is not assigned to an event");
        }

        if (!collectionBox.getFundraisingEvent().getId().equals(eventId)) {
            log.error("Collection box with ID {} is assigned to a different event", boxId);
            throw new BoxAlreadyAssignedException("Collection box is assigned to a different event");
        }

        List<Money> boxMoneys = collectionBox.getMoneys();
        double moneyInBox = boxMoneys
                .stream()
                .mapToDouble(money -> money.amount().doubleValue())
                .sum();
        if (moneyInBox <= 0) {
            log.error("Collection box with ID {} is empty", boxId);
            throw new EmptyBoxException("Collection box is empty");
        }

        var fundraisingEvent = fundraisingEventService.getEventById(eventId);

        List<String> toCodes = collectionBox.getMoneys()
                .stream()
                .map(money -> money.currencyCode().name())
                .distinct()
                .toList();

        Money total = currencyServiceClient.convertAndSumAllBoxes(fundraisingEvent, boxMoneys, toCodes);
        fundraisingEvent.setAmount(total);

        List<Money> clearedAmount = boxMoneys.stream()
                .map(money -> money.subtract(money))
                .toList();
        collectionBox.setMoneys(clearedAmount);

        log.info("Money {} - transferred from box with ID: {} to event with ID: {}", total.amount(), boxId, eventId);

        return mapToDto(collectionBox);
    }

    private CollectionBoxDto mapToDto(CollectionBox collectionBox) {
        var moneys = collectionBox.getMoneys();

        boolean isEmpty = moneys.stream()
                .mapToDouble(money -> money.amount().doubleValue())
                .sum() == 0.0;
        boolean isAssigned = collectionBox.getFundraisingEvent() != null;
        Set<CurrencyCode> currencyCodes = moneys.stream()
                .map(Money::currencyCode)
                .collect(Collectors.toSet());

        return new CollectionBoxDto(collectionBox.getId(), isAssigned, isEmpty, currencyCodes);
    }
}
