package com.codeinvestigator.cryptobotspring.candlecollect;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigInteger;

public interface CandleResponseItemRepository extends MongoRepository<CandleResponseItem, String> {

    void deleteBySymbolAndInterval(Symbol symbol, Interval interval);

    @Query(value=""+
            "{" +
            "'symbol': {$eq: ?0}," +
            "'interval': {$eq: ?1}," +
            "'openTime': {$gte: ?2}," +
            "'openTime': {$lt: ?3} " +
            "}" , delete = true
    )
    void deleteWithinTime(Symbol symbol,
                                                                            Interval interval,
                                                                            Long greater,
                                                                            Long less
                                                                                   );


    Page<CandleResponseItem> findBySymbolAndIntervalOrderByOpenTime(Symbol symbol, Interval interval, Pageable pageRequest);
}
