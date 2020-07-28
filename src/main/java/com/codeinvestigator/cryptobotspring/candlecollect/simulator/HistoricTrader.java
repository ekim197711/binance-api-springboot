package com.codeinvestigator.cryptobotspring.candlecollect.simulator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.CandleItemRepository;
import com.codeinvestigator.cryptobotspring.candlecollect.Interval;
import com.codeinvestigator.cryptobotspring.candlecollect.Symbol;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class HistoricTrader {
    private CandleItemRepository repository;

    public HistoricTraderSummary simulate(Symbol symbol, Interval interval, Collection<HistoricTraderStrategy> strategies) {
        HistoricTraderSummary summary = new HistoricTraderSummary();
        Page<CandleItem> items = repository.findBySymbolAndIntervalOrderByOpenTime(symbol, interval, PageRequest.of(0, 999999,
                Sort.Direction.ASC, "openTime"));


        return summary;
    }

}
