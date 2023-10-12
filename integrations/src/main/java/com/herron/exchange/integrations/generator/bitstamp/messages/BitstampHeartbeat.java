package com.herron.exchange.integrations.generator.bitstamp.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record BitstampHeartbeat(@JsonProperty("data") Map<String, Object> data, @JsonProperty("channel") String channel, @JsonProperty("event") String event) {

    public boolean isSuccessful() {
        return data.get("status").equals("success");
    }
}
