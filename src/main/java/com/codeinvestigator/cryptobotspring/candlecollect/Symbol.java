package com.codeinvestigator.cryptobotspring.candlecollect;

import lombok.Getter;

@Getter
public enum Symbol {
    BTCUSDT("BTCUSDT"),
    LTCUSDT("LTCUSDT");
    private String code;

    Symbol(String code) {
        this.code = code;
    }
}
