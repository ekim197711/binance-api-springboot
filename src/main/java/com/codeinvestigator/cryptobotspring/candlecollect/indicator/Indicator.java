package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.average.AverageComputation;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.average.AverageIndicator;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.average.MacdIndicator;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.rsi.RelativeStrengthComputation;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.truerange.TrueRangeComputation;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.truerange.TrueRangeIndicator;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.List;

@Data
@Slf4j
@Builder
public class Indicator {

    public static final int BD_SCALE = 6;
    private RSIIndicator rsiIndicator;
    private AverageIndicator averageIndicator;
    private MacdIndicator macdIndicator;
    private TrueRangeIndicator trueRangeIndicator;


    public static Indicator calculateDummy(CandleItem item) {
        log.info("Calculate indicators for initial candleitem / dummy value {}", item);
       return Indicator.builder()
                .averageIndicator(new AverageComputation(item).calculateDummy())
                .trueRangeIndicator(new TrueRangeComputation().calculateDummy())
                .rsiIndicator(new RelativeStrengthComputation(item).calculateDummy())
                .build();

    }

    public static Indicator calculate(List<CandleItem> history, CandleItem item) {
        CandleItem itemPrev = null;
        itemPrev = history.get(history.size() - 1);
        return Indicator.builder()
                .averageIndicator(new AverageComputation(history, item, history.get(history.size() - 1)).calculate())
                .trueRangeIndicator(new TrueRangeComputation(item, itemPrev, history).calculate())
                .rsiIndicator(new RelativeStrengthComputation(history, item).calculate())
                .build();
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
