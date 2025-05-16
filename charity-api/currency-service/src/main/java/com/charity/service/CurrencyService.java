package com.charity.service;

import com.charity.client.Client;
import com.charity.dto.ExchangeRateResponse;
import com.charity.model.CurrencyCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final Client client;

    public Map<String, BigDecimal> getRates(String from, List<String> to) {
        List<CurrencyCode> codes = to.stream()
                .map(CurrencyCode::convertTo)
                .toList();
        ExchangeRateResponse exchangeRates = client.getExchangeRates(from, codes);

        return exchangeRates.rates();
    }
}
