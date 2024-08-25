package com.herron.exchange.integrations.bitstamp.client;

import com.herron.exchange.common.api.common.websocket.api.WebsocketRequest;

import java.util.function.Consumer;

public record BitstampWebsocketRequest(String uri, String topic, Consumer<Object> consumer, RequestType requestType) implements WebsocketRequest {

    public enum RequestType {
        SUBSCRIBE("bts:subscribe"),
        UNSUBSCRIBE("bts:unsubscribe"),
        HEART_BEAT("bts:heartbeat");

        private final String message;

        RequestType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public record RequestMessage(String event,
                                 Data data) {
    }

    public record Data(String channel) {
    }

    @Override
    public RequestMessage createRequestMessage() {
        return new RequestMessage(requestType.getMessage(), new Data(topic));
    }
}
