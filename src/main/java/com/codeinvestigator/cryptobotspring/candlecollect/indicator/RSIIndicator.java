package com.codeinvestigator.cryptobotspring.candlecollect.indicator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeinvestigator.cryptobotspring.candlecollect.indicator.Indicator.BD_SCALE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RSIIndicator {
    Map<Integer, BigDecimal> looses = new HashMap<>();
    Map<Integer, BigDecimal> gains = new HashMap<>();
    private BigDecimal rsi = BigDecimal.valueOf(-1, BD_SCALE);
    private BigDecimal rs = BigDecimal.valueOf(-1, BD_SCALE);
    private static final int MAX_PERIOD = 14;
    {
        looses.put(13, BigDecimal.ZERO);
        looses.put(14, BigDecimal.ZERO);
        gains.put(13, BigDecimal.ZERO);
        gains.put(14, BigDecimal.ZERO);
    }

    public BigDecimal averageGains(int period){
        return gains.get(period).divide(new BigDecimal(period));
    }

    public BigDecimal averageLooses(int period){
        return looses.get(period).divide(new BigDecimal(period));
    }

    public void calculate(List<CandleItem> history){
        calculateGainsAndLooses(history);
        calculateRelativeStrength();
        calculateRelativeStrengthIndex(history);
    }

    public void calculateGainsAndLooses(List<CandleItem> history){
        for (int i = 0; i < history.size() && i < MAX_PERIOD; i++) {
            final int currentIndex = i;
            CandleItem candleItem = history.get(history.size() - 1 - i);
            if (candleItem.difference().doubleValue() >0d){
                gains.entrySet().stream().filter(e -> e.getKey() > currentIndex)
                        .forEach(e -> e.setValue(e.getValue().add(candleItem.difference())));
            }
            else{
                looses.entrySet().stream().filter(e -> e.getKey() > currentIndex)
                        .forEach(e -> e.setValue(e.getValue().add(candleItem.difference().abs())));
            }
        }
    }

    public void calculateRelativeStrength(List<CandleItem> history){
        if (history.size() == 14)
            rs = gains.get(14).divide(looses.get(14), BD_SCALE, RoundingMode.HALF_UP);
        else if (history.size() > 14)
            rs = gains.get(14).divide(looses.get(14), BD_SCALE, RoundingMode.HALF_UP)   ;

    }

    public void calculateRelativeStrengthIndex(List<CandleItem> history){
        CandleItem previous = history.get(history.size() - 1);
        if (history.size() < 14) {
            return;
        }
//        else if (history.size() == 14){
//            100 - 100 / (1+RS)
            rsi = new BigDecimal(100).subtract(
                    new BigDecimal(100).divide(
                            new BigDecimal(1).add(rs)
                    )
            );
//        }
//        else{

//        }
    }

}
