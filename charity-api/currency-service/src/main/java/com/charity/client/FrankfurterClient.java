package com.charity.client;

import com.charity.dto.ExchangeRateResponse;
import com.charity.model.CurrencyCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class FrankfurterClient implements Client {

    private static final String BASE_URL = "https://api.frankfurter.app/latest";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Cacheable(value = "exchangeRates", key = "#from + ':' + #to")
    public ExchangeRateResponse getExchangeRates(String from, List<CurrencyCode> to) {
        List<String> codes = to.stream()
                .map(code -> code.name().toUpperCase())
                .toList();
        String toParam = String.join(",", codes);
        String url = String.format("%s?base=%s&symbols=%s", BASE_URL, from, toParam);

        try {
            ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
            if (response == null || response.rates() == null || response.rates().isEmpty()) {
                log.error("Response is null or empty");
                throw new IllegalStateException("Failed to fetch exchange rates");
            }

            return response;
        } catch (Exception e) {
            log.error("Error fetching exchange rates from Frankfurter API: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch exchange rates from Frankfurter API", e);
        }
    }
}
