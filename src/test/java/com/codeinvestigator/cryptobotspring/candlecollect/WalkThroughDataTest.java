package com.codeinvestigator.cryptobotspring.candlecollect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        CandleItem ciTest = allForSymbolAndInterval.stream().filter(i -> i.openDateTime().isEqual(LocalDateTime.of(2020, 7, 29, 5, 0)))
                .collect(Collectors.toList()).get(0);
        System.out.println("RSI: " + ciTest.getIndicator().getRsi());
        System.out.println("Indicators: " + ciTest.getIndicator());
        System.out.println("candle: " + ciTest.simpleToString());
        Assertions.assertTrue(ciTest.getIndicator().getRsi().doubleValue() > 59.0);
        Assertions.assertTrue(ciTest.getIndicator().getRsi().doubleValue() < 60.0);
    }
}