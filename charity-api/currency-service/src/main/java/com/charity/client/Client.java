package com.charity.client;

import com.charity.dto.ExchangeRateResponse;
import com.charity.model.CurrencyCode;

import java.util.List;

public interface Client {

    ExchangeRateResponse getExchangeRates(String from, List<CurrencyCode> to);
}
