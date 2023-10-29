package com.herron.exchange.integrations.bitstamp.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.herron.exchange.integrations.bitstamp.messages.BitstampEventData;

public class BitstampJsonMessageDecoder {
    private final Class<? extends BitstampEventData> classToBeDecoded;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BitstampJsonMessageDecoder(Class<? extends BitstampEventData> classToBeDecoded) {
        this.classToBeDecoded = classToBeDecoded;
    }

    public BitstampEventData decodeMessage(final String message) {
        try {
            return objectMapper.readValue(message, classToBeDecoded);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
