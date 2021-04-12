package com.test.performance.mysql.service;

import static org.mockito.Mockito.when;

import com.test.performance.mysql.common.ReportDataGenerator;
import com.test.performance.mysql.model.Report;
import com.test.performance.mysql.repository.ReportRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    void reportUpdate_whenReportIsNotNull_returnUpdatedReport() {
        String group = "group-1";
        Report report = ReportDataGenerator.generateReport(group);
        when(reportRepository.save(report)).thenReturn(report);

        Report reportResponse = reportService.update(report);

        Assertions.assertEquals(report, reportResponse);
    }

    @Test
    void getById_whenIdExist_returnReport() {
        String group = "group-q";
        Report report = ReportDataGenerator.generateReport(group);
        when(reportRepository.findById(report.getId())).thenReturn(Optional.of(report));

        Optional<Report> reportResponse = reportService.getById(report.getId());

        Assertions.assertEquals(Optional.of(report), reportResponse);
    }

    @Test
    void getById_whenIdDoNotExist_returnEmptyReport() {
        when(reportRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

        Optional<Report> reportResponse = reportService.getById(UUID.randomUUID().toString());

        Assertions.assertEquals(Optional.empty(), reportResponse);
    }

    @Test
    void getAllReport_whenIsUnPaged_returnAllEnabledReports() {
        int total = 2;
        List<Report> reports = IntStream.of(0, total)
                                        .mapToObj(p -> ReportDataGenerator.generateReport("group-" + p))
                                        .collect(Collectors.toList());
        Page<Report> expected = new PageImpl(reports, Pageable.unpaged(), total);
        when(
            reportRepository.findAllByEnabledIsTrue(Pageable.unpaged())
        ).thenReturn(expected);

        Page<Report> response = reportService.getAll("", Pageable.unpaged());

        Assertions.assertEquals(expected, response);
    }

    @Test
    void getAllReport_whenIsPagerIsProvides_returnAllEnabledReportsForPage() {
        Pageable pageable = PageRequest.of(0, 20);
        int total = 6;
        List<Report> reports = IntStream.of(0, total)
                                        .mapToObj(p -> ReportDataGenerator.generateReport("g-" + p))
                                        .collect(Collectors.toList());
        PageImpl expected = new PageImpl(reports, pageable, total);
        when(reportRepository.findAllByEnabledIsTrue(pageable)
        ).thenReturn(expected);

        Iterable<Report> response = reportService.getAll("", pageable);

        Assertions.assertEquals(expected, response);
    }
}