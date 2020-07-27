package com.codeinvestigator.cryptobotspring.candlecollect;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandleResponseItemRepository extends MongoRepository<CandleResponseItem, String> {

}
