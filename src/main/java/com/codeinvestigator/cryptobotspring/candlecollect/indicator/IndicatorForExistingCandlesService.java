package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.CandleItemRepository;
import com.codeinvestigator.cryptobotspring.candlecollect.Interval;
import com.codeinvestigator.cryptobotspring.candlecollect.Symbol;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndicatorForExistingCandlesService {
    private final CandleItemRepository repository;

    public void calcIndicatorsFromDB(Symbol symbol, Interval interval){
        List<CandleItem> candles = repository.findAllForSymbolAndInterval(symbol, interval);
        log.info("Found candles: {} for symbol {} and interval {}",
                candles.size(),
                symbol,
                interval);
        calcIndicators(candles);
    }

    public List<CandleItem>  calcIndicators(List<CandleItem> candles) {
        for (int i = 1; i < candles.size(); i++) {
            CandleItem candleItem = candles.get(i);
            List<CandleItem> history = candles.subList(0, i);
            candleItem.setIndicator(Indicator.calculate(history, candleItem));
            candleItem.setIndicatorCalculated(true);
        }
        List<CandleItem> candleItems = repository.saveAll(candles);
        return candleItems;
    }

}
