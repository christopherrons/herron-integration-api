package com.herron.exchange.integrations.generator.bitstamp.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.herron.exchange.common.api.common.enums.*;
import com.herron.exchange.common.api.common.messages.common.Participant;
import com.herron.exchange.integrations.generator.bitstamp.api.BitstampMessage;

import java.util.Map;

import static com.herron.exchange.integrations.generator.bitstamp.utils.BitstampUtils.*;

public record BitstampCancelOrder(OrderOperationEnum orderOperation,
                                  Participant participant,
                                  String orderId,
                                  OrderSideEnum orderSide,
                                  double initialVolume,
                                  double currentVolume,
                                  double monetaryAmount,
                                  long timeStampInMs,
                                  String instrumentId,
                                  String orderbookId,
                                  TimeInForceEnum timeInForceEnum,
                                  OrderTypeEnum orderType,
                                  OrderCancelOperationTypeEnum cancelOperationType) implements BitstampMessage {

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
    public BitstampCancelOrder(@JsonProperty("data") Map<String, Object> data, @JsonProperty("channel") String channel, @JsonProperty("event") String event) {
        this(OrderOperationEnum.extractValue(event),
                generateParticipant(),
                !data.isEmpty() ? (String) data.get("id_str") : "NONE",
                !data.isEmpty() ? OrderSideEnum.fromValue((int) data.get("order_type")) : OrderSideEnum.INVALID_ORDER_SIDE,
                !data.isEmpty() ? Double.parseDouble((String) data.get("amount_str")) : -1.0,
                !data.isEmpty() ? Double.parseDouble((String) data.get("amount_str")) : -1.0,
                !data.isEmpty() ? Double.parseDouble((String) data.get("price_str")) : 0,
                !data.isEmpty() ? Long.parseLong((String) data.get("microtimestamp")) / 1000 : -1L,
                createInstrumentId(channel),
                createOrderbookId(channel),
                generateTimeInForce(),
                generateOrderType(),
                OrderCancelOperationTypeEnum.KILLED
        );
    }
}
