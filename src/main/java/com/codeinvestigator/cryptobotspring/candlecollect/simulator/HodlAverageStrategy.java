package com.codeinvestigator.cryptobotspring.candlecollect.simulator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import com.codeinvestigator.cryptobotspring.candlecollect.indicator.Indicators;
import com.codeinvestigator.cryptobotspring.candlecollect.simulator.strategy.HistoricTraderStrategy;

import java.util.List;

public class HodlAverageStrategy extends HistoricTraderStrategy {
    @Override
    public void giveInfo(Indicators indicators, List<CandleItem> history) {

    }
}
