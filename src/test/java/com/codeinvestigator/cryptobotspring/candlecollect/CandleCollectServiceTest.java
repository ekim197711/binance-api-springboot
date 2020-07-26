package com.codeinvestigator.cryptobotspring.candlecollect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CandleCollectServiceTest {

    @Autowired
    CandleCollectService candleCollectService;

    @Test
    public void extractCandles() {
        LocalDateTime start = LocalDateTime.of(2020,6,1,0,0);
        LocalDateTime end = start.plusDays(1).minusNanos(1);
        List<CandleResponseItem> candleResponseItems = candleCollectService.extractCandles(start, end,
                Symbol.LTCUSDT,
                Interval.FIFTEEN_MIN);
        Assertions.assertTrue(candleResponseItems.size() > 0);
//        System.out.println(candleResponseItems);
        candleResponseItems.forEach(e ->{
            System.out.println(e.simplePrint());
        });
    }
}