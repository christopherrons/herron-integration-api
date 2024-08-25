package com.herron.exchange.integrations.bitstamp.enums;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

public enum BitstampOrderOperations {
    INVALID_ORDER_OPERATION("invalid"),
    ORDER_CREATED("order_created"),
    ORDER_DELETED("order_deleted"),
    ORDER_UPDATED("order_changed");

    private static final Map<String, BitstampOrderOperations> VALUES_BY_IDENTIFIER = stream(BitstampOrderOperations.values()).
            collect(toMap(BitstampOrderOperations::getValue, identity()));
    private final String value;

    BitstampOrderOperations(String value) {
        this.value = value;
    }

    public static BitstampOrderOperations getValue(String value) {
        return VALUES_BY_IDENTIFIER.getOrDefault(value, INVALID_ORDER_OPERATION);
    }

    public String getValue() {
        return value;
    }
}
