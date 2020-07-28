package com.codeinvestigator.cryptobotspring.candlecollect.simulator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Data
@Slf4j
@Builder
public class Indicators {
    private BigDecimal rsi;
    private BigDecimal rs;
    private BigDecimal movingAverage20;

    public static Indicators of(List<CandleItem> history) {
        if (history.size() < 20)
            return null;
        BigDecimal gains14 = new BigDecimal("0");
        BigDecimal loose14 = new BigDecimal("0");
        BigDecimal avg = new BigDecimal("0");
        for (int i = 0; i < 20; i++) {
            CandleItem candleItem = history.get(history.size() - i - 1);
            if (i < 14) {
                gains14 = gains14.add(candleItem.gain());
                loose14 = loose14.add(candleItem.loose());
                log.info("Calc gains and looses");
            }
            avg = avg.add(candleItem.getClose());
        }
        avg = avg.divide(BigDecimal.valueOf(20),10, RoundingMode.HALF_UP);

        BigDecimal rs = gains14.divide(loose14, 20, RoundingMode.HALF_UP);
//        100 – 100 / ( 1 + RS )
        BigDecimal rsi = BigDecimal.valueOf(100 - 100 / (1 + rs.doubleValue()));

        Indicators newIndicators =  Indicators.builder()
                .movingAverage20(avg)
                .rs(rs)
                .rsi(rsi)
                .build();
        log.info("Created new indicators: {}", newIndicators);
        return newIndicators;
    }
}
