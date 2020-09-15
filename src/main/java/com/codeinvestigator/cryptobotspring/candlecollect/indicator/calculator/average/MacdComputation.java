package com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.average;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class MacdComputation {
    private final List<CandleItem> history;
    private final AverageIndicator currentAverageIndicator;
    private BigDecimal macd12;
    private BigDecimal avgmacd;


    public MacdIndicator calculate(){
        return MacdIndicator.builder()
                .macd(BigDecimal.ZERO)
                .simpleMovingAverageMacd(BigDecimal.ZERO)
                .exponentialMovingAverageMacd(BigDecimal.ZERO)
                .build();
    }

    public MacdIndicator calculateDummy(){
        return MacdIndicator.builder()
                .macd(BigDecimal.ZERO)
                .simpleMovingAverageMacd(BigDecimal.ZERO)
                .exponentialMovingAverageMacd(BigDecimal.ZERO)
                .build();
    }
    private BigDecimal calculateMovingAverageConvergenceDivergence() {
        this.macd12 = currentAverageIndicator.getExponentialMovingAverages().get(12).subtract(
                currentAverageIndicator.getExponentialMovingAverages().get(26)
        );
        return macd12;
    }

    private BigDecimal simpleMovingAverageMacd(){
        int period = Math.min(9, history.size());
        BigDecimal avgmacd = macd12.add(BigDecimal.ZERO);
        for (int i = 0;i < period-1; i++){
            CandleItem ci = history.get(history.size() - 1 - i);
            avgmacd = avgmacd.add(ci.getIndicator().getMacdIndicator().getMacd());
        }
        this.avgmacd = avgmacd.divide(BigDecimal.valueOf(period), Constants.MATHCONTEXT);
        return avgmacd;
    }

    private BigDecimal calculateMacdEma9() {
        MacdIndicator macdIndicatorPrev = history.get(history.size() - 1).getIndicator().getMacdIndicator();
        int key = 9;
        BigDecimal timeplusone = BigDecimal.valueOf(key).add(BigDecimal.ONE);
        BigDecimal multiplier = BigDecimal.valueOf(2)
                .divide(timeplusone, Constants.MATHCONTEXT);
        BigDecimal macdXmultiplier = multiplier.multiply(macd12, Constants.MATHCONTEXT);
        BigDecimal prevXmultiplier = BigDecimal.ONE.subtract(multiplier).multiply(
                macdIndicatorPrev.getExponentialMovingAverageMacd(),
                Constants.MATHCONTEXT);
        BigDecimal ema = macdXmultiplier.add(prevXmultiplier);
        return ema;
    }

}
