package com.herron.exchange.integrations.eurex;


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
                "    Name\\n" +
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
                "    ProductLine\\n" +
                "    ISIN\\n" +
                "    PreviousDaySettlementPrice";
        return String.format("{\"query\":\"query{Contracts(filter: {Product: { eq: \\\"%s\\\" }}) { date data { %s }}}\"}", product, data);
    }
}
