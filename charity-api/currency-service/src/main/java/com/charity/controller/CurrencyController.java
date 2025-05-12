package com.charity.controller;

import com.charity.model.CurrencyCode;
import com.charity.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/{from}")
    public Map<String, BigDecimal> getExchangeRates(@PathVariable String from,
                                                    @RequestParam("to") List<String> to) {
        return currencyService.getCurrencies(from, to);
    }
}
