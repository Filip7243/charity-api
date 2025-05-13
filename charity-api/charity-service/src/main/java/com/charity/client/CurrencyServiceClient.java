package com.charity.client;

import com.charity.model.CurrencyCode;
import com.charity.model.FundraisingEvent;
import com.charity.model.Money;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_EVEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyServiceClient {

    private final WebClient.Builder webClientBuilder;
    private static final int RATE_SCALE = 2;

    public Money convertAndSumAllBoxes(FundraisingEvent fundraisingEvent, List<Money> boxMoneys, List<String> toCodes) {
        CurrencyCode targetCurrency = fundraisingEvent.getAmount().currencyCode();
        Map<String, BigDecimal> rates = fetchExchangeRates(targetCurrency, toCodes);

        Money total = fundraisingEvent.getAmount();

        for (Money boxMoney : boxMoneys) {
            total = total.add(convertToTargetCurrency(boxMoney, targetCurrency, rates));
        }

        return total;
    }

    private Map<String, BigDecimal> fetchExchangeRates(CurrencyCode targetCurrency, List<String> toCodes) {
        log.info("Fetching exchange rates for target currency: {}", targetCurrency);

        String uri = buildCurrencyUri(targetCurrency.name(), toCodes);
        Map<String, BigDecimal> rates = webClientBuilder.build()
                .get()
                .uri("lb://currency-service/v1/currencies/{currency}?to=PLN", targetCurrency)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, BigDecimal>>() {
                })
                .block();

        if (rates == null || rates.isEmpty()) {
            log.error("Failed to fetch exchange rates");
            throw new IllegalStateException("Failed to fetch exchange rates");
        }

        log.info("Fetched exchange rates: {}", rates);
        return rates;
    }

    private Money convertToTargetCurrency(Money sourceMoney, CurrencyCode targetCurrency, Map<String, BigDecimal> rates) {
        CurrencyCode sourceCurrency = sourceMoney.currencyCode();

        if (sourceCurrency.equals(targetCurrency)) {
            log.info("Currency {} is the same as target. No conversion needed.", sourceCurrency);
            return sourceMoney;
        }

        BigDecimal rate = rates.get(sourceCurrency.name());
        if (rate == null) {
            log.error("Missing exchange rate for currency {}", sourceCurrency);
            throw new IllegalArgumentException("Exchange rate for currency " + sourceCurrency + " not found");
        }

        BigDecimal convertedAmount = sourceMoney.amount()
                .divide(rate, RATE_SCALE, HALF_EVEN);  // TODO: add divide in Money

        return new Money(convertedAmount, targetCurrency);
    }

    private String buildCurrencyUri(String baseCurrency, List<String> targetCurrencies) {
        String baseUri = "lb://currency-service/v1/currencies/" + baseCurrency;

        if (targetCurrencies == null || targetCurrencies.isEmpty()) {
            return baseUri;
        }

        String queryParams = targetCurrencies.stream()
                .map(code -> "to=" + code)
                .collect(Collectors.joining("&"));

        return baseUri + "?" + queryParams;
    }
}
