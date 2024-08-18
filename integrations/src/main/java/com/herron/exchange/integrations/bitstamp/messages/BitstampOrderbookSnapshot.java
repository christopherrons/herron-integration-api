package com.herron.exchange.integrations.bitstamp.messages;

import java.util.List;

public record BitstampOrderbookSnapshot(List<BitstampAddOrder> bids, List<BitstampAddOrder> ask, long timeStampInMs) {

}
