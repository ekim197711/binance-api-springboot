package com.codeinvestigator.cryptobotspring.candlecollect;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("dangerous")
public class CleanUpDateTest {

    @Autowired
    CandleItemRepository candleItemRepository;

    @Test
    public void cleanUp(){
        candleItemRepository.deleteBySymbolAndInterval(Symbol.LTCUSDT, Interval.FIFTEEN_MIN);
    }
}