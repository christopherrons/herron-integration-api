package com.herron.exchange.integrations.generator.bitstamp;

import com.herron.event.generator.server.api.MessageHandler;
import com.herron.event.generator.server.client.BitstampSubscription;
import com.herron.event.generator.server.config.BitstampConsumerConfig;
import com.herron.event.generator.server.messages.BitstampOrderbookData;
import com.herron.event.generator.server.messages.BitstampStateChange;
import com.herron.event.generator.server.messages.BitstampStockInstrument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.herron.event.generator.server.utils.BitstampUtils.*;

public class BitstampConsumer {

    private final MessageHandler messageHandler;
    private static final Logger LOGGER = LoggerFactory.getLogger(BitstampConsumer.class);
    private final BitstampConsumerConfig.SubscriptionDetailConfig subscriptionDetailConfig;
    private final Map<String, BitstampSubscription> keyToSubscription = new ConcurrentHashMap<>();

    public BitstampConsumer(BitstampConsumerConfig.SubscriptionDetailConfig subscriptionDetailConfig, MessageHandler messageHandler) {
        this.subscriptionDetailConfig = subscriptionDetailConfig;
        this.messageHandler = messageHandler;
    }

    public void init() {
        Set<String> processedChannels = new HashSet<>();
        for (var channel : subscriptionDetailConfig.getChannels()) {
            if (processedChannels.contains(channel)) {
                throw new IllegalArgumentException(String.format("Duplicate subscription detail: %s", channel));
            }
            processedChannels.add(channel);
            initOrderbook(channel);
        }

        for (var channel : subscriptionDetailConfig.getChannels()) {
            try {
                var subscription = new BitstampSubscription(
                        messageHandler,
                        channel,
                        subscriptionDetailConfig.getUri()
                );
                subscription.subscribe();
                keyToSubscription.computeIfAbsent(channel, k -> subscription);

            } catch (DeploymentException | IOException | URISyntaxException e) {
                LOGGER.error("Unable to subscribe to channel() {}: {}", channel, e);
            }
        }
    }

    private void initOrderbook(String channel) {
        var instrument = new BitstampStockInstrument(createInstrumentId(channel), Instant.now().toEpochMilli());
        String tradingCurrency = channel.split("_")[2].substring(3, 6);

        var orderbook = new BitstampOrderbookData(createOrderbookId(channel), instrument.instrumentId(),
                tradingCurrency.equals("eur") ? MatchingAlgorithmEnum.FIFO : MatchingAlgorithmEnum.PRO_RATA, tradingCurrency, 0, Instant.now().toEpochMilli());

        var stateChange = new BitstampStateChange(orderbook.orderbookId(), StateChangeTypeEnum.CONTINUOUS_TRADING, Instant.now().toEpochMilli());

        messageHandler.handleMessages(List.of(instrument, orderbook, stateChange), getPartitionKey(channel));
        LOGGER.info("Init Orderbook for detail complete: {}", channel);
    }

}
