package com.herron.exchange.integrations.nasdaq.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public record TreasuryYieldData(@JsonProperty("dataset") Dataset dataset) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Dataset(
            @JsonProperty("id") int id,
            @JsonProperty("dataset_code") String dataset_code,
            @JsonProperty("database_code") String database_code,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("refreshed_at") String refreshed_at,
            @JsonProperty("newest_available_date") String newest_available_date,
            @JsonProperty("oldest_available_date") String oldest_available_date,
            @JsonProperty("column_names") List<String> column_names,
            @JsonProperty("frequency") String frequency,
            @JsonProperty("type") String type,
            @JsonProperty("premium") boolean premium,
            @JsonProperty("limit") String limit,
            @JsonProperty("transform") String transform,
            @JsonProperty("column_index") String column_index,
            @JsonProperty("start_date") String start_date,
            @JsonProperty("end_date") String end_date,
            @JsonProperty("data") List<List<Object>> data
    ) {
        public List<YieldCurveItem> getDataToYieldItems() {
            List<YieldCurveItem> yieldItems = new ArrayList<>();

            for (List<Object> dateData : data) {
                if (dateData.size() == 13) {
                    LocalDate date = LocalDate.parse((String) dateData.get(0));
                    double[] yieldValues = new double[12];

                    for (int i = 1; i <= 12; i++) {
                        yieldValues[i - 1] = ((Double) dateData.get(i)) / 100;
                    }

                    LocalDate[] maturities = Arrays.stream(NasdaqDataLinkMaturityDatesEnum.values())
                            .map(NasdaqDataLinkMaturityDatesEnum::getMaturityDateBuilder)
                            .map(builder -> builder.apply(date))
                            .toArray(LocalDate[]::new);

                    YieldCurveItem yieldItem = new YieldCurveItem(date, yieldValues, maturities);
                    yieldItems.add(yieldItem);
                }
            }

            return yieldItems;
        }
    }

    public record YieldCurveItem(LocalDate date,
                                 double[] yieldValues,
                                 LocalDate[] maturityDates) {
    }

    public enum NasdaqDataLinkMaturityDatesEnum {
        ONE_MONTH("1MO", (LocalDate startDate) -> startDate.plusMonths(1)),
        TWO_MONTH("2MO", (LocalDate startDate) -> startDate.plusMonths(2)),
        THREE_MONTH("3MO", (LocalDate startDate) -> startDate.plusMonths(3)),
        SIX_MONTH("6MO", (LocalDate startDate) -> startDate.plusMonths(6)),
        ONE_YEAR("1YR", (LocalDate startDate) -> startDate.plusYears(1)),
        TWO_YEAR("2YR", (LocalDate startDate) -> startDate.plusYears(2)),
        THREE_YEAR("3YR", (LocalDate startDate) -> startDate.plusYears(3)),
        FIVE_YEAR("5YR", (LocalDate startDate) -> startDate.plusYears(5)),
        SEVEN_YEAR("7YR", (LocalDate startDate) -> startDate.plusYears(7)),
        TEN_YEAR("10YR", (LocalDate startDate) -> startDate.plusYears(10)),
        TWENTY_YEAR("20YR", (LocalDate startDate) -> startDate.plusYears(20)),
        THIRTY_YEAR("30YR", (LocalDate startDate) -> startDate.plusYears(30));

        private final String maturityName;
        private final UnaryOperator<LocalDate> maturityDateBuilder;
        private static final Map<String, Function<LocalDate, LocalDate>> VALUES_BY_IDENTIFIER =
                stream(NasdaqDataLinkMaturityDatesEnum.values())
                        .collect(toMap(NasdaqDataLinkMaturityDatesEnum::getMaturityName, NasdaqDataLinkMaturityDatesEnum::getMaturityDateBuilder));

        NasdaqDataLinkMaturityDatesEnum(String maturityName, UnaryOperator<LocalDate> maturityDateBuilder) {
            this.maturityName = maturityName;
            this.maturityDateBuilder = maturityDateBuilder;
        }

        public static LocalDate createMaturityDate(final LocalDate startDate, final String maturityString) {
            return VALUES_BY_IDENTIFIER.containsKey(maturityString) ? VALUES_BY_IDENTIFIER.get(maturityString).apply(startDate) : startDate;
        }

        public String getMaturityName() {
            return maturityName;
        }

        public Function<LocalDate, LocalDate> getMaturityDateBuilder() {
            return maturityDateBuilder;
        }


    }
}
