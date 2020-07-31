package com.codeinvestigator.cryptobotspring.candlecollect;

import lombok.Getter;

@Getter
public enum Interval {
    ONE_MIN("1m",1),
    THREE_MIN("3m",3),
    FIVE_MIN("5m",5),
    FIFTEEN_MIN("15m",15),
    THIRTY_MIN("30m",30),
    ONE_HOUR("1h",60),
    TWO_HOUR("2h",120),
    FOUR_HOUR("4h",240),
    SIX_HOUR("6h",360),
    EIGHT_HOUR("8h",480),
    TWELVE_HOUR("12h",720),
    ONE_DAY("1d",1440),
    THREE_DAY("3d",4320),
    ONE_WEEK("1w",10080),
    ONE_MONTH("1M", 43800);
    private final String code;
    private Integer minutes;

    public double chunksPerDay(){
       return 1440d / minutes;
    }

    Interval(String code, Integer minutes) {
        this.code = code;
        this.minutes = minutes;
    }

}
