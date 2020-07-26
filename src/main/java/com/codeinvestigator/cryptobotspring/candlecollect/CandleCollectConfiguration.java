package com.codeinvestigator.cryptobotspring.candlecollect;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("candlecollect")
@Data
public class CandleCollectConfiguration {
    private String candleUrlPrefix;
    private String candleUrl;
    private String candleUrlQuerySymbol;
    private String candleUrlQueryInterval;
    private String candleUrlQueryStartTime;
    private String candleUrlQueryEndTime;
    private String candleUrlQueryLimit;
}
