package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.average.AverageComputation;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.average.AverageIndicator;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.rsi.RelativeStrengthComputation;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.truerange.TrueRangeComputation;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.truerange.TrueRangeIndicator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.List;

@Data
@Slf4j
@NoArgsConstructor
public class Indicator {

    public static final int BD_SCALE = 6;
    private RSIIndicator rsiIndicator;
    private AverageIndicator averageIndicator;
    private TrueRangeIndicator trueRangeIndicator;



    public static Indicator calculate(List<CandleItem> history, CandleItem item) {
        Indicator ni = new Indicator();
        if (history.size() == 0)
            return ni;

        CandleItem itemPrev = null;
        itemPrev = history.get(history.size() - 1);

        ni.setAverageIndicator(new AverageComputation(history, item, history.get(history.size()-1)).calculate());
        ni.setTrueRangeIndicator(new TrueRangeComputation(item, itemPrev, history).calculate());
        ni.setRsiIndicator(new RelativeStrengthComputation(history, item).calculate());
//        log.info("Calculated indicators: {}", ni);
        return ni;
    }







    public void nicePrintMovingAverages() {
        log.info("MA Printout BEGIN ####");
        DecimalFormat df = new DecimalFormat("###,##0.000");
        for (Integer integer : getAverageIndicator().getMovingAverages().keySet()) {
            log.info("{}: {}", integer, df.format(getAverageIndicator().getMovingAverages().get(integer).doubleValue()));
        }
        log.info("MA Printout END ####");
    }

    public void nicePrintExpAverages() {
        log.info("Exp MA Printout BEGIN ####");
        DecimalFormat df = new DecimalFormat("###,##0.000");
        for (Integer integer : getAverageIndicator().getExponentialMovingAverages().keySet()) {
            log.info("{}: {}", integer, df.format(getAverageIndicator().getMovingAverages().get(integer).doubleValue()));
        }
        log.info("Exp MA Printout END ####");
    }



    public void nicePrintMacdATR() {
        DecimalFormat df = new DecimalFormat("###,##0.000");

        log.info("Macd: {}", df.format(getAverageIndicator().getMovingAverageConvergenceDivergence().doubleValue()));
        log.info("AverageTrueRange: {}", df.format(getTrueRangeIndicator().getAverageTrueRange().doubleValue()));
//        log.info("RSI: {}", df.format(getRsi().doubleValue()));
    }


}
