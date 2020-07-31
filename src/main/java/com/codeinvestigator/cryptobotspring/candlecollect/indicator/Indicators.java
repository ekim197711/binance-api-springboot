package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
@NoArgsConstructor
public class Indicators {
    public static final int BD_SCALE = 6;
    private BigDecimal rsi = BigDecimal.valueOf(-1, BD_SCALE);
    private BigDecimal rs = BigDecimal.valueOf(-1, BD_SCALE);
    private Map<Integer, BigDecimal> movingAverages = new HashMap<>(Map.of(
            7, BigDecimal.valueOf(0),
            12, BigDecimal.valueOf(0),
            20, BigDecimal.valueOf(0),
            26, BigDecimal.valueOf(0),
            50, BigDecimal.valueOf(0),
            99, BigDecimal.valueOf(0),
            200, BigDecimal.valueOf(0))
    );
    private Map<Integer, BigDecimal> exponentialMovingAverages = new HashMap<>(Map.of(
            12, BigDecimal.valueOf(0),
            26, BigDecimal.valueOf(0)
    ));

    private BigDecimal trueRange = BigDecimal.valueOf(-1, BD_SCALE);
    private BigDecimal averageTrueRange = BigDecimal.valueOf(-1, BD_SCALE);
    private BigDecimal movingAverageConvergenceDivergence = BigDecimal.valueOf(-1, BD_SCALE);


    public Indicators calculate(List<CandleItem> history, CandleItem item) {
        if (history.size() == 0)
            return this;

        CandleItem itemPrev = null;
//        if (history.size() > 0) {
        itemPrev = history.get(history.size() - 1);
        if (itemPrev.getIndicators() == null)
            itemPrev.setIndicators(new Indicators());
        if (item.getIndicators() == null)
            item.setIndicators(new Indicators());
//        }
//        if (history.size() >= 200) {
        calculateMovingAverages(history);
        calculateExponentialMovingAverages(history, item, itemPrev);
        calculateMovingAverageConvergenceDivergence();
//        }
//        List<CandleItem> historyInclThis = new ArrayList<>(history);
//        historyInclThis.add(item);
        calculateRelativeStrengthIndex(history);
        calculateTrueRange(item, itemPrev);
        calculateAverageTrueRange(item, itemPrev, history);
        log.info("Calculated indicators: {}", this);
        return this;
    }

    private void calculateExponentialMovingAverages(List<CandleItem> history, CandleItem item, CandleItem prev) {
//        Initial SMA: 10-period sum / 10
//        Multiplier: (2 / (Time periods + 1) ) = (2 / (10 + 1) ) = 0.1818 (18.18%)
//        EMA: {Close - EMA(previous day)} x multiplier + EMA(previous day).
        if (prev == null)
            return;


        for (Integer key : exponentialMovingAverages.keySet()) {
            BigDecimal prevEMA = prev.getIndicators().getExponentialMovingAverages().get(key);
            if (prevEMA.equals(BigDecimal.ZERO))
                prevEMA = prev.getIndicators().getMovingAverages().get(key);
            BigDecimal timeplusone = BigDecimal.valueOf(key, BD_SCALE)
                    .add(BigDecimal.valueOf(1, BD_SCALE));
            BigDecimal multiplier = BigDecimal.valueOf(2, BD_SCALE)
                    .divide(timeplusone, BD_SCALE, RoundingMode.HALF_UP);
            exponentialMovingAverages.put(key,
                    item.getClose()
                            .subtract(prevEMA)
                            .multiply(multiplier)
                            .add(prevEMA));
        }
    }

