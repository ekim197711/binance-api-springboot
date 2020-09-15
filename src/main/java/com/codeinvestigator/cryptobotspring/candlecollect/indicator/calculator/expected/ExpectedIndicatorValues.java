package com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.expected;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;

import java.util.List;

import static java.time.temporal.ChronoUnit.MINUTES;

public class ExpectedIndicatorValues {
    private DataExpected dataExpected;
    private CandleItem candleItem;


    public ExpectedIndicatorValues(DataExpected dataExpected, List<CandleItem> history) {
        this.dataExpected = dataExpected;
        this.candleItem = history
                .stream()
                .filter(ci -> ci
                .openDateTime()
                .minusHours(2)
                .until(dataExpected.getLocalDateTime(), MINUTES) < 10)
                .findFirst()
                .get();
    }
}
