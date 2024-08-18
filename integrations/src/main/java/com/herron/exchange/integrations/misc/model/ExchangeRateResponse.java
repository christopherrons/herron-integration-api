package com.herron.exchange.integrations.misc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ExchangeRateResponse(
        @JsonProperty("amount") double amount,
        @JsonProperty("base") String base,
        @JsonProperty("date") String date,
        @JsonProperty("rates") Map<String, Double> rates
) {
}