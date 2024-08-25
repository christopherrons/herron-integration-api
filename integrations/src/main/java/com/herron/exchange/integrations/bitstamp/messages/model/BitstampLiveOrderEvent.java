package com.herron.exchange.integrations.bitstamp.messages.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.herron.exchange.common.api.common.enums.*;
import com.herron.exchange.common.api.common.messages.common.Member;
import com.herron.exchange.common.api.common.messages.common.Participant;
import com.herron.exchange.common.api.common.messages.common.User;
import com.herron.exchange.integrations.bitstamp.messages.api.BitstampMessage;

import static com.herron.exchange.integrations.bitstamp.utils.BitstampUtils.createInstrumentId;
import static com.herron.exchange.integrations.bitstamp.utils.BitstampUtils.createOrderbookId;

public record BitstampLiveOrderEvent(@JsonProperty("event") String event,
                                     @JsonProperty("channel") String channel,
                                     @JsonProperty("id") long id,
                                     @JsonProperty("id_str") String idStr,
                                     @JsonProperty("order_type") int orderType,
                                     @JsonProperty("datetime") String datetime,
                                     @JsonProperty("microtimestamp") long microtimestamp,
                                     @JsonProperty("amount") double amount,
                                     @JsonProperty("amount_str") String amountStr,
                                     @JsonProperty("amount_at_create") double amountAtCreate,
                                     @JsonProperty("amount_traded") double amountTraded,
                                     @JsonProperty("price") double price,
                                     @JsonProperty("price_str") String priceStr) implements BitstampMessage {

    public OrderOperationEnum orderOperation() {
        return OrderOperationEnum.fromValue(event);
    }

    public Participant participant() {
        return new Participant(
                new Member("Bitstamp"),
                new User("anonymous")
        );
    }

    public OrderSideEnum orderSide() {
        return OrderSideEnum.fromValue(orderType);
    }

    public long timeStampInMs() {
        return microtimestamp / 1000;
    }

    public String instrumentId() {
        return createInstrumentId(channel);
    }

    public String orderbookId() {
        return createOrderbookId(channel);
    }

    public TimeInForceEnum timeInForce() {
        return TimeInForceEnum.SESSION;
    }

    public OrderTypeEnum orderTypeEnum() {
        return price <= 99_999_999.0 ? OrderTypeEnum.LIMIT : OrderTypeEnum.MARKET;
    }

    public OrderOperationCauseEnum orderOperationCause() {
        return switch (orderOperation()) {
            case INSERT -> OrderOperationCauseEnum.NEW_ORDER;
            case UPDATE -> OrderOperationCauseEnum.EXTERNAL_UPDATE;
            case CANCEL -> OrderOperationCauseEnum.FILLED;
        };
    }
}
