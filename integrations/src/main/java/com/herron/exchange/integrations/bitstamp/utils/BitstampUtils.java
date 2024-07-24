package com.herron.exchange.integrations.bitstamp.utils;

import com.herron.exchange.common.api.common.enums.OrderTypeEnum;
import com.herron.exchange.common.api.common.enums.TimeInForceEnum;
import com.herron.exchange.common.api.common.messages.common.Participant;
import net.datafaker.Faker;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.herron.exchange.common.api.common.enums.OrderTypeEnum.LIMIT;
import static com.herron.exchange.common.api.common.enums.OrderTypeEnum.MARKET;
import static com.herron.exchange.common.api.common.enums.TimeInForceEnum.*;

public class BitstampUtils {
    private static final Faker NAME_FAKER = new Faker();
    private static final List<String> USER_POOL = IntStream.range(0, 2000).mapToObj(k -> generateUser()).toList();

    private static final List<TimeInForceEnum> TIME_IN_FORCE = List.of(FOK, FAK, SESSION);
    private static final List<OrderTypeEnum> ORDER_TYPES = List.of(MARKET, LIMIT);
    private static final Random RANDOM_UNIFORM = new Random();

    public static String createInstrumentId(String channel) {
        return String.format("equity_%s", channel.split("_")[2]);
    }

    public static String createOrderbookId(String channel) {
        return String.format("bitstamp_%s", createInstrumentId(channel));
    }

    public static OrderTypeEnum generateOrderType() {
        return ORDER_TYPES.get(RANDOM_UNIFORM.nextInt(ORDER_TYPES.size()));
    }

    public static TimeInForceEnum generateTimeInForce() {
        return TIME_IN_FORCE.get(RANDOM_UNIFORM.nextInt(TIME_IN_FORCE.size()));
    }

    public static String generateUser() {
        return String.format("%s%s", NAME_FAKER.funnyName().name(), NAME_FAKER.name().lastName());
    }

    public static String getUserFromPool() {
        return USER_POOL.get(RANDOM_UNIFORM.nextInt(0, USER_POOL.size()));
    }

    public static Participant generateParticipant() {
        return new Participant("Bistamp", generateUser());
    }

}
