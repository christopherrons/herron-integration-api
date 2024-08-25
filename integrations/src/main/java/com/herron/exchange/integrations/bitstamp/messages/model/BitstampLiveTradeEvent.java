package com.herron.exchange.integrations.bitstamp.messages.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.herron.exchange.common.api.common.messages.common.Member;
import com.herron.exchange.common.api.common.messages.common.Participant;
import com.herron.exchange.common.api.common.messages.common.User;
import com.herron.exchange.integrations.bitstamp.messages.api.BitstampMessage;

import static com.herron.exchange.integrations.bitstamp.utils.BitstampUtils.createInstrumentId;
import static com.herron.exchange.integrations.bitstamp.utils.BitstampUtils.createOrderbookId;

public record BitstampLiveTradeEvent(@JsonProperty("event") String event,
                                     @JsonProperty("channel") String channel,
                                     @JsonProperty("id") int id,
                                     @JsonProperty("timestamp") String timestamp,
                                     @JsonProperty("amount") double amount,
                                     @JsonProperty("amount_str") String amountStr,
                                     @JsonProperty("price") double price,
                                     @JsonProperty("price_str") String priceStr,
                                     @JsonProperty("type") int type,
                                     @JsonProperty("microtimestamp") long microtimestamp,
                                     @JsonProperty("buy_order_id") String buyOrderId,
                                     @JsonProperty("sell_order_id") String sellOrderId) implements BitstampMessage {

    public Participant bidParticipant() {
        return new Participant(
                new Member("Bitstamp"),
                new User("anonymous-bid")
        );
    }

    public Participant askParticipant() {
        return new Participant(
                new Member("Bitstamp"),
                new User("anonymous-ask")
        );
    }

    public long timeStampInMs() {
        return microtimestamp / 1000;
    }

    public boolean isBidSideAggressor() {
        return type == 0;
    }

    public String instrumentId() {
        return createInstrumentId(channel);
    }

    public String orderbookId() {
        return createOrderbookId(channel);
    }
}
