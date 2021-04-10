package com.test.performance.mysql.service;

import com.test.performance.mysql.model.Report;
import com.test.performance.mysql.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final ReportRepository reportRepository;


    @Autowired
    public ReportService(final ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report create(final Report report) {
        return reportRepository.save(report);
    }

    public Iterable<Report> getAll(final String group, final Pageable pageable) {
//        Query query = new Query();
//        if (pageable.isPaged()) {
//            query.skip(pageable.getPageNumber() * pageable.getPageSize());
//            query.limit(pageable.getPageSize());
//        }
//        query.addCriteria(Criteria.where("enabled")
//                                  .is(true));
//        if (StringUtils.hasText(group)) {
//            query.addCriteria(Criteria.where("group")
//                                      .is(group));
//        }
        return reportRepository.findAll();
    }
}