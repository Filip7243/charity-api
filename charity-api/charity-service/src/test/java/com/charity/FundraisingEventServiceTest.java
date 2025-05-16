package com.charity;

import com.charity.dto.CreateFundraisingEventRequest;
import com.charity.exception.ResourceNotFound;
import com.charity.model.CurrencyCode;
import com.charity.model.FundraisingEvent;
import com.charity.model.Money;
import com.charity.repository.FundraisingEventRepository;
import com.charity.service.FundraisingEventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FundraisingEventServiceTest {

    @Mock
    private FundraisingEventRepository fundraisingEventRepository;

    @InjectMocks
    private FundraisingEventService fundraisingEventService;

    @Test
    void shouldCreateEvent() {
        var request = new CreateFundraisingEventRequest(
                "Test Event",
                CurrencyCode.PLN
        );
        var savedEvent = new FundraisingEvent(
                request.name(),
                new Money(BigDecimal.ZERO, request.code())
        );

        when(fundraisingEventRepository.save(any(FundraisingEvent.class)))
                .thenReturn(savedEvent);
        fundraisingEventService.createFundraisingEvent(request);

        verify(fundraisingEventRepository).save(savedEvent);
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {
        Long eventId = 1L;
        when(fundraisingEventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> fundraisingEventService.getEventById(eventId));
    }
}
