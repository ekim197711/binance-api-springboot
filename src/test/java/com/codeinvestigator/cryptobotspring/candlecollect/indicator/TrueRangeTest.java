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
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
public class TrueRangeTest {
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
    public void calculateIndicators(){
        Long gte = LocalDateTime.of(2020, 8, 28, 0, 0)
                .toInstant(ZoneOffset.UTC).toEpochMilli();
        Long lt = LocalDateTime.of(2020, 9, 3, 0, 0)
                .toInstant(ZoneOffset.UTC).toEpochMilli();
        System.out.println(String.format("GTE %s, LT %s", gte, lt));
        List<CandleItem> withinTime = repository.findWithinTime(Symbol.LTCUSDT, Interval.ONE_HOUR,
                gte,lt
        );
        System.out.println("Calc items now: " + withinTime.size());
        List<CandleItem> candleItems = indicatorForExistingCandlesService.calcIndicators(withinTime);

//        expected values;
        /* +2 GMT
        2020-08-29 1000         64,02
        2020-08-30 1400         61,92
        2020-08-31 0600         61,83
        2020-09-01 0800         70,9
        2020-09-02 1800         16,84

         */
        List<Double> expected = List.of(64.02,61.92d, 61.83,70.9, 16.84);
        LocalDateTime dateZero = LocalDateTime.of(2020, 8, 29, 10, 0);
        LocalDateTime dateFirst = LocalDateTime.of(2020, 8, 30, 14, 0);
        LocalDateTime dateSecond = LocalDateTime.of(2020, 8, 31, 6, 0);
        LocalDateTime dateThird = LocalDateTime.of(2020, 9, 1, 8, 0);
        LocalDateTime dateFourth = LocalDateTime.of(2020, 9, 2, 18, 0);
        int i = 0;
        for (LocalDateTime dt : List.of(dateZero, dateFirst, dateSecond, dateThird, dateFourth)){
//            for (CandleItem ci : candleItems){
//                long until = ci.openDateTime().until(dt, ChronoUnit.MINUTES);
//                System.out.println(String.format("minutes between: %s, candleOpenLong: %s, candle: %s, test: %s", until, ci.getOpenTime(), ci.openDateTime(), dt));
//            }

            CandleItem ciFirst = candleItems.stream().filter(ci ->
                    ci.openDateTime().until(dt.minusHours(2), ChronoUnit.MINUTES) < 10).findFirst().get();
            System.out.println(String.format("Expected: %s, real: %s", expected.get(i),
                    ciFirst.getIndicator().getRsiIndicator().getRsi()));
            i++;
        }
        for (CandleItem ci : candleItems){
            ci.printRSI();
        }

//        candleItems

    }
}