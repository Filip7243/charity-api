package com.charity;

import com.charity.client.Client;
import com.charity.dto.ExchangeRateResponse;
import com.charity.exception.InvalidDataException;
import com.charity.model.CurrencyCode;
import com.charity.service.CurrencyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.time.LocalTime.now;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CurrencyServiceTest {

    @Mock
    private Client client;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void shouldGetRates() {
        String from = "EUR";
        List<String> to = List.of("USD", "GBP");
        List<CurrencyCode> codes = to.stream()
                .map(CurrencyCode::convertTo)
                .toList();

        Map<String, BigDecimal> rates = Map.of(
                "USD", BigDecimal.valueOf(1.1),
                "GBP", BigDecimal.valueOf(0.85)
        );
        var mockResponse = new ExchangeRateResponse(from, now().toString(), rates);

        when(client.getExchangeRates(from, codes)).thenReturn(mockResponse);

        Map<String, BigDecimal> result = currencyService.getRates(from, to);
        assert result.size() == 2;
        assert result.get("USD").compareTo(BigDecimal.valueOf(1.1)) == 0;
        assert result.get("GBP").compareTo(BigDecimal.valueOf(0.85)) == 0;
    }
}
