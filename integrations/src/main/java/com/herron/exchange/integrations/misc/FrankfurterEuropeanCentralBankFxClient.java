package com.herron.exchange.integrations.misc;

import com.herron.exchange.common.api.common.http.HttpRequestHandler;
import com.herron.exchange.integrations.misc.model.ExchangeRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//https://www.frankfurter.app/docs/
public class FrankfurterEuropeanCentralBankFxClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrankfurterEuropeanCentralBankFxClient.class);
    private final String url;

    public FrankfurterEuropeanCentralBankFxClient(String url) {
        this.url = url;
    }

    public ExchangeRateResponse requestLatestRates(String baseCurrency) {
        LOGGER.info("Requesting latest quotes with base currency {}", baseCurrency);
        return HttpRequestHandler.getRequest(String.format("%s/latest?base=%s", url, baseCurrency.toUpperCase()), ExchangeRateResponse.class);
    }
}
