package com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.average;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.Indicator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class AverageComputation {
    private final List<CandleItem> history;
    private final CandleItem item;
    private final CandleItem itemPrev;
    private final static List<Integer> MOVINGAVERAGE_PERIODS;
    private final static List<Integer> EXPONENTIAL_MOVINGAVERAGE_PERIODS;
    static {
        MOVINGAVERAGE_PERIODS = List.of(7,12,20,26,50,99,200);
        EXPONENTIAL_MOVINGAVERAGE_PERIODS = List.of(7, 12,26);
    }

    public AverageComputation(CandleItem item) {
        this.item = item;
        this.history = null;
        this.itemPrev = null;
    }

    public AverageIndicator calculate(){
        if (itemPrev == null || history == null)
            throw new IllegalStateException("It looks like this computatation is made to calculate the dummy / initial value.");
        return AverageIndicator.builder()
                .movingAverages(calculateMovingAverages(history))
                .movingAverageConvergenceDivergence(calculateMovingAverageConvergenceDivergence())
                .exponentialMovingAverages(calculateExponentialMovingAverages(
                        history, item, itemPrev))
                .build();
    }

    public Map<Integer, BigDecimal> calculateMovingAverages(List<CandleItem> history) {
        Map<Integer, BigDecimal> movingAverages = MOVINGAVERAGE_PERIODS.stream().collect(
                Collectors.toMap(i -> i, i -> BigDecimal.ZERO)
        );
        for (int i = 0; i < 200 && i < history.size(); i++) {
            CandleItem candleItem = history.get(history.size() - i - 1);
            for (Integer key : MOVINGAVERAGE_PERIODS) {
                if (i < key) {
                    movingAverages.put(key, movingAverages.get(key).add(candleItem.getClose()));
                }
            }
        }

        for (Integer key : MOVINGAVERAGE_PERIODS) {
            BigDecimal divide = movingAverages.get(key)
                    .divide(BigDecimal.valueOf(key), Indicator.BD_SCALE, RoundingMode.HALF_UP);
            movingAverages.put(key, divide);
        }
        return movingAverages;
    }

    public BigDecimal calculateMovingAverageConvergenceDivergence() {
        Map<Integer, BigDecimal> exponentialMovingAverages = EXPONENTIAL_MOVINGAVERAGE_PERIODS.stream().
                collect(
                        Collectors.toMap(k -> k, v -> BigDecimal.ZERO)
                );
        return exponentialMovingAverages.get(12).subtract(exponentialMovingAverages.get(26));
    }

    public Map<Integer, BigDecimal> calculateExponentialMovingAverages(
                                                                       List<CandleItem> history, CandleItem item, CandleItem prev) {
        Map<Integer, BigDecimal> exponentialMovingAverages = EXPONENTIAL_MOVINGAVERAGE_PERIODS
                .stream()
                .collect(Collectors.toMap(i -> i, i -> BigDecimal.ZERO));
//        Initial SMA: 10-period sum / 10
//        Multiplier: (2 / (Time periods + 1) ) = (2 / (10 + 1) ) = 0.1818 (18.18%)
//        EMA: {Close - EMA(previous day)} x multiplier + EMA(previous day).
        if (prev == null)
            return exponentialMovingAverages;


        for (Integer key : EXPONENTIAL_MOVINGAVERAGE_PERIODS) {
            BigDecimal prevEMA = prev.getIndicator().getAverageIndicator().getExponentialMovingAverages().get(key);
            if (prevEMA.equals(BigDecimal.ZERO))
                prevEMA = prev.getIndicator().getAverageIndicator().getMovingAverages().get(key);
            BigDecimal timeplusone = BigDecimal.valueOf(key, Indicator.BD_SCALE)
                    .add(BigDecimal.valueOf(1, Indicator.BD_SCALE));
            BigDecimal multiplier = BigDecimal.valueOf(2, Indicator.BD_SCALE)
                    .divide(timeplusone, Indicator.BD_SCALE, RoundingMode.HALF_UP);
            exponentialMovingAverages.put(key,
                    item.getClose()
                            .subtract(prevEMA)
                            .multiply(multiplier)
                            .add(prevEMA));
        }
        return exponentialMovingAverages;
    }

    public AverageIndicator calculateDummy() {
        return AverageIndicator.builder()
                .exponentialMovingAverages(EXPONENTIAL_MOVINGAVERAGE_PERIODS
                .stream()
                .collect(Collectors.toMap(i -> i, i -> item.getClose()))
        )
                .movingAverages(
                        MOVINGAVERAGE_PERIODS.stream().
                                collect(
                                        Collectors.toMap(k -> k, v -> BigDecimal.ZERO)
                                )
                )
                .movingAverageConvergenceDivergence(
                        BigDecimal.ZERO
                )
                .build();
    }
}
