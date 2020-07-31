package com.codeinvestigator.cryptobotspring.candlecollect;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CandleItemRepository extends MongoRepository<CandleItem, String> {

    void deleteBySymbolAndInterval(Symbol symbol, Interval interval);

    @Query(value = "" +
            "{" +
            "'symbol': {$eq: ?0}," +
            "'interval': {$eq: ?1}," +
            "'openTime': {$gte: ?2}," +
            "'openTime': {$lt: ?3} " +
            "}", delete = true
    )
    void deleteWithinTime(Symbol symbol,
                          Interval interval,
                          Long greater,
                          Long less
    );

    @Query(value = "" +
            "{" +
            "'symbol': {$eq: ?0}," +
            "'interval': {$eq: ?1}," +
            "'openTime': {$gte: ?2}," +
            "'openTime': {$lt: ?3} " +
            "}", delete = false
    )
    List<CandleItem> findWithinTime(Symbol symbol,
                          Interval interval,
                          Long greater,
                          Long less
    );


    Page<CandleItem> findBySymbolAndIntervalOrderByOpenTime(Symbol symbol, Interval interval, Pageable pageRequest);

    @Query(value = "" +
            "{" +
            "'symbol': {$eq: ?0}," +
            "'interval': {$eq: ?1}" +
            "}", delete = true, sort = "{'openTime': 1}"
    )
    List<CandleItem> findAllForSymbolAndInterval(
            Symbol symbol
            , Interval interval);
}
