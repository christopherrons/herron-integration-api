package com.herron.exchange.integrations.generator.bitstamp.config;

import com.herron.exchange.common.api.common.enums.KafkaTopicEnum;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.bitstamp-market-data.nr-of-partitions}")
    public int nrOfPartitions;

    @Bean
    public NewTopic bitstampMarketDataTopic() {
        return TopicBuilder
                .name(KafkaTopicEnum.BITSTAMP_MARKET_DATA.getTopicName())
                .partitions(nrOfPartitions)
                .build();
    }
}
