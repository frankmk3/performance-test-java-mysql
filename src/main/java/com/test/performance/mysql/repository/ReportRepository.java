package com.test.performance.mysql.repository;

import com.test.performance.mysql.model.Report;
import org.springframework.data.repository.CrudRepository;

public interface ReportRepository extends CrudRepository<Report, String> {
//    @Query("{'$and':[{'$or':[{'name':{$ne:null}}, {'enabled':true}]}]}")
//    Flux<Report> findAllPageable(Pageable page);
//
//    Mono<Report> findByIdAndEnabledIsTrue(String id);
//
//    @Query("{'$and':[ {_id:'?0'},{'$or':[{'name':{$ne:null}}, {'enabled':true}]}]}")
//    Mono<Report> findByIdAndEnabledIsTrueOrNameNotNull(String id);
//
//    Mono<Report> findOneById(String id);

}
