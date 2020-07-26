package com.codeinvestigator.cryptobotspring.candlecollect;

import lombok.Getter;

@Getter
public enum Interval {
    ONE_MIN("1m"),
    THREE_MIN("3m"),
    FIVE_MIN("5m"),
    FIFTEEN_MIN("15m"),
    THIRTY_MIN("30m"),
    ONE_HOUR("1h"),
    TWO_HOUR("2h"),
    FOUR_HOUR("4h"),
    SIX_HOUR("6h"),
    EIGHT_HOUR("8h"),
    TWELVE_HOUR("12h"),
    ONE_DAY("1d"),
    THREE_DAY("3d"),
    ONE_WEEK("1w"),
    ONE_MONTH("1M");
    private final String code;

    Interval(String code) {
        this.code = code;
    }
}
