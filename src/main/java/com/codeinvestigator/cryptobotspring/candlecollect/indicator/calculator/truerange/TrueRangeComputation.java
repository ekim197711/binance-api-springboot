package com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.truerange;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.Indicator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TrueRangeComputation {

    private final CandleItem item;
    private final CandleItem itemPrev;
    private BigDecimal currentTrueRange;
    private final List<CandleItem> history;
    private BigDecimal calculateAverageTrueRange() {

        if (history.size() < 14)
            return BigDecimal.ZERO;
        else if (history.size() == 14) {
            BigDecimal trSum = BigDecimal.ZERO;
            for (int i = 0; i < 14; i++) {
                CandleItem ci = history.get(history.size() - i -1);
                trSum = trSum.add(ci.getIndicator().getTrueRangeIndicator().getTrueRange());
            }
            BigDecimal atr=  trSum.divide(BigDecimal.valueOf(14), Indicator.BD_SCALE, RoundingMode.HALF_UP);
            log.info("First ATR has been calculated! {}", atr);
            return atr;
        } else {
//            Current ATR = [(Prior ATR x 13) + Current TR] / 14
            BigDecimal atr= itemPrev.getIndicator().getTrueRangeIndicator()
                    .getAverageTrueRange()
                    .multiply(new BigDecimal("13"))
                    .add(currentTrueRange)
                    .divide(new BigDecimal("14"), Indicator.BD_SCALE, RoundingMode.HALF_UP);
//            log.info("ATR! {}", atr);
            return atr;
        }
    }

    public TrueRangeIndicator calculate(){
        return TrueRangeIndicator.builder()
                .trueRange(calculateTrueRange())
                .averageTrueRange(calculateAverageTrueRange())
                .build();

    }

    private BigDecimal calculateTrueRange() {
        BigDecimal lowMinusPreviousClose = BigDecimal.ZERO;
        BigDecimal highMinusPreviousClose = BigDecimal.ZERO;
        if (itemPrev != null) {
            highMinusPreviousClose = item.getHigh().subtract(itemPrev.getClose()).abs();
            lowMinusPreviousClose = item.getLow().subtract(itemPrev.getClose()).abs();
        }
        currentTrueRange = item
                .difference()
                .abs()
                .max(highMinusPreviousClose)
                .max(lowMinusPreviousClose);
        return currentTrueRange;
    }
}
