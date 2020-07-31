package com.codeinvestigator.cryptobotspring.candlecollect;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

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

    @Test
    public void ltc2020Hour(){
        Symbol symbol = Symbol.LTCUSDT;
        Interval interval = Interval.ONE_HOUR;
        List<CandleItem> allForSymbolAndInterval = candleItemRepository.findAllForSymbolAndInterval(symbol, interval);
        System.out.println("No of elements: " + allForSymbolAndInterval.size());
        allForSymbolAndInterval.forEach(i ->{
            System.out.println(i.indicatorString());
        });
    }
}