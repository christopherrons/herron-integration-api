package com.herron.exchange.integrations.bitstamp.enums;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

public enum BitstampChannels {

    INVALID_BITSTAMP_CHANNEL("invalid"),
    LIVE_ORDERS("live_orders"),
    LIVE_TRADES("live_trades"),
    DETAIL_ORDER_BOOK("detail_order_book");

    private static final Map<String, BitstampChannels> VALUES_BY_IDENTIFIER = stream(BitstampChannels.values()).
            collect(toMap(BitstampChannels::getValue, identity()));
    private final String value;

    BitstampChannels(String value) {
        this.value = value;
    }

    public static BitstampChannels getValue(String value) {
        int index = value.lastIndexOf('_');
        if (index != -1) {
            return VALUES_BY_IDENTIFIER.getOrDefault(value.substring(0, index), INVALID_BITSTAMP_CHANNEL);
        }
        return VALUES_BY_IDENTIFIER.getOrDefault(value, INVALID_BITSTAMP_CHANNEL);
    }

    public String getValue() {
        return value;
    }
}
