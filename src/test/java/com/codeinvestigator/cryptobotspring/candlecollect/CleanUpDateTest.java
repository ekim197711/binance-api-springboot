package com.codeinvestigator.cryptobotspring.candlecollect;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
@Tag("dangerous")
public class CleanUpDateTest {

    @Autowired
    CandleResponseItemRepository candleResponseItemRepository;

    @Test
    public void cleanUp(){
        candleResponseItemRepository.deleteBySymbolAndInterval(Symbol.LTCUSDT, Interval.FIFTEEN_MIN);
    }
}