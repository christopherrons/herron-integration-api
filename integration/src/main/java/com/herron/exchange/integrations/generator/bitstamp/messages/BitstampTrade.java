package com.herron.exchange.integrations.generator.bitstamp.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.herron.exchange.common.api.common.messages.common.Participant;
import com.herron.exchange.integrations.generator.bitstamp.api.BitstampMessage;

import java.util.Map;

import static com.herron.exchange.integrations.generator.bitstamp.utils.BitstampUtils.*;

public record BitstampTrade(Participant bidParticipant,
                            Participant askParticipant,
                            String tradeId,
                            String buyOrderId,
                            String askOrderId,
                            boolean isBidSideAggressor,
                            double volume,
                            double price,
                            long timeStampInMs,
                            String instrumentId,
                            String orderbookId) implements BitstampMessage {

    /*  Json Structure example of Bitstamp Live Trade
 {
    "data":{
       "id":216689415,
       "timestamp":"1641719887",
       "amount":500,
       "amount_str":"500.00000000",
       "price":0.74481,
       "price_str":"0.74481",
       "type":1,
       "microtimestamp":"1641719887925000",
       "buy_order_id":1445416603234305,
       "sell_order_id":1445416604745728
    },
    "channel":"live_trades_xrpusd",
    "event":"trade"
 }*/
    public BitstampTrade(@JsonProperty("data") Map<String, Object> data, @JsonProperty("channel") String channel, @JsonProperty("event") String event) {
        this(generateParticipant(),
                generateParticipant(),
                !data.isEmpty() ? (String) data.get("id") : "-1",
                !data.isEmpty() ? (String) data.get("buy_order_id") : "NONE",
                !data.isEmpty() ? (String) data.get("sell_order_id") : "NONE",
                !data.isEmpty() && (int) data.get("type") == 0,
                !data.isEmpty() ? Double.parseDouble((String) data.get("amount_str")) : -1.0,
                !data.isEmpty() ? Double.parseDouble((String) data.get("price_str")) : 0,
                !data.isEmpty() ? Long.parseLong((String) data.get("microtimestamp")) / 1000 : -1L,
                createInstrumentId(channel),
                createOrderbookId(channel)
        );
    }
}
