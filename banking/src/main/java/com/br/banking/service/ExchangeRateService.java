package com.br.banking.service;

import com.br.banking.dto.response.ExchangeRateResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ExchangeRateService {
    static String EXCHANGE_RATE_API_URL = "https://api.exchangerate-api.com/v4/latest/USD";
    RestTemplate restTemplate;

    static BigDecimal currentCurrencyRate;
    static BigDecimal newCurrencyRate;


    public ExchangeRateService() {
        this.restTemplate = new RestTemplate();
    }

    public void setCurrentRates(String currentCurrency,String newCurrency){
        ExchangeRateResponse exchangeRateResponse = getApi(currentCurrency,newCurrency);
        BigDecimal currentCurrencyRate = exchangeRateResponse.getRates().get(currentCurrency);
        BigDecimal newCurrencyRate = exchangeRateResponse.getRates().get(newCurrency);
        ExchangeRateService.currentCurrencyRate=currentCurrencyRate;
        ExchangeRateService.newCurrencyRate=newCurrencyRate;
    }

    public BigDecimal convertCurrency(BigDecimal value, String currentCurrency, String newCurrency) {
        setCurrentRates(currentCurrency,newCurrency);

        BigDecimal valueInUSD = value.divide(ExchangeRateService.currentCurrencyRate, BigDecimal.ROUND_HALF_UP);
        return valueInUSD.multiply(ExchangeRateService.newCurrencyRate);
    }


    public ExchangeRateResponse getApi(String currentCurrency,String newCurrency){
        ExchangeRateResponse exchangeRateResponse = restTemplate.getForObject(EXCHANGE_RATE_API_URL, ExchangeRateResponse.class);
        if (exchangeRateResponse == null || !exchangeRateResponse.getRates().containsKey(newCurrency) || !exchangeRateResponse.getRates().containsKey(currentCurrency)) {
            throw new IllegalArgumentException("Invalid currency code provided.");
        }
        return exchangeRateResponse;
    }

}



