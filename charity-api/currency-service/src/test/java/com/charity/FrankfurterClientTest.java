package com.charity;

import com.charity.client.FrankfurterClient;
import com.charity.dto.ExchangeRateResponse;
import com.charity.exception.InvalidDataException;
import com.charity.model.CurrencyCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class FrankfurterClientTest {

    @Autowired
    private FrankfurterClient client;

    @Test
    void shouldFetchRatesFromFrankfurter() {
        String from = "EUR";
        List<CurrencyCode> to = List.of(CurrencyCode.USD, CurrencyCode.GBP);

        var response = client.getExchangeRates(from, to);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.rates());
        Assertions.assertFalse(response.rates().isEmpty());
        Assertions.assertTrue(response.rates().containsKey("USD"));
    }

    @Test
    void shouldThrowExceptionForEmptyToList() {
        String from = "EUR";

        InvalidDataException exception = Assertions.assertThrows(
                InvalidDataException.class,
                () -> client.getExchangeRates(from, List.of())
        );

        Assertions.assertEquals("No valid currency codes provided", exception.getMessage());
    }
}
