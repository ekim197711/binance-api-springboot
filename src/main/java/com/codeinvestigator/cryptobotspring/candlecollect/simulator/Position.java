package com.codeinvestigator.cryptobotspring.candlecollect.simulator;

import com.codeinvestigator.cryptobotspring.candlecollect.CandleItem;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data
public class Position {
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal quantity;
    private BigDecimal percentageDifference;

    private Position() {
    }

    public void close(CandleItem item) {
        this.closePrice = item.getClose();
        this.closeTime = item.closeDateTime();
        percentageDifference = this.closePrice.subtract(this.openPrice)
                .divide(openPrice, 10, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public static Position open(CandleItem item, BigDecimal bankroll) {
        Position newpos = new Position();
        newpos.openTime = item.closeDateTime();
        newpos.openPrice = item.getClose();
        newpos.quantity = bankroll.divide(newpos.openPrice, 10, RoundingMode.HALF_UP);
        return newpos;
    }
}
