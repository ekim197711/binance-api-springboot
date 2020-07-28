package com.codeinvestigator.cryptobotspring.candlecollect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleCollectService {
    private final CandleCollectConfiguration candleCollectConfiguration;
    private final CandleItemRepository repository;

    public void mineData(LocalDateTime begin, LocalDateTime end, Symbol symbol, Interval interval) {
        log.info("Mine data for: {} to: {} for {} with interval: {}", begin, end, symbol, interval);
        repository.deleteWithinTime(symbol,
                interval,
                begin.toInstant(ZoneOffset.UTC).toEpochMilli(),
                end.toInstant(ZoneOffset.UTC).toEpochMilli());
        LocalDateTime current = begin.minusDays(0);
        while (current.isBefore(end)) {
            List<CandleItem> items = extractCandles(current, current.plusDays(1), symbol, interval);
            repository.saveAll(items);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            current = current.plusDays(1);
        }
    }

    public List<CandleItem> extractCandles(LocalDateTime begin, LocalDateTime end, Symbol symbol, Interval interval) {
        log.info("Extract candles: {} to {} symbol {} interval {}", begin,end,symbol,interval);
        RestTemplate rt = new RestTemplate();
        URI url = UriComponentsBuilder.fromHttpUrl(candleCollectConfiguration.getCandleUrlPrefix()
                + candleCollectConfiguration.getCandleUrl())
                .queryParam(candleCollectConfiguration.getCandleUrlQueryStartTime()
                        , begin.toEpochSecond(ZoneOffset.UTC) * 1000)
                .queryParam(candleCollectConfiguration.getCandleUrlQuerySymbol(),
                        symbol.getCode())
                .queryParam(candleCollectConfiguration.getCandleUrlQueryInterval(),
                        interval.getCode())
                .queryParam(candleCollectConfiguration.getCandleUrlQueryLimit(), 1000)
                .queryParam(candleCollectConfiguration.getCandleUrlQueryEndTime(),
                        end.toEpochSecond(ZoneOffset.UTC) * 1000)
                .build().toUri();

        log.debug("Url: {}", url);
        ResponseEntity<List<List<Object>>> exchange = rt.exchange(new RequestEntity<>(HttpMethod.GET, url),
                new ParameterizedTypeReference<>() {
                });
        List<List<Object>> response = exchange.getBody();

        if (response == null) {
            log.warn("No response from service!");
            return new ArrayList<>();
        }

        List<CandleItem> collect = response.stream()
                .map(l -> CandleItem.fromArray(l, symbol, interval))
                .collect(Collectors.toList());
        log.info("items extracted: {}", collect.size());
        return collect;
    }
}
