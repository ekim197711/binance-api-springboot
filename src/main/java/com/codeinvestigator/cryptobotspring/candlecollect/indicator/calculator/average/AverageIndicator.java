package com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.average;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Builder
public class AverageIndicator {

    private BigDecimal movingAverageConvergenceDivergence;
    private BigDecimal macdema9;
    private Map<Integer, BigDecimal> movingAverages;
    private Map<Integer, BigDecimal> exponentialMovingAverages;
}
