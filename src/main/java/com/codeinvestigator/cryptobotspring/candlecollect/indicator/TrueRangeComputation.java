package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
public class TrueRangeComputation {
    public BigDecimal calculateAverageTrueRange(Indicator item, Indicator itemPrev, List<CandleItem> history) {
        if (history.size() < 14)
            return BigDecimal.ZERO;
        else if (history.size() == 14) {
            BigDecimal trSum = BigDecimal.ZERO;
            for (int i = 0; i < 14; i++) {
                CandleItem ci = history.get(history.size() - i -1);
                trSum = trSum.add(ci.getIndicator().getTrueRange());
            }
            BigDecimal atr=  trSum.divide(BigDecimal.valueOf(14), Indicator.BD_SCALE, RoundingMode.HALF_UP);
            log.info("First ATR has been calculated! {}", atr);
            return atr;
        } else {
//            Current ATR = [(Prior ATR x 13) + Current TR] / 14
            BigDecimal atr= itemPrev
                    .getAverageTrueRange()
                    .multiply(new BigDecimal("13"))
                    .add(item.getTrueRange())
                    .divide(new BigDecimal("14"), Indicator.BD_SCALE, RoundingMode.HALF_UP);
//            log.info("ATR! {}", atr);
            return atr;
        }
    }

    public BigDecimal calculateTrueRange(CandleItem item, CandleItem itemPrev) {
        BigDecimal lowMinusPreviousClose = BigDecimal.ZERO;
        BigDecimal highMinusPreviousClose = BigDecimal.ZERO;
        if (itemPrev != null) {
            highMinusPreviousClose = item.getHigh().subtract(itemPrev.getClose()).abs();
            lowMinusPreviousClose = item.getLow().subtract(itemPrev.getClose()).abs();
        }
        return item
                .difference()
                .abs()
                .max(highMinusPreviousClose)
                .max(lowMinusPreviousClose);
    }
}
