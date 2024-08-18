package com.herron.exchange.integrations.bitstamp.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.herron.exchange.integrations.bitstamp.client.BitstampSubscription;
import com.herron.exchange.integrations.bitstamp.enums.EventDescriptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.herron.exchange.common.api.common.enums.OrderOperationEnum.*;

public record BitstampEventData(@JsonProperty("data") Map<String, Object> data, @JsonProperty("channel") String channel, @JsonProperty("event") String event) {

    private static final Logger LOGGER = LoggerFactory.getLogger(BitstampSubscription.class);

    public EventDescriptionEnum getEventDescriptionEnum() {
        return EventDescriptionEnum.getEventDescriptionEnum(event);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getChannel() {
        return channel;
    }

    public String getEvent() {
        return event;
    }

    public BitstampAddOrder getSnapshot() {
        try {
            return new BitstampAddOrder(data, channel, INSERT);
        } catch (Exception e) {
            LOGGER.warn("Unable to parse Order: Data: {}, Channel: {}, Event: {}", data, channel, event);
            return null;
        }
    }

    public BitstampAddOrder getAddOrder() {
        try {
            return new BitstampAddOrder(data, channel, INSERT);
        } catch (Exception e) {
            LOGGER.warn("Unable to parse Order: Data: {}, Channel: {}, Event: {}", data, channel, event);
            return null;
        }
    }

    public BitstampUpdateOrder getUpdateOrder() {
        try {
            return new BitstampUpdateOrder(data, channel, UPDATE);
        } catch (Exception e) {
            LOGGER.warn("Unable to parse Order: Data: {}, Channel: {}, Event: {}", data, channel, event);
            return null;
        }
    }

    public BitstampCancelOrder getCancelOrder() {
        try {
            return new BitstampCancelOrder(data, channel, CANCEL);
        } catch (Exception e) {
            LOGGER.warn("Unable to parse Order: Data: {}, Channel: {}, Event: {}", data, channel, event);
            return null;
        }
    }

    public BitstampTrade getTrade() {
        try {
            return new BitstampTrade(data, channel, event);
        } catch (Exception e) {
            LOGGER.warn("Unable to parse Trade: Data: {}, Channel: {}, Event: {}", data, channel, event);
            return null;
        }
    }

    public boolean isHeartbeatSuccessful() {
        try {
            return new BitstampHeartbeat(data, channel, event).isSuccessful();
        } catch (Exception e) {
            LOGGER.warn("Unable to parse Heartbeat: Data: {}, Channel: {}, Event: {}", data, channel, event);
            return false;
        }
    }

    @Override
    public String toString() {
        return "BitstampEvent{" +
                "eventDescriptionEnum=" + getEventDescriptionEnum() +
                ", data=" + data +
                ", channel='" + channel + '\'' +
                ", event='" + event + '\'' +
                '}';
    }
}
