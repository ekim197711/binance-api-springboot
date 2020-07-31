package com.codeinvestigator.cryptobotspring.candlecollect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
//@Tag("integration")
public class MineDataTest {

    @Autowired
    CandleCollectService candleCollectService;

    @Autowired
    CandleItemRepository repository;

    @Test
    public void mine2020() {
        Symbol symbol = Symbol.LTCUSDT;
        Interval interval = Interval.ONE_HOUR;
        candleCollectService.mineData(
                LocalDateTime.of(2020, 6, 1, 0, 0),
                LocalDateTime.of(2020, 7, 30, 12, 0),
                symbol, interval
                );
    }


}