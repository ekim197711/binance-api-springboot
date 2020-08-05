package com.codeinvestigator.cryptobotspring.candlecollect;

import com.codeinvestigator.cryptobotspring.candlecollect.indicator.Indicator;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Builder
@Data
public class CandleItem {

    public static CandleItem fromArray(List<Object> fields, Symbol symbol, Interval interval){
        int i = 0;
        return CandleItem.builder()
                .symbol(symbol)
                .interval(interval)
                .openTime((Long)fields.get(i++))
                .open(new BigDecimal(fields.get(i++).toString()))
                .high(new BigDecimal(fields.get(i++).toString()))
                .low(new BigDecimal(fields.get(i++).toString()))
                .close(new BigDecimal(fields.get(i++).toString()))
                .volume(new BigDecimal(fields.get(i++).toString()))
                .closeTime((Long)fields.get(i++))
                .quoteAssetVolume(new BigDecimal(fields.get(i++).toString()))
                .numberOfTrades(BigInteger.valueOf(Long.parseLong(fields.get(i++).toString())))
                .takerBuyBaseAssetVolume(new BigDecimal(fields.get(i++).toString()))
                .takerBuyQuoteAssetVolume(new BigDecimal(fields.get(i++).toString()))
                .Ignore(new BigDecimal(fields.get(i).toString()))
                .build();
    }

    @Id
    private String _id;
    private Symbol symbol;
    private Interval interval;

    private Long openTime;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private BigDecimal volume;
    private Long closeTime;
    private BigDecimal quoteAssetVolume;
    private BigInteger numberOfTrades;
    private BigDecimal takerBuyBaseAssetVolume;
    private BigDecimal takerBuyQuoteAssetVolume;
    private BigDecimal Ignore;
    private Indicator indicator;
    private boolean indicatorCalculated;
    {
        indicatorCalculated = false;
        indicator = new Indicator();
    }
    public LocalDateTime openDateTime(){
        return LocalDateTime.ofEpochSecond(openTime/1000, (int)(openTime%1000), ZoneOffset.UTC);
    }

    public LocalDateTime closeDateTime(){
        return LocalDateTime.ofEpochSecond(closeTime/1000, (int)(closeTime%1000), ZoneOffset.UTC);
    }

    public BigDecimal difference(){
        return close.subtract(open);
    }

    public BigDecimal differencePercentage(){
        return close.subtract(open).divide(open,6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal gain(){
        return BigDecimal.valueOf(Math.max(difference().doubleValue(), 0.0d));
    }
    public BigDecimal loose(){
        return BigDecimal.valueOf(Math.min(difference().doubleValue(), 0.0d));
    }

    public String simpleToString(){
        return String.format("%s - %s volume: %s diff: %s "
                , openDateTime()
                , closeDateTime()
                , volume
                , differencePercentage());
    }


    public String indicatorString() {
        return simpleToString() + " indicators " + this.getIndicator();
    }
}
