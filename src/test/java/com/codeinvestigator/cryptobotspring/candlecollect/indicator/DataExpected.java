package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;



@Getter
@RequiredArgsConstructor
public enum DataExpected {
    ZERO(LocalDateTime.of(2020, 8, 29, 10, 0), 64.2,57.54,57.61,0d,
            0.19, 0.12, 0.07 ),
    ONE(LocalDateTime.of(2020, 8, 30, 14, 0),61.92,58.13,58.14,0d,
            0.20,0.19,0.01),
    TWO(LocalDateTime.of(2020, 8, 31, 6, 0),61.83,62.43,62.37,0d,
            1.11,1.11,0d),
    THREE(LocalDateTime.of(2020, 9, 1, 8, 0),70.9,61.39,61.66,0d,
            0.12,0.04,0.08),
    FOURTH(LocalDateTime.of(2020, 9, 2, 18, 0),16.84,59.02,58.86,0d,
            -1.19, -0.62, -0.57)
;

    private final LocalDateTime localDateTime;
    private final Double rsi;
    private final Double ma7;
    private final Double ema7;
    private final Double tr;
    private final Double macd12;
    private final Double macdema9;
    private final Double macdhistoric;




    public String averageString() {
        return String.format("ma: %s, ema: %s, macd12: %s, macdema9", ma7, ema7, macd12, macdema9);
    }
}
