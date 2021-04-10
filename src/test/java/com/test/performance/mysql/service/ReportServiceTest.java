package com.test.performance.mysql.service;

import static org.mockito.Mockito.when;

import com.test.performance.mysql.common.ReportDataGenerator;
import com.test.performance.mysql.model.Report;
import com.test.performance.mysql.repository.ReportRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ReportServiceTest {

    private ReportService reportService;

    @MockBean
    private ReportRepository reportRepository;

    @BeforeEach
    void init() {
        reportRepository = Mockito.mock(ReportRepository.class);
        reportService = new ReportService(reportRepository);
    }

    @Test
    void reportCreate_whenReportIsNotNull_returnCreatedReport() {
        String group = "group-1";
        Report report = ReportDataGenerator.generateReport(group);
        when(reportRepository.save(report)).thenReturn(report);

        Report reportResponse = reportService.create(report);

        Assertions.assertEquals(report, reportResponse);
    }

    @Test
    void getAllReport_whenIsUnPaged_returnAllEnabledReports() {
        List<Report> reports = IntStream.of(0, 2)
                                        .mapToObj(p -> ReportDataGenerator.generateReport("group-" + p))
                                        .collect(Collectors.toList());
        when(reportRepository.findAll()).thenReturn(reports);

        Iterable<Report> response = reportService.getAll("", Pageable.unpaged());

        Assertions.assertEquals(reports, response);
    }

    @Test
    void getAllReport_whenIsPagerIsProvides_returnAllEnabledReportsForPage() {
        Pageable pageable = PageRequest.of(0, 20);
        List<Report> reports = IntStream.of(0, 2)
                                        .mapToObj(p -> ReportDataGenerator.generateReport("g-" + p))
                                        .collect(Collectors.toList());
        when(reportRepository.findAll()).thenReturn(reports);

        Iterable<Report> response = reportService.getAll("", pageable);

        Assertions.assertEquals(reports, response);
    }
}