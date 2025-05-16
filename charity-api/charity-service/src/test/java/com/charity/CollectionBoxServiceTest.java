package com.charity;

import com.charity.dto.AddMoneyRequest;
import com.charity.dto.CreateCollectionBoxRequest;
import com.charity.exception.ResourceNotFound;
import com.charity.model.CollectionBox;
import com.charity.model.FundraisingEvent;
import com.charity.model.Money;
import com.charity.repository.CollectionBoxRepository;
import com.charity.service.CollectionBoxService;
import com.charity.service.FundraisingEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.charity.model.CurrencyCode.*;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CollectionBoxServiceTest {

    @Mock
    private FundraisingEventService fundraisingEventService;
    @Mock
    private CollectionBoxRepository collectionBoxRepository;

    @InjectMocks
    private CollectionBoxService collectionBoxService;

    @Test
    void shouldCreateCollectionBox() {
        Long eventId = 1L;
        var request = new CreateCollectionBoxRequest(Set.of(EUR, USD), eventId);
        var event = new FundraisingEvent("Test Event", new Money(ZERO, PLN));
        var savedBox = new CollectionBox(List.of(), event);
        savedBox.setId(1L);

        when(fundraisingEventService.getEventById(eventId)).thenReturn(event);
        when(collectionBoxRepository.save(any(CollectionBox.class))).thenReturn(savedBox);

        collectionBoxService.createCollectionBox(request);
        verify(collectionBoxRepository).save(any(CollectionBox.class));
    }

    @Test
    void shouldThrowExceptionWhenBoxNotFound() {
        Long id = 1L;

        when(collectionBoxRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> collectionBoxService.getCollectionBoxById(id));
    }

    @Test
    void shouldAddMoneyToBox() {
        Long boxId = 1L;
        var request = new AddMoneyRequest(boxId, BigDecimal.TEN, EUR);
        var currentBoxMoney = new Money(ZERO, EUR);
        var box = new CollectionBox(List.of(currentBoxMoney),
                new FundraisingEvent("Test Event", currentBoxMoney));

        when(collectionBoxRepository.findById(boxId)).thenReturn(Optional.of(box));

        collectionBoxService.addMoneyToBox(request);

        assertEquals(BigDecimal.TEN.setScale(2, HALF_EVEN),
                box.getMoneys().get(0).amount());
    }
}
