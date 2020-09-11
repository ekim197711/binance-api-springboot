package com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.rsi;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.Constants;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.RSIIndicator;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class RelativeStrengthComputation {
    private final List<CandleItem> history;
    private final CandleItem current;
    BigDecimal avgGain = BigDecimal.ZERO;
    BigDecimal avgLoose = BigDecimal.ZERO;

    public RelativeStrengthComputation(CandleItem item) {
        this.current = item;
        this.history = null;
    }

    private CandleItem previousCandle() {
        return history.get(history.size() - 1);
    }

    private boolean beforeInitial() {
        return history.size() < 14;
    }

    private boolean isInitial() {
        return (history.size() == 14);
    }

    public RSIIndicator calculate() {
        if (beforeInitial())
            return calculateBefore();
        else if (isInitial()) {
            avgGainLooseInitialCalc();
            return calculateRSI();
        } else {
            avgGainLooseCalc();
            return calculateRSI();
        }
    }

    private RSIIndicator calculateBefore() {
        return new RSIIndicator(BigDecimal.ZERO, RSIState.BEFORE_INITIAL, avgGain, avgLoose);
    }

    private void avgGainLooseInitialCalc() {
        for (int i = 0; i < 14; i++) {
            CandleItem ci = history.get(history.size() - 1 - i);
            avgGain = avgGain.add(ci.gain());
            avgLoose = avgLoose.add(ci.loose());
        }
        avgGain = avgGain.divide(BigDecimal.valueOf(14), Constants.MATHCONTEXT);
        avgLoose = avgLoose.divide(BigDecimal.valueOf(14), Constants.MATHCONTEXT);
    }

    private void avgGainLooseCalc() {

        avgGain = previousCandle().getIndicator().getRsiIndicator()
                .getAvgGain()
                .multiply(BigDecimal.valueOf(13))
                .add(current.gain())
                .divide(BigDecimal.valueOf(14), Constants.MATHCONTEXT)
        ;
        avgLoose = previousCandle().getIndicator().getRsiIndicator()
                .getAvgLoose()
                .multiply(BigDecimal.valueOf(13))
                .add(current.loose())
                .divide(BigDecimal.valueOf(14), Constants.MATHCONTEXT)
        ;
    }

    private RSIIndicator calculateRSI() {
        BigDecimal gDivl = avgGain.divide(avgLoose, Constants.MATHCONTEXT);
        BigDecimal rsi =
                BigDecimal.valueOf(100).subtract(
                        BigDecimal.valueOf(100).divide(
                                BigDecimal.valueOf(1).add(
                                        gDivl
                                ), Constants.BD_SCALE, Constants.ROUNDING_MODE));
        if (isInitial())
            return new RSIIndicator(rsi, RSIState.INITIAL, avgGain, avgLoose);
        else
            return new RSIIndicator(rsi, RSIState.AFTER_INITIAL, avgGain, avgLoose);
    }

    public RSIIndicator calculateDummy() {
        return new RSIIndicator(BigDecimal.valueOf(50), RSIState.BEFORE_INITIAL, BigDecimal.ZERO,
                BigDecimal.ZERO);
    }
}
