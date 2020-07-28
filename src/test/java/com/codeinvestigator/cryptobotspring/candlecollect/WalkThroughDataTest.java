package com.codeinvestigator.cryptobotspring.candlecollect;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class WalkThroughDataTest {

    @Autowired
    CandleItemRepository candleItemRepository;

    @Test
    public void gothroughLTC15Min(){
        Page<CandleItem> items = candleItemRepository.findBySymbolAndIntervalOrderByOpenTime(
                Symbol.LTCUSDT,
                Interval.FIFTEEN_MIN,
                PageRequest.of(0, 99999, Sort.by(Sort.Direction.ASC, "openTime")));
        items.getContent().forEach(item -> System.out.println(item.simpleToString()));
        System.out.println("Went through " + items.getTotalElements() + " elements.");
    }
}