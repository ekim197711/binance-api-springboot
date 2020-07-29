package com.codeinvestigator.cryptobotspring.candlecollect.simulator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
@Slf4j
@NoArgsConstructor
public class Indicators {
    private BigDecimal rsi = BigDecimal.valueOf(-1);
    private BigDecimal rs = BigDecimal.valueOf(-1);
    private BigDecimal movingAverage20 = BigDecimal.valueOf(-1);
    private BigDecimal movingAverage50 = BigDecimal.valueOf(-1);
    private BigDecimal movingAverage99 = BigDecimal.valueOf(-1);
    private BigDecimal movingAverage200 = BigDecimal.valueOf(-1);
    private BigDecimal trueRange = BigDecimal.valueOf(-1);
    private BigDecimal averageTrueRange = BigDecimal.valueOf(-1);

    public void calculate(List<CandleItem> history, CandleItem item) {
        CandleItem itemPrev = null;
        if (history.size() > 0) {
            itemPrev = history.get(history.size() - 1);
        }

        calculateMovingAverages(history);
        relativeStrengthIndex(history);
        trueRange(item, itemPrev);
        averageTrueRange(item, itemPrev, history);
        log.info("Calculated indicators: {}", this);
    }

    private void calculateMovingAverages(List<CandleItem> history) {
        if (history.size() >= 200) {
            BigDecimal avg = new BigDecimal("0");
            BigDecimal avg50 = new BigDecimal("0");
            BigDecimal avg99 = new BigDecimal("0");
            BigDecimal avg200 = new BigDecimal("0");
            for (int i = 0; i < 200 && i < history.size(); i++) {
                CandleItem candleItem = history.get(history.size() - i - 1);
                avg = avg.add(candleItem.getClose());
                if (i < 50)
                    avg50 = avg50.add(candleItem.getClose());
                if (i < 99)
                    avg99 = avg99.add(candleItem.getClose());
                if (i < 200)
                    avg200 = avg200.add(candleItem.getClose());
            }
            avg = avg.divide(BigDecimal.valueOf(20), 10, RoundingMode.HALF_UP);
            avg50 = avg50.divide(BigDecimal.valueOf(50), 10, RoundingMode.HALF_UP);
            avg200 = avg200.divide(BigDecimal.valueOf(200), 10, RoundingMode.HALF_UP);
            avg99 = avg99.divide(BigDecimal.valueOf(99), 10, RoundingMode.HALF_UP);
            this.movingAverage20 = avg;
            this.movingAverage50 = avg50;
            this.movingAverage99 = avg99;
            this.movingAverage200 = avg200;
        }
    }

    private void relativeStrengthIndex(List<CandleItem> history) {
        BigDecimal gains14 = new BigDecimal("0");
        BigDecimal loose14 = new BigDecimal("0");

        for (int i = 0; i < 14 && i < history.size(); i++) {
            CandleItem candleItem = history.get(history.size() - i - 1);
            gains14 = gains14.add(candleItem.gain());
            loose14 = loose14.add(candleItem.loose());
        }
        BigDecimal rs = gains14.divide(loose14, 20, RoundingMode.HALF_UP);
        //        100 – 100 / ( 1 + RS )
        BigDecimal rsi = BigDecimal.valueOf(100 - 100 / (1 + rs.doubleValue()));
        this.rs = rs;
        this.rsi = rsi;
    }

    private void averageTrueRange(CandleItem item, CandleItem itemPrev, List<CandleItem> history) {
        if (itemPrev == null)
            return;
        else if (history.size() < 14)
            return;
        else if (itemPrev.getIndicators().getTrueRange().doubleValue() < 0) {
            BigDecimal trSum = BigDecimal.ZERO;
            for (int i = 0; i < 14; i++) {
                CandleItem ci = history.get(history.size() - i - 1);
                trSum.add(ci.getIndicators().getTrueRange());
            }
            this.averageTrueRange = trSum.divide(BigDecimal.valueOf(14), 10, RoundingMode.HALF_UP);
        } else {
//            Current ATR = [(Prior ATR x 13) + Current TR] / 14
            this.averageTrueRange = itemPrev.getIndicators()
                    .getAverageTrueRange()
                    .multiply(BigDecimal.valueOf(13))
                    .add(getTrueRange())
                    .divide(BigDecimal.valueOf(14), 10, RoundingMode.HALF_UP);
        }
    }

    private void trueRange(CandleItem item, CandleItem itemPrev) {
        BigDecimal lowMinusPreviousClose = BigDecimal.ZERO;
        BigDecimal highMinusPreviousClose = BigDecimal.ZERO;
        if (itemPrev != null) {
            highMinusPreviousClose = item.getHigh().subtract(itemPrev.getClose()).abs();
            lowMinusPreviousClose = item.getLow().subtract(itemPrev.getClose()).abs();
        }
        BigDecimal trueRange = item
                .difference()
                .abs()
                .max(highMinusPreviousClose)
                .max(lowMinusPreviousClose);
        this.trueRange = trueRange;
    }

}
