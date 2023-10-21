package com.herron.exchange.integrations.bitstamp.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.herron.exchange.common.api.common.enums.*;
import com.herron.exchange.common.api.common.messages.common.Participant;
import com.herron.exchange.integrations.bitstamp.api.BitstampMessage;

import java.util.Map;

import static com.herron.exchange.integrations.bitstamp.utils.BitstampUtils.*;


public record BitstampAddOrder(OrderOperationEnum orderOperation,
                               Participant participant,
                               String orderId,
                               OrderSideEnum orderSide,
                               double initialVolume,
                               double currentVolume,
                               double price,
                               long timeStampInMs,
                               String instrumentId,
                               String orderbookId,
                               TimeInForceEnum timeInForce,
                               OrderTypeEnum orderType,
                               OrderOperationCauseEnum orderOperationCauseEnum) implements BitstampMessage {

    /*  Json Structure example of Bitstamp Live Order
{
     "data": {
             "id": 1441935087595520,
             "id_str": "1441935087595520",
             "order_type": 1,
             "datetime": "1640869916",
             "microtimestamp": "1640869915619000",
             "amount": 2162.61269107,
             "amount_str": "2162.61269107",
             "price": 0.84604,
             "price_str": "0.84604"
 },
     "channel": "live_orders_xrpusd",
     "event": "order_deleted"
 }*/
    public BitstampAddOrder(@JsonProperty("data") Map<String, Object> data, @JsonProperty("channel") String channel, OrderOperationEnum orderOperation) {
        this(orderOperation,
                generateParticipant(),
                !data.isEmpty() ? (String) data.get("id_str") : "NONE",
                !data.isEmpty() ? OrderSideEnum.fromValue((int) data.get("order_type")) : null,
                !data.isEmpty() ? Double.parseDouble((String) data.get("amount_str")) : -1.0,
                !data.isEmpty() ? Double.parseDouble((String) data.get("amount_str")) : -1.0,
                !data.isEmpty() ? Double.parseDouble((String) data.get("price_str")) : 0,
                !data.isEmpty() ? Long.parseLong((String) data.get("microtimestamp")) / 1000 : -1L,
                createInstrumentId(channel),
                createOrderbookId(channel),
                generateTimeInForce(),
                Double.parseDouble((String) data.get("price_str")) <= 99_999_999.0 ? OrderTypeEnum.LIMIT : OrderTypeEnum.MARKET,
                OrderOperationCauseEnum.NEW_ORDER
        );
    }
}
