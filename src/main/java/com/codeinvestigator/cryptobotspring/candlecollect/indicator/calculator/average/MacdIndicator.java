package com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.average;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MacdIndicator {
//    Moving average convergence divergence
    private BigDecimal macd;
    private BigDecimal simpleMovingAverageMacd;
//    Signal line
    private BigDecimal exponentialMovingAverageMacd;
    private BigDecimal macdEma9;
//    histogram
    private BigDecimal histogram;

}
