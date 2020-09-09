package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import java.math.MathContext;
import java.math.RoundingMode;

public class Constants {
    public static final int BD_SCALE = 6;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    public static final MathContext MATHCONTEXT = new MathContext(BD_SCALE, ROUNDING_MODE);
}
