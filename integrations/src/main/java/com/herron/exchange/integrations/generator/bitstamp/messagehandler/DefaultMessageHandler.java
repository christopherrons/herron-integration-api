package com.herron.exchange.integrations.generator.bitstamp.messagehandler;

import com.herron.event.generator.server.api.MessageHandler;
import com.herron.event.generator.server.messages.BitstampAddOrder;
import com.herron.event.generator.server.messages.BitstampTrade;
import com.herron.exchange.common.api.common.api.Message;
import com.herron.exchange.common.api.common.comparator.MessageComparator;
import com.herron.exchange.common.api.common.datastructures.TimeBoundBlockingPriorityQueue;
import com.herron.exchange.common.api.common.logging.EventLogger;
import com.herron.exchange.common.api.common.messages.HerronBroadcastMessage;
import com.herron.exchange.common.api.common.model.PartitionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultMessageHandler implements MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMessageHandler.class);
    private static final int TIME_IN_QUEUE_MS = 10000;
    private final EventLogger eventLogging;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Set<String> orderIds = new HashSet<>();
    private final Map<PartitionKey, TimeBoundBlockingPriorityQueue<Message>> partitionKeyToEventPriorityQueue = new ConcurrentHashMap<>();
    private final Map<PartitionKey, AtomicLong> partitionKeyToSequenceNumberHandler = new ConcurrentHashMap<>();

    public DefaultMessageHandler(KafkaTemplate<String, Object> kafkaTemplate) {
        this(kafkaTemplate, new EventLogger());
    }

    public DefaultMessageHandler(KafkaTemplate<String, Object> kafkaTemplate, EventLogger eventLogging) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventLogging = eventLogging;
    }

    public void handleMessage(Message message, PartitionKey partitionKey) {
        if (message == null) {
            return;
        }
        TimeBoundBlockingPriorityQueue<Message> queue = findOrCreateQueue(partitionKey);
        try {
            var messages = queue.addItemThenPurge(message);
            handleMessages(messages, partitionKey);
        } catch (Exception e) {
            LOGGER.warn("Unable to handle message: {}. {}", message, e);
        }
    }

    public void handleMessages(List<Message> messages, PartitionKey partitionKey) {
        for (var message : messages) {
            if (message == null) {
                continue;
            }

            if (message instanceof BitstampAddOrder order) {
                // Since we have our own trading engine we only handle add orders
                handleOrder(order, partitionKey);
            } else if (message instanceof BitstampTrade trade) {
                handleTrade(trade, partitionKey);
            } else {
                publish(message, partitionKey);
            }
        }
    }

    private void handleOrder(BitstampAddOrder order, PartitionKey partitionKey) {
        if (order.currentVolume() <= 0 || order.price() <= 0) {
            return;
        }
        publish(order, partitionKey);
    }

    private void handleTrade(BitstampTrade trade, PartitionKey partitionKey) {
        // We do not want to process trades where we have never seen the order
        if (orderIds.contains(trade.askOrderId()) && orderIds.contains(trade.buyOrderId())) {
            publish(trade, partitionKey);
        }
    }

    private synchronized void publish(Message message, PartitionKey partitionKey) {
        var broadCast = new HerronBroadcastMessage(message, message.messageType().getMessageTypeId(), getSequenceNumber(partitionKey), Instant.now().toEpochMilli());
        kafkaTemplate.send(partitionKey.topicEnum().getTopicName(), partitionKey.partitionId(), broadCast.messageType().getMessageTypeId(), broadCast);
        eventLogging.logEvent();
    }

    private TimeBoundBlockingPriorityQueue<Message> findOrCreateQueue(PartitionKey partitionKey) {
        return partitionKeyToEventPriorityQueue.computeIfAbsent(
                partitionKey,
                e -> new TimeBoundBlockingPriorityQueue<>(TIME_IN_QUEUE_MS, new MessageComparator<>())
        );
    }

    private long getSequenceNumber(PartitionKey partitionKey) {
        return partitionKeyToSequenceNumberHandler.computeIfAbsent(partitionKey, k -> new AtomicLong(1)).getAndIncrement();
    }

}

