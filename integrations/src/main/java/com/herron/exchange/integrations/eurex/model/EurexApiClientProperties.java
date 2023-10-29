package com.herron.exchange.integrations.eurex.model;

public record EurexApiClientProperties(String url, String apiKey, int contractRequestLimit) {
}
