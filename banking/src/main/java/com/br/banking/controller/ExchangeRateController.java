package com.br.banking.controller;

import com.br.banking.service.ExchangeRateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ExchangeRateController {

    ExchangeRateService exchangeRateService;

    @GetMapping("/exchange-rates")
    public BigDecimal getExchangeRates(@RequestParam BigDecimal value,@RequestParam String currentCurrency,@RequestParam String newCurrency) {
        return exchangeRateService.convertCurrency(value,currentCurrency,newCurrency);
    }

    @GetMapping("/rates")
    public RedirectView rates(){
        return new RedirectView("https://api.exchangerate-api.com/v4/latest/USD");
    }
}
