package com.codeinvestigator.cryptobotspring.candlecollect;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Builder
@Data
public class CandleResponseItem {

    public static CandleResponseItem fromArray(List<Object> fields){
        int i = 0;
        return CandleResponseItem.builder()
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
        return close.subtract(open).divide(open,20, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public String simplePrint(){
        return String.format("%s - %s volume: %s diff: %s "
                , openDateTime()
                , closeDateTime()
                , volume
                , differencePercentage());
    }


}
