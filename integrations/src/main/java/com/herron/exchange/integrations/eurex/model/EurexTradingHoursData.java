package com.herron.exchange.integrations.eurex.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record EurexTradingHoursData(@JsonProperty("data") Data data) {

    public Map<String, EurexTradingHoursData.TradingHour> getByProductId() {
        return data().tradingHours().data().stream().collect(Collectors.toMap(EurexTradingHoursData.TradingHour::productId, Function.identity(), (o1, o2) -> o1));
    }

    public record Data(@JsonProperty("TradingHours") TradingHours tradingHours) {
    }

    public record TradingHours(@JsonProperty("date") String date,
                               @JsonProperty("data") List<TradingHour> data) {
    }

    public record TradingHour(
            @JsonProperty("StartContinuousTrading") String startContinuousTrading,
            @JsonProperty("ProductID") String productId,
            @JsonProperty("Product") String product,
            @JsonProperty("EndContinuousTrading") String eEndContinuousTrading,
            @JsonProperty("EndClosingAuction") String endClosingAuction,
            @JsonProperty("EndOpeningAuction") String endOpeningAuction) {
    }
}
