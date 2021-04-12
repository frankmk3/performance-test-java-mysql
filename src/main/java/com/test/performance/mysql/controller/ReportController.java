package com.test.performance.mysql.controller;

import com.test.performance.mysql.common.Constants;
import com.test.performance.mysql.common.ReportDataGenerator;
import com.test.performance.mysql.dto.ReportCreation;
import com.test.performance.mysql.dto.ReportDTO;
import com.test.performance.mysql.model.Report;
import com.test.performance.mysql.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to manage reports information
 */
@Slf4j
@RestController
@Api(tags = {ReportController.LABEL})
@RequestMapping("/reports")
public class ReportController {

    public static final String LABEL = "Reports";

    private final ReportService reportService;

    private final Constants constants;

    @Autowired
    public ReportController(
        final ReportService reportService,
        final Constants constants
    ) {
        this.reportService = reportService;
        this.constants = constants;
    }

    /**
     * Get reports information
     *
     * @return a paged response with status 200 and the resultant entity collection. In case of bad query parameter,
     * 400. In case of bad credentials, 401
     */
    @ApiOperation(value = "Get reports", tags = LABEL)
    @GetMapping
    public Page<Report> getReports(
        @RequestParam(name = "group", required = false) final String group,
        @RequestParam(name = "page", required = false, defaultValue = "-1") final int page,
        @RequestParam(name = "size", required = false, defaultValue = "-1") final int size) {
        final int requestPage = page < 0 ? constants.getPaginatorPage() : page;
        final int requestSize =
            size > 0 && size <= constants.getPaginatorMaxSize() ? size : constants.getPaginatorSize();
        final Pageable pageable = PageRequest.of(requestPage, requestSize);
        return reportService.getAll(
            group,
            pageable
        );
    }

    /**
     * Create single report
     *
     * @return a paged response with status 200 and the resultant entity collection. In case of bad query parameter,
     * 400. In case of bad credentials, 401
     */
    @ApiOperation(value = "Add report", tags = LABEL)
    @PostMapping
    public Report createReport(
        @RequestBody final ReportDTO reportDTO
    ) {
        long startTime = System.currentTimeMillis();

        Report report = reportService.create(
            ReportDataGenerator.generateReport(reportDTO.getGroup())
        );
        long endTime = System.currentTimeMillis() - startTime;
        log.info(String.format("Time elapsed for report creation: %d", endTime));

        return report;
    }

    /**
     * Update report information
     *
     * @return a paged response with status 200 and the resultant entity collection. In case of bad query parameter,
     * 400. In case of bad credentials, 401
     */
    @ApiOperation(value = "Update report", tags = LABEL)
    @PutMapping("/{id}")
    public ResponseEntity updateReport(
        @PathVariable final String id,
        @RequestBody final ReportDTO reportUpdate
    ) {
        long startTime = System.currentTimeMillis();
        Optional<Report> report = reportService.getById(id);
        if (report.isPresent()) {
            Report reportToUpdate = report.get();
            reportToUpdate.setGroup(reportUpdate.getGroup());
            Report reportResponse = reportService.update(reportToUpdate);
            long endTime = System.currentTimeMillis() - startTime;
            log.info(String.format("Time elapsed for report update: %d", endTime));
            return ResponseEntity.ok(reportResponse);
        }
        return ResponseEntity.notFound()
                             .build();
    }

    /**
     * Create multiple reports information
     *
     * @return a paged response with status 200 and the resultant entity collection. In case of bad query parameter,
     * 400. In case of bad credentials, 401
     */
    @ApiOperation(value = "Add multi reports", tags = LABEL)
    @PostMapping("/multi")
    public ReportCreation createMultiReports(
        @RequestBody final ReportCreation reportCreation
    ) {
        long startTime = System.currentTimeMillis();
        LongStream.range(0, reportCreation.getAmount())
                  .forEach(c ->
                      reportService.create(
                          ReportDataGenerator.generateReport(reportCreation.getGroup())
                      )
                  );
        long endTime = System.currentTimeMillis() - startTime;
        log.info(String.format("Time elapsed for (%d): %d", reportCreation.getAmount(), endTime));
        return reportCreation;
    }

}