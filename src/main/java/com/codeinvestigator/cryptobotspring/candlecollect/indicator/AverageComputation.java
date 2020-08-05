package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class AverageComputation {
    public Map<Integer, BigDecimal> calculateMovingAverages(Map<Integer, BigDecimal> movingAverages, List<CandleItem> history) {
        for (int i = 0; i < 200 && i < history.size(); i++) {
            CandleItem candleItem = history.get(history.size() - i - 1);
            for (Integer key : movingAverages.keySet()) {
                if (i < key) {
                    movingAverages.put(key, movingAverages.get(key).add(candleItem.getClose()));
                }
            }
        }

        for (Integer key : movingAverages.keySet()) {
            BigDecimal divide = movingAverages.get(key)
                    .divide(BigDecimal.valueOf(key), Indicator.BD_SCALE, RoundingMode.HALF_UP);
            movingAverages.put(key, divide);
        }
        return movingAverages;
    }

    public BigDecimal calculateMovingAverageConvergenceDivergence(Map<Integer, BigDecimal> exponentialMovingAverages) {
        return exponentialMovingAverages.get(12).subtract(exponentialMovingAverages.get(26));
    }

    public Map<Integer, BigDecimal> calculateExponentialMovingAverages(Map<Integer, BigDecimal> exponentialMovingAverages,
                                                                       List<CandleItem> history, CandleItem item, CandleItem prev) {
//        Initial SMA: 10-period sum / 10
//        Multiplier: (2 / (Time periods + 1) ) = (2 / (10 + 1) ) = 0.1818 (18.18%)
//        EMA: {Close - EMA(previous day)} x multiplier + EMA(previous day).
        if (prev == null)
            return exponentialMovingAverages;


        for (Integer key : exponentialMovingAverages.keySet()) {
            BigDecimal prevEMA = prev.getIndicator().getExponentialMovingAverages().get(key);
            if (prevEMA.equals(BigDecimal.ZERO))
                prevEMA = prev.getIndicator().getMovingAverages().get(key);
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

}
