package com.herron.exchange.integrations.generator.eurex.model;

public record EurexApiClientProperties(String url, String apiKey, int contractRequestLimit) {
}
