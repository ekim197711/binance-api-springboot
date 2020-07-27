package com.codeinvestigator.cryptobotspring.candlecollect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class MineDataTest {

    @Autowired
    CandleCollectService candleCollectService;

    @Test
    public void mine15MinJune2020(){
        candleCollectService.mineData(
                LocalDateTime.of(2020,6,1,0,0),
                LocalDateTime.of(2020,7,1,0,0),
                Symbol.LTCUSDT,
                Interval.FIFTEEN_MIN
        );
    }
}