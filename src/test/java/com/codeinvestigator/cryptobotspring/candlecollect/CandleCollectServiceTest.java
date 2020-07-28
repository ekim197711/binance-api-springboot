package com.codeinvestigator.cryptobotspring.candlecollect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class CandleCollectServiceTest {

    @Autowired
    CandleCollectService candleCollectService;

    @Test
    public void extractCandles() {
        LocalDateTime start = LocalDateTime.of(2020,6,1,0,0);
        LocalDateTime end = start.plusDays(1).minusNanos(1);
        List<CandleItem> candleItems = candleCollectService.extractCandles(start, end,
                Symbol.LTCUSDT,
                Interval.FIFTEEN_MIN);
        Assertions.assertTrue(candleItems.size() > 0);
//        System.out.println(candleResponseItems);
        candleItems.forEach(e ->{
            System.out.println(e.simpleToString());
        });
    }
}