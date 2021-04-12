package com.test.performance.mysql.repository;

import com.test.performance.mysql.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ReportRepository extends CrudRepository<Report, String> {

    Page<Report> findAllByGroupAndEnabledIsTrue(String group, Pageable pageable);

    Page<Report> findAllByEnabledIsTrue(Pageable pageable);

}
