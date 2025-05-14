package com.charity;

import com.charity.client.CharityServiceClient;
import com.charity.model.CurrencyCode;
import com.charity.model.FundraisingEventDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CharityServiceClientTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient; // Mockuj WebClient

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec; // Mockuj RequestHeadersUriSpec

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec; // Mockuj RequestHeadersSpec

    @Mock
    private WebClient.ResponseSpec responseSpec; // Mockuj ResponseSpec

    @InjectMocks
    private CharityServiceClient charityServiceClient;


    @Test
    public void shouldReturnEvents() {
        FundraisingEventDto e1 = new FundraisingEventDto(
                "Event_1",
                1,
                BigDecimal.ZERO,
                CurrencyCode.EUR
        );
        FundraisingEventDto[] events = {e1};

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FundraisingEventDto[].class)).thenReturn(Mono.just(events));

        FundraisingEventDto[] result = charityServiceClient.getAllFundraisingEvents();
        assert result != null;
        assert result.length == 1;
        assert result[0].name().equals("Event_1");
    }

    @Test
    public void shouldThrowExceptionWhenEventsAreNull() {
        FundraisingEventDto[] empty = {};

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(FundraisingEventDto[].class)).thenReturn(Mono.just(empty));

        try {
            charityServiceClient.getAllFundraisingEvents();
        } catch (IllegalStateException e) {
            assert e.getMessage().equals("Failed to fetch fundraising events");
        }
    }
}
