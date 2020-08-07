package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class RelativeStrengthComputation {
    private Double gainsAverage13 = 0d;
    private Double looseAverage13 = 0d;


    public BigDecimal calculateRelativeStrength(List<CandleItem> history) {
        BigDecimal gains14 = new BigDecimal("0");
        BigDecimal loose14 = new BigDecimal("0");
        BigDecimal rs;
        for (int i = 0; i < 14 && i < history.size(); i++) {
            CandleItem candleItem = history.get(history.size() - i - 1);

            BigDecimal diff = candleItem.difference();
            if (diff.doubleValue() > 0)
                gains14 = gains14.add(diff.abs());
            else
                loose14 = loose14.add(diff.abs());
        }

        if (loose14.doubleValue() == 0d) {
            rs = BigDecimal.ZERO;

        } else {
            rs = gains14.divide(loose14, Indicator.BD_SCALE, RoundingMode.HALF_UP);
        }
        return rs;
    }

    private void calcLooseAndGain13(List<CandleItem> history) {
        for (int i = 0; i < 14; i++) {
            CandleItem candleItem = history.get(history.size() - 1 - i);
            double diff = candleItem.differencePercentage().doubleValue();
            if (diff < 0) {
                looseAverage13 += Math.abs(diff);
            } else {
                gainsAverage13 += Math.abs(diff);
            }
        }
        looseAverage13 = looseAverage13 / 13;
        gainsAverage13 = gainsAverage13 / 13;
    }

    public BigDecimal calculateRelativeStrengthIndex(List<CandleItem> history, BigDecimal rs) {
        BigDecimal rsi;
        //        100 – 100 / ( 1 + RS )
        if (history.size() < 14) {
            Double less14rsi = 100 - (100 / (1 + rs.doubleValue()));
            return new BigDecimal(less14rsi);
        } else {
            calcLooseAndGain13(history);
            CandleItem last = history.get(history.size() - 1);
            double lastGain = 0d;
            double lastLoose = 0d;
            if (last.differencePercentage().doubleValue() < 0) {
                lastLoose = last.difference().doubleValue();
            } else lastGain = last.differencePercentage().doubleValue();
            double midcalc = (gainsAverage13 * 13 + lastGain) / (looseAverage13 * 13 + lastLoose);
            double rsid = 100d - (100d / (1d + midcalc));
//            log.info("rsid value: {}", rsid);
            if (rsid < 0.0000001d) {
                rsi = BigDecimal.ZERO;
            } else
                rsi = BigDecimal.valueOf(rsid);
            return rsi;
        }
    }
}
