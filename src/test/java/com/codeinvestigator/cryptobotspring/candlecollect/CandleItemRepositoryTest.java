package com.codeinvestigator.cryptobotspring.candlecollect;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Tag("dangerous")
public class CandleItemRepositoryTest {

    @Autowired
    CandleItemRepository repository;

    private CandleItem createTestData(Symbol symbol, Interval interval) {
        return CandleItem
                .builder()
                .numberOfTrades(new BigInteger("20"))
                .close(BigDecimal.TEN)
                .open((BigDecimal.TEN))
                .closeTime(100l)
                .openTime(100l)
                .Ignore(BigDecimal.TEN)
                .volume(BigDecimal.TEN)
                .symbol(symbol)
                .interval(interval)
                .high(BigDecimal.TEN)
                .low(BigDecimal.TEN)
                .build();
    }

    @Test
    public void testcreateAndDeleteTimeRange() {
        repository.deleteAll();
        CandleItem testData1 = createTestData(Symbol.LTCUSDT, Interval.FIFTEEN_MIN);
        testData1.setOpenTime(
                LocalDateTime.of(2020, 2, 1, 0, 0)
                        .toInstant(ZoneOffset.UTC).toEpochMilli());

        CandleItem testData2 = createTestData(Symbol.LTCUSDT, Interval.FIFTEEN_MIN);
        testData2.setOpenTime(
                LocalDateTime.of(2020, 2, 2, 0, 0)
                        .toInstant(ZoneOffset.UTC).toEpochMilli());

        repository.save(testData1);
        repository.save(testData2);
        assertEquals(2, repository.count());

        repository.deleteWithinTime(
                Symbol.LTCUSDT, Interval.FIFTEEN_MIN,
                LocalDateTime.of(2020, 2, 1, 0, 0)
                        .toInstant(ZoneOffset.UTC).toEpochMilli()
                ,LocalDateTime.of(2020, 2, 2, 0, 0)
                        .toInstant(ZoneOffset.UTC).toEpochMilli());
        assertEquals(1, repository.count());
        List<CandleItem> all = repository.findAll();
        System.out.println(all);
    }

    @Test
    public void testcreateAndDelete() {
        repository.deleteAll();
        repository.save(createTestData(Symbol.LTCUSDT, Interval.FIFTEEN_MIN));
        repository.save(createTestData(Symbol.LTCUSDT, Interval.FIFTEEN_MIN));
        repository.save(createTestData(Symbol.LTCUSDT, Interval.FIFTEEN_MIN));
        repository.save(createTestData(Symbol.BTCUSDT, Interval.FIFTEEN_MIN));
        repository.save(createTestData(Symbol.LTCUSDT, Interval.FOUR_HOUR));
        assertEquals(5, repository.count());
        repository.deleteBySymbolAndInterval(Symbol.LTCUSDT, Interval.FIFTEEN_MIN);
        assertEquals(2, repository.count());
        List<CandleItem> all = repository.findAll();
        System.out.println(all);
    }
    
    @Test
    public void testMany() {
        repository.deleteAll();
        repository.saveAll(List.of(
                createTestData(Symbol.LTCUSDT, Interval.FIFTEEN_MIN),
        createTestData(Symbol.LTCUSDT, Interval.FIFTEEN_MIN),
        createTestData(Symbol.LTCUSDT, Interval.FIFTEEN_MIN),
        createTestData(Symbol.BTCUSDT, Interval.FIFTEEN_MIN),
        createTestData(Symbol.LTCUSDT, Interval.FOUR_HOUR)));
        assertEquals(5, repository.count());
    }
}