package com.herron.exchange.integrations.generator.bitstamp;

import com.herron.exchange.integrations.generator.bitstamp.api.BitstampMessage;
import com.herron.exchange.integrations.generator.bitstamp.client.BitstampSubscription;
import com.herron.exchange.integrations.generator.bitstamp.model.BitstampWebsocketRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class BitstampWebsocketClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitstampWebsocketClient.class);
    private final Map<BitstampWebsocketRequest, BitstampSubscription> requestToSubscription = new ConcurrentHashMap<>();

    public void subscribe(Consumer<BitstampMessage> messageConsumer, BitstampWebsocketRequest request) {
        requestToSubscription.computeIfAbsent(request, k -> {
            var subscription = new BitstampSubscription(messageConsumer, request);
            subscription.subscribe();
            return subscription;
        });
    }

    public void destroy() {
        requestToSubscription.values().forEach(BitstampSubscription::unsubscribe);
    }

}
