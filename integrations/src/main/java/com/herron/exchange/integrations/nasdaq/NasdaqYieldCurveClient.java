package com.herron.exchange.integrations.nasdaq;

import com.herron.exchange.integrations.nasdaq.model.NasdaqDataLinkApiClientProperties;
import com.herron.exchange.integrations.nasdaq.model.TreasuryYieldData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.herron.exchange.common.api.common.http.HttpRequestHandler.getRequest;

public class NasdaqYieldCurveClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NasdaqYieldCurveClient.class);
    private final String apiKey;
    private final String apiUrl;

    public NasdaqYieldCurveClient(NasdaqDataLinkApiClientProperties apiClientProperties) {
        this.apiKey = apiClientProperties.apiKey();
        this.apiUrl = apiClientProperties.url();
    }

    public TreasuryYieldData requestYieldCurveData(LocalDate from, LocalDate to) {
        LOGGER.info("Fetching treasury yields from {} to {}", from, to);
        return getRequest(String.format("%s?start_date=%s?end_date=%s?api_key=%s", apiUrl, from.toString(), to.toString(), apiKey), TreasuryYieldData.class);
    }

}
