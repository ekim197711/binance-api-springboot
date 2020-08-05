package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.CandleItemRepository;
import com.codeinvestigator.cryptobotspring.candlecollect.Interval;
import com.codeinvestigator.cryptobotspring.candlecollect.Symbol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@SpringBootTest
public class IndicatorForExistingCandlesServiceTest {
    @Autowired
    IndicatorForExistingCandlesService indicatorForExistingCandlesService;

    @Autowired
    CandleItemRepository repository;

    @Test
    public void calcSome() {
        indicatorForExistingCandlesService.calcIndicatorsFromDB(
                Symbol.LTCUSDT,
                Interval.ONE_HOUR
        );
    }

    @Test
    public void tryMovingAverage(){
        List<CandleItem> withinTime = repository.findWithinTime(Symbol.LTCUSDT, Interval.ONE_HOUR,
                LocalDateTime.of(2020, 6, 2, 0, 0)
                        .toInstant(ZoneOffset.UTC).toEpochMilli(),
                LocalDateTime.of(2020, 7, 30, 0, 0)
                        .toInstant(ZoneOffset.UTC).toEpochMilli()
        );
        System.out.println("Calc items now: " + withinTime.size());
        List<CandleItem> candleItems = indicatorForExistingCandlesService.calcIndicators(withinTime);
        CandleItem candleItem = candleItems.get(candleItems.size() - 20);
        List<CandleItem> history = candleItems.subList(0, candleItems.size() - 20);
        candleItem.getIndicator().calculate(history, candleItem);

        String s = candleItem.simpleToString();
        System.out.println("CANDLEITEM CHECK " + s);
        candleItem.getIndicator().nicePrintMovingAverages();
        candleItem.getIndicator().nicePrintExpAverages();
        candleItem.getIndicator().nicePrintMacdATR();

    }
}