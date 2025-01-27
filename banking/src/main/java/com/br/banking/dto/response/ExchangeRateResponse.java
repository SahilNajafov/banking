package com.br.banking.dto.response;

import java.math.BigDecimal;
import java.util.Map;

public class ExchangeRateResponse {
    private Map<String, BigDecimal> rates;

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
