package com.herron.exchange.integrations.bitstamp.messages.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.herron.exchange.integrations.bitstamp.messages.api.BitstampMessage;

import java.util.List;

import static com.herron.exchange.integrations.bitstamp.utils.BitstampUtils.createInstrumentId;
import static com.herron.exchange.integrations.bitstamp.utils.BitstampUtils.createOrderbookId;

public record BitstampDetailOrderbookEvent(@JsonProperty("event") String event,
                                           @JsonProperty("channel") String channel,
                                           @JsonProperty("timestamp") long timestamp,
                                           @JsonProperty("microtimestamp") long microtimestamp,
                                           @JsonProperty("bids") List<List<String>> bids,
                                           @JsonProperty("asks") List<List<String>> asks) implements BitstampMessage {

    public Order getBestBid() {
        var list =  bids.getFirst();
        return new Order(Double.parseDouble(list.getFirst()), Double.parseDouble(list.get(1)), list.get(2));
    }

    public Order getBestAsk() {
        var list =  asks.getFirst();
        return new Order(Double.parseDouble(list.getFirst()), Double.parseDouble(list.get(1)), list.get(2));
    }

    public List<Order> getBids() {
        return bids.stream()
                .map(list -> new Order(Double.parseDouble(list.getFirst()), Double.parseDouble(list.get(1)), list.get(2)))
                .toList();
    }

    public List<Order> getAsks() {
        return asks.stream()
                .map(list -> new Order(Double.parseDouble(list.getFirst()), Double.parseDouble(list.get(1)), list.get(2)))
                .toList();
    }

    public String instrumentId() {
        return createInstrumentId(channel);
    }

    public String orderbookId() {
        return createOrderbookId(channel);
    }

    public long timeStampInMs() {
        return microtimestamp / 1000;
    }

    public record Order(double price, double volume, String orderId) {
    }
}
