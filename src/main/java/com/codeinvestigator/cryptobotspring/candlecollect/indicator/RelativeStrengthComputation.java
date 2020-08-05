package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class RelativeStrengthComputation {

    public BigDecimal calculateRelativeStrength(List<CandleItem> history) {
        BigDecimal gains14 = new BigDecimal("0");
        BigDecimal loose14 = new BigDecimal("0");
        BigDecimal rs;
        for (int i = 0; i < 14 && i < history.size() - 1; i++) {
            CandleItem candleItem = history.get(history.size() - i - 1);
            CandleItem candleItemMinusOne = history.get(history.size() - i - 2);
//            BigDecimal diff = candleItem.getClose().subtract(candleItemMinusOne.getClose());
            BigDecimal diff = candleItem.difference();
            if (diff.doubleValue() > 0)
                gains14 = gains14.add(diff.abs());
            else
                loose14 = loose14.add(diff.abs());
        }
        gains14 = gains14.divide(BigDecimal.valueOf(14d), Indicator.BD_SCALE, RoundingMode.HALF_UP);
        loose14 = loose14.divide(BigDecimal.valueOf(14d), Indicator.BD_SCALE, RoundingMode.HALF_UP);

        if (loose14.doubleValue() == 0d) {
            rs = BigDecimal.ZERO;

        } else {
             rs = gains14.divide(loose14, Indicator.BD_SCALE, RoundingMode.HALF_UP);
        }
        return rs;
    }

    public BigDecimal calculateRelativeStrengthIndex(List<CandleItem> history, BigDecimal rs) {
        BigDecimal rsi;
        //        100 – 100 / ( 1 + RS )
        double rsid = 100d - (100d / (1d + rs.doubleValue()));
//            log.info("rsid value: {}", rsid);
        if (rsid < 0.0000001d) {
            rsi = BigDecimal.ZERO;
        } else
            rsi = BigDecimal.valueOf(rsid);
        return rsi;
    }
}
