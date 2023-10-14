package com.herron.exchange.integrations.generator.eurex;

import com.herron.exchange.integrations.generator.eurex.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.herron.exchange.common.api.common.http.HttpRequestHandler.postRequest;

public class EurexReferenceDataApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(EurexReferenceDataApiClient.class);
    private static final String API_KEY_HEADER = "X-DBP-APIKEY";
    private final String apiKey;
    private final String apiUrl;
    private final int contractRequestLimit;

    public EurexReferenceDataApiClient(EurexApiClientProperties eurexApiClientProperties) {
        this.apiKey = eurexApiClientProperties.apiKey();
        this.apiUrl = eurexApiClientProperties.url();
        this.contractRequestLimit = eurexApiClientProperties.contractRequestLimit();
    }

    public List<EurexContractData> fetchContractData(EurexProductData productData) {
        LOGGER.info("Fetching contract data.");
        return productData.data().productInfos().data().subList(0, contractRequestLimit).stream()
                .map(productInfo -> postRequest(apiUrl, EurexClientQueryUtil.createContractQuery(productInfo.product()), Map.of(API_KEY_HEADER, apiKey), EurexContractData.class))
                .filter(Objects::nonNull)
                .toList();
    }

    public EurexProductData fetchProductData() {
        LOGGER.info("Fetching product data.");
        return postRequest(apiUrl, EurexClientQueryUtil.createProductQuery(), Map.of(API_KEY_HEADER, apiKey), EurexProductData.class);
    }

    public EurexHolidayData fetchHolidayData() {
        LOGGER.info("Fetching holiday data.");
        return postRequest(apiUrl, EurexClientQueryUtil.createHolidayQuery(), Map.of(API_KEY_HEADER, apiKey), EurexHolidayData.class);
    }

    public EurexTradingHoursData fetchTradingHourData() {
        LOGGER.info("Fetching trading hour data.");
        return postRequest(apiUrl, EurexClientQueryUtil.createTradingHoursQuery(), Map.of(API_KEY_HEADER, apiKey), EurexTradingHoursData.class);
    }
}