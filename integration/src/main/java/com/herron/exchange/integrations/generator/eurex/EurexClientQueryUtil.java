package com.herron.exchange.integrations.generator.eurex;

import com.herron.exchange.common.api.common.api.referencedata.exchange.BusinessCalendar;
import com.herron.exchange.common.api.common.api.referencedata.exchange.Market;
import com.herron.exchange.common.api.common.api.referencedata.exchange.Product;
import com.herron.exchange.common.api.common.api.referencedata.exchange.TradingCalendar;
import com.herron.exchange.common.api.common.api.referencedata.instruments.Instrument;
import com.herron.exchange.common.api.common.api.referencedata.orderbook.OrderbookData;
import com.herron.exchange.common.api.common.messages.refdata.*;
import com.herron.exchange.common.api.common.model.ImmutableHerronBusinessCalendar;
import com.herron.exchange.common.api.common.model.ImmutableHerronTradingCalendar;
import com.herron.exchange.integrations.generator.eurex.model.EurexContractData;
import com.herron.exchange.integrations.generator.eurex.model.EurexHolidayData;
import com.herron.exchange.integrations.generator.eurex.model.EurexProductData;
import com.herron.exchange.integrations.generator.eurex.model.EurexTradingHoursData;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.herron.exchange.common.api.common.enums.AuctionAlgorithmEnum.DUTCH;
import static com.herron.exchange.common.api.common.enums.MatchingAlgorithmEnum.FIFO;
import static com.herron.exchange.common.api.common.enums.MatchingAlgorithmEnum.PRO_RATA;
import static com.herron.exchange.common.api.common.enums.OptionExerciseTyleEnum.AMERICAN;
import static com.herron.exchange.common.api.common.enums.OptionExerciseTyleEnum.EUROPEAN;
import static com.herron.exchange.common.api.common.enums.OptionTypeEnum.CALL;
import static com.herron.exchange.common.api.common.enums.OptionTypeEnum.PUT;
import static com.herron.exchange.common.api.common.enums.SettlementTypeEnum.CASH;
import static com.herron.exchange.common.api.common.enums.SettlementTypeEnum.PHYSICAL;

public class EurexClientQueryUtil {

    public static String createTradingHoursQuery() {
        String data = "    ProductID\\n" +
                "    Product\\n" +
                "    EndOpeningAuction\\n" +
                "    EndClosingAuction\\n" +
                "    EndContinuousTrading\\n" +
                "    StartContinuousTrading";
        return String.format("{\"query\":\"query{TradingHours { date data { %s }}}\"}", data);
    }

    public static String createHolidayQuery() {
        String data = "    ProductID\\n" +
                "    Product\\n" +
                "    ExchangeHoliday\\n" +
                "    Holiday";
        return String.format("{\"query\":\"query{Holidays { date data { %s }}}\"}", data);
    }

    public static String createProductQuery() {
        String data = "    ProductID\\n" +
                "    Currency\\n" +
                "    Product\\n" +
                "    UnderlyingName\\n" +
                "    UnderlyingISIN\\n" +
                "    TickSize\\n" +
                "    TickValue\\n" +
                "    ContractSize\\n" +
                "    Underlying";
        return String.format("{\"query\":\"query{ProductInfos { date data { %s }}}\"}", data);
    }

    public static String createContractQuery(String product) {
        String data = " InstrumentID\\n" +
                "    ContractID\\n" +
                "    ProductID\\n" +
                "    ContractSize\\n" +
                "    CallPut\\n" +
                "    Strike\\n" +
                "    SettlementType\\n" +
                "    FirstTradingDate\\n" +
                "    ContractDate\\n" +
                "    ExpirationDate\\n" +
                "    LastTradingDate\\n" +
                "    OptionsDelta\\n" +
                "    ExerciseStyle\\n" +
                "    ISIN\\n" +
                "    PreviousDaySettlementPrice";
        return String.format("{\"query\":\"query{Contracts(filter: {Product: { eq: \\\"%s\\\" }}) { date data { %s }}}\"}", product, data);
    }
}
