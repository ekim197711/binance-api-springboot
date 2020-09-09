package com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.truerange;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TrueRangeIndicator {
    private BigDecimal trueRange;
    private BigDecimal averageTrueRange;
}
