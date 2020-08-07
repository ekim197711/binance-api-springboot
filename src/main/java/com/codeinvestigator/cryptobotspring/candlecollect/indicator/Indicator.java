package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
@NoArgsConstructor
public class Indicator {
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


    public static Indicator calculate(List<CandleItem> history, CandleItem item) {
        Indicator ni = new Indicator();
        if (history.size() == 0)
            return ni;

        CandleItem itemPrev = null;
        itemPrev = history.get(history.size() - 1);


        ni.movingAverages = new AverageComputation().calculateMovingAverages(ni.movingAverages,history);
        ni.exponentialMovingAverages = new AverageComputation().calculateExponentialMovingAverages(ni.exponentialMovingAverages,
                history, item, itemPrev);
        ni.movingAverageConvergenceDivergence = new AverageComputation().calculateMovingAverageConvergenceDivergence(ni.exponentialMovingAverages);
        ni.rs = new RelativeStrengthComputation().calculateRelativeStrength(history);
        ni.rsi = new RelativeStrengthComputation().calculateRelativeStrengthIndex(history, ni.rs);
        ni.trueRange = new TrueRangeComputation().calculateTrueRange(item, itemPrev);
        ni.averageTrueRange = new TrueRangeComputation().calculateAverageTrueRange(item, itemPrev, history);
//        log.info("Calculated indicators: {}", ni);
        return ni;
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



    public void nicePrintMacdATR() {
        DecimalFormat df = new DecimalFormat("###,##0.000");

        log.info("Macd: {}", df.format(getMovingAverageConvergenceDivergence().doubleValue()));
        log.info("AverageTrueRange: {}", df.format(getAverageTrueRange().doubleValue()));
        log.info("RSI: {}", df.format(getRsi().doubleValue()));
    }
}