    private void calculateMovingAverages(List<CandleItem> history) {
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
                    .divide(BigDecimal.valueOf(key), BD_SCALE, RoundingMode.HALF_UP);
            movingAverages.put(key, divide);
        }

    }

    private void calculateMovingAverageConvergenceDivergence() {
        movingAverageConvergenceDivergence = exponentialMovingAverages.get(12).subtract(exponentialMovingAverages.get(26));
    }

    private void calculateRelativeStrengthIndex(List<CandleItem> history) {
        BigDecimal gains14 = new BigDecimal("0");
        BigDecimal loose14 = new BigDecimal("0");

        for (int i = 0; i < 14 && i < history.size()-1; i++) {
            CandleItem candleItem = history.get(history.size() - i - 1);
            CandleItem candleItemMinusOne = history.get(history.size() - i - 2);
//            BigDecimal diff = candleItem.getClose().subtract(candleItemMinusOne.getClose());
            BigDecimal diff = candleItem.difference();
            if (diff.doubleValue() > 0)
                gains14 = gains14.add(diff.abs());
            else
                loose14 = loose14.add(diff.abs());
        }
        gains14 = gains14.divide(BigDecimal.valueOf(14d), BD_SCALE, RoundingMode.HALF_UP);
        loose14 = loose14.divide(BigDecimal.valueOf(14d), BD_SCALE, RoundingMode.HALF_UP);

        if (loose14.doubleValue() == 0d) {
            this.rs = BigDecimal.ZERO;
            this.rsi = BigDecimal.ZERO;
        } else {
            BigDecimal rs = gains14.divide(loose14, BD_SCALE, RoundingMode.HALF_UP);
            //        100 – 100 / ( 1 + RS )
            double rsid = 100d - (100d / (1d + rs.doubleValue()));
//            log.info("rsid value: {}", rsid);
            if (rsid < 0.0000001d) {
                this.rsi = BigDecimal.ZERO;
            } else
                this.rsi = BigDecimal.valueOf(rsid);
            this.rs = rs;
        }
    }

    private void calculateAverageTrueRange(CandleItem item, CandleItem itemPrev, List<CandleItem> history) {
       if (history.size() < 14)
            return;
        else if (history.size() == 14) {
            BigDecimal trSum = BigDecimal.ZERO;
            for (int i = 0; i < 14; i++) {
                CandleItem ci = history.get(history.size() - i -1);
                trSum = trSum.add(ci.getIndicators().getTrueRange());
            }
            this.averageTrueRange = trSum.divide(BigDecimal.valueOf(14), BD_SCALE, RoundingMode.HALF_UP);
            log.info("First ATR has been calculated! {}", this.averageTrueRange);
        } else {
//            Current ATR = [(Prior ATR x 13) + Current TR] / 14
            this.averageTrueRange = itemPrev.getIndicators()
                    .getAverageTrueRange()
                    .multiply(new BigDecimal("13"))
                    .add(this.getTrueRange())
                    .divide(new BigDecimal("14"), BD_SCALE, RoundingMode.HALF_UP);
            log.info("ATR! {}", this.averageTrueRange);
        }
    }

    public void nicePrintMovingAverages() {
        log.info("MA Printout BEGIN ####");
        DecimalFormat df = new DecimalFormat("###,##0.000");
        for (Integer integer : getMovingAverages().keySet()) {
            log.info("{}: {}", integer, df.format(getMovingAverages().get(integer).doubleValue()));
        }
        log.info("MA Printout END ####");
    }

    public void nicePrintExpAverages() {
        log.info("Exp MA Printout BEGIN ####");
        DecimalFormat df = new DecimalFormat("###,##0.000");
        for (Integer integer : getExponentialMovingAverages().keySet()) {
            log.info("{}: {}", integer, df.format(getMovingAverages().get(integer).doubleValue()));
        }
        log.info("Exp MA Printout END ####");
    }

    private void calculateTrueRange(CandleItem item, CandleItem itemPrev) {
        BigDecimal lowMinusPreviousClose = BigDecimal.ZERO;
        BigDecimal highMinusPreviousClose = BigDecimal.ZERO;
        if (itemPrev != null) {
            highMinusPreviousClose = item.getHigh().subtract(itemPrev.getClose()).abs();
            lowMinusPreviousClose = item.getLow().subtract(itemPrev.getClose()).abs();
        }
        this.trueRange = item
                .difference()
                .abs()
                .max(highMinusPreviousClose)
                .max(lowMinusPreviousClose);
    }

    public void nicePrintMacdATR() {
        DecimalFormat df = new DecimalFormat("###,##0.000");

        log.info("Macd: {}", df.format(getMovingAverageConvergenceDivergence().doubleValue()));
        log.info("AverageTrueRange: {}", df.format(getAverageTrueRange().doubleValue()));
        log.info("RSI: {}", df.format(getRsi().doubleValue()));
    }
}
