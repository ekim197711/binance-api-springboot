package com.codeinvestigator.cryptobotspring.candlecollect.simulator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class HistoricTraderStrategy {
    private Position position;
    private final List<Position> positionHistory = new ArrayList<>();
    private BigDecimal bankroll = BigDecimal.valueOf(1000);
    @Getter
    private TradeAction tradeAction;
    public abstract void giveInfo(Indicators indicators, List<CandleItem> history);

    void buy(CandleItem item){
        if (position != null){
            return;
        }
        position = Position.open(item, bankroll);
        log.info("Opened position {}", position);
    }

    void sell(CandleItem item){
        if (position == null){
            return;
        }
        position.close(item);
        positionHistory.add(position);
        position = null;
        log.info("Closed position {}", position);
    }
}
