package com.codeinvestigator.cryptobotspring.candlecollect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleCollectService {
    private final CandleCollectConfiguration candleCollectConfiguration;

    public List<CandleResponseItem>  extractCandles(LocalDateTime begin, LocalDateTime end, Symbol symbol, Interval interval) {

        RestTemplate rt = new RestTemplate();
        URI url = UriComponentsBuilder.fromHttpUrl(candleCollectConfiguration.getCandleUrlPrefix()
                + candleCollectConfiguration.getCandleUrl())
                .queryParam(candleCollectConfiguration.getCandleUrlQueryStartTime()
                        , begin.toEpochSecond(ZoneOffset.UTC)*1000)
                .queryParam(candleCollectConfiguration.getCandleUrlQuerySymbol(),
                        symbol.getCode())
                .queryParam(candleCollectConfiguration.getCandleUrlQueryInterval(),
                        interval.getCode())
                .queryParam(candleCollectConfiguration.getCandleUrlQueryLimit(),1000)
                .queryParam(candleCollectConfiguration.getCandleUrlQueryEndTime(),
                        end.toEpochSecond(ZoneOffset.UTC)*1000)
                .build().toUri();

        RequestEntity<String> request = new RequestEntity<>(HttpMethod.GET, url);
        log.info("Url: {}",url);
        List response = rt.getForObject(url, List.class);
        List<CandleResponseItem> collect = (List<CandleResponseItem>)response.stream()
                .map(o -> CandleResponseItem.fromArray((List) o))
                .collect(Collectors.toList());
//        log.info("Collected {} candles",collect.size());
        return collect;
    }
}
