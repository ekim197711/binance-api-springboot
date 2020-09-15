package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.CandleItemRepository;
import com.codeinvestigator.cryptobotspring.candlecollect.Interval;
import com.codeinvestigator.cryptobotspring.candlecollect.Symbol;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.calculator.expected.DataExpected;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
public class AverageTest {
    @Autowired
    IndicatorForExistingCandlesService indicatorForExistingCandlesService;

    @Autowired
    CandleItemRepository repository;

//    @ClassRule
//    public static DockerComposeContainer environment =
//            new DockerComposeContainer<>(new File("src/test/resources/compose-test.yml"))
//                    .withExposedService("mymongo", 27017,
//                            Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));
    @Test
    public void averageTest(){
//        environment.start();
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

        for (DataExpected de : DataExpected.values()){
            LocalDateTime dt = de.getLocalDateTime();
            CandleItem ciFirst = candleItems.stream().filter(ci ->
                    ci.openDateTime().until(dt.minusHours(2), ChronoUnit.MINUTES) < 10).findFirst().get();
            System.out.println(String.format("Expected: %s, real: %s", de.averageString(),
                    ciFirst.getIndicator().getAverageIndicator()));
        }
    }
}