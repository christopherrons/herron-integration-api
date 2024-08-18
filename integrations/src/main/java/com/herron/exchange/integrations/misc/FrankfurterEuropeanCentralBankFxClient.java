package com.herron.exchange.integrations.misc;

import com.herron.exchange.common.api.common.http.HttpRequestHandler;
import com.herron.exchange.integrations.misc.model.ExchangeRateResponse;

//https://www.frankfurter.app/docs/
public class FrankfurterEuropeanCentralBankFxClient {

    private final String url;

    public FrankfurterEuropeanCentralBankFxClient(String url) {
        this.url = url;
    }

    public ExchangeRateResponse requestLatestRates(String baseCurrency) {
        return HttpRequestHandler.getRequest(String.format("%s/latest?base=%s", url, baseCurrency.toUpperCase()), ExchangeRateResponse.class);
    }
}
