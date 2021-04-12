package com.test.performance.mysql.service;

import com.test.performance.mysql.model.Report;
import com.test.performance.mysql.repository.ReportRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public Page<Report> getAll(final String group, final Pageable pageable) {
        if (StringUtils.hasText(group)) {
            return reportRepository.findAllByGroupAndEnabledIsTrue(group, pageable);
        } else {
            return reportRepository.findAllByEnabledIsTrue(pageable);
        }
    }

    public Report update(Report report) {
        return reportRepository.save(report);
    }

    public Optional<Report> getById(String id) {
        return reportRepository.findById(id);
    }
}