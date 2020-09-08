package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.rsi.RSIState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeinvestigator.cryptobotspring.candlecollect.indicator.Indicator.BD_SCALE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RSIIndicator {
    private BigDecimal rsi;
    private RSIState rsiState;
}
