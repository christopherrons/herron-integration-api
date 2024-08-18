package com.herron.exchange.integrations.bitstamp.enums;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

public enum EventDescriptionEnum {
    INVALID_EVENT_DESCRIPTION("invalid"),
    ORDER_CREATED("order_created"),
    ORDER_DELETED("order_deleted"),
    ORDER_UPDATED("order_changed"),
    TRADE("trade"),
    DATA("data"),
    SUBSCRIPTION_SUCCEEDED("bts:subscription_succeeded"),
    FORCED_RECONNECT("bts:request_reconnect"),
    HEART_BEAT("bts:heartbeat");

    private static final Map<String, EventDescriptionEnum> VALUES_BY_IDENTIFIER = stream(EventDescriptionEnum.values()).
            collect(toMap(EventDescriptionEnum::getEventDescription, identity()));
    private final String eventDescription;


    EventDescriptionEnum(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public static EventDescriptionEnum getEventDescriptionEnum(String eventDescription) {
        return VALUES_BY_IDENTIFIER.getOrDefault(eventDescription, INVALID_EVENT_DESCRIPTION);
    }

    public String getEventDescription() {
        return eventDescription;
    }
}
