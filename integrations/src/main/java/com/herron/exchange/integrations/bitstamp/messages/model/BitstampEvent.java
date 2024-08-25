package com.herron.exchange.integrations.bitstamp.messages.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.herron.exchange.common.api.common.websocket.api.WebsocketRequest;
import com.herron.exchange.common.api.common.websocket.api.WebsocketRequestKey;
import com.herron.exchange.integrations.bitstamp.enums.BitstampChannels;
import com.herron.exchange.integrations.bitstamp.messages.api.BitstampMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public record BitstampEvent(@JsonProperty("event") String event,
                            @JsonProperty("channel") String channel,
                            @JsonProperty("data") Map<String, Object> data) implements BitstampMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(BitstampEvent.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public WebsocketRequestKey createKey(String uri) {
        return new WebsocketRequest.DefaultRequestKey(uri, channel);
    }

    public static BitstampEvent from(String data) {
        return deserializeMessage(data, BitstampEvent.class);
    }

    public Optional<BitstampMessage> getMessage() {
        return getType().map(type -> {
            data.putIfAbsent("event", event);
            data.putIfAbsent("channel", channel);
            return deserializeMessage(data, type);
        });
    }

    private Optional<Class<? extends BitstampMessage>> getType() {
        if (channel == null || event == null) {
            throw new IllegalArgumentException("Channel and event must not be null");
        }

        if (data.isEmpty()) {
            return Optional.empty();
        }

        return switch (BitstampChannels.getValue(channel)) {
            case DETAIL_ORDER_BOOK -> Optional.of(BitstampDetailOrderbookEvent.class);
            case LIVE_TRADES -> Optional.of(BitstampLiveTradeEvent.class);
            case LIVE_ORDERS -> Optional.of(BitstampLiveOrderEvent.class);
            default -> Optional.empty();
        };
    }

    private static <T extends BitstampMessage> T deserializeMessage(Object message, Class<T> classToBeDecoded) {
        try {
            if (message instanceof String messageString) {
                return MAPPER.readValue(messageString, classToBeDecoded);
            } else if (message instanceof JsonNode node) {
                return MAPPER.treeToValue(node, classToBeDecoded);
            } else if (message instanceof Map<?, ?> map) {
                return MAPPER.convertValue(map, classToBeDecoded);
            }
            LOGGER.warn("Unable to map unhandled type: {}:", message);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Unable to map message {}: {}", message, e);
        }
        return null;
    }
}
