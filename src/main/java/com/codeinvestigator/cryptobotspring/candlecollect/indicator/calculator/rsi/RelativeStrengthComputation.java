package com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.rsi;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.Constants;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.RSIIndicator;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeinvestigator.cryptobotspring.candlecollect.indicator.Indicator.BD_SCALE;

@RequiredArgsConstructor
public class RelativeStrengthComputation {
    private final List<CandleItem> history;
    private final CandleItem current;

    Map<Integer, BigDecimal> looses = new HashMap<>();
    Map<Integer, BigDecimal> gains = new HashMap<>();
    //    private BigDecimal rsi = BigDecimal.valueOf(-1, BD_SCALE);
//    private BigDecimal rs = BigDecimal.valueOf(-1, BD_SCALE);
    private static final int MAX_PERIOD = 14;

    //    {
//        looses.put(13, BigDecimal.ZERO);
//        looses.put(14, BigDecimal.ZERO);
//        gains.put(13, BigDecimal.ZERO);
//        gains.put(14, BigDecimal.ZERO);
//    }
    private boolean beforeInitial() {
        return history.size() < 14;
    }

    private boolean isInitial() {
        return (history.size() == 14);
    }

    public BigDecimal averageGains(int period) {
        return gains.get(period).divide(new BigDecimal(period));
    }

    public BigDecimal averageLooses(int period) {
        return looses.get(period).divide(new BigDecimal(period));
    }


    public RSIIndicator calculate() {
        if (beforeInitial())
            return calculateBefore();
        else if (isInitial())
            return calculateInitial();
        else
            return calculateRealRSI();
    }

    private RSIIndicator calculateBefore() {
        return new RSIIndicator(BigDecimal.ZERO, RSIState.BEFORE_INITIAL);
    }

    private RSIIndicator calculateRealRSI() {
        BigDecimal gain = current.gain();
        BigDecimal loose = current.loose();
        for (int i = 0; i < 13; i++) {
            CandleItem ci = history.get(history.size() - 1 - i);
            gain = gain.add(ci.gain());
            loose = loose.add(ci.loose());
        }
        BigDecimal gainDivLoose = gain.divide(loose, Constants.BD_SCALE, Constants.ROUNDING_MODE);
        BigDecimal rsi =
                BigDecimal.valueOf(100).subtract(
                        BigDecimal.valueOf(100).divide(
                                BigDecimal.valueOf(1).add(
                                        gainDivLoose
                                ), Constants.BD_SCALE, Constants.ROUNDING_MODE));
        return new RSIIndicator(rsi, RSIState.AFTER_INITIAL);
    }

    private RSIIndicator calculateInitial() {
        BigDecimal gain = BigDecimal.ZERO;
        BigDecimal loose = BigDecimal.ZERO;
        for (int i = 0; i < 14; i++) {
            CandleItem ci = history.get(history.size() - 1 - i);
            gain = gain.add(ci.gain().divide(ci.getOpen(), Constants.BD_SCALE, Constants.ROUNDING_MODE));
            loose = loose.add(ci.loose().divide(ci.getOpen(), Constants.BD_SCALE, Constants.ROUNDING_MODE));
        }
        BigDecimal rsi =
                BigDecimal.valueOf(100).subtract(
                        BigDecimal.valueOf(100).divide(
                                BigDecimal.valueOf(1).add(
                                        gain.divide(loose, Constants.BD_SCALE, Constants.ROUNDING_MODE)
                                ), Constants.BD_SCALE, Constants.ROUNDING_MODE));
        return new RSIIndicator(rsi, RSIState.INITIAL);
    }


}
