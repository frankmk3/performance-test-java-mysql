package com.test.performance.mysql.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.performance.mysql.common.Constants;
import com.test.performance.mysql.common.ReportDataGenerator;
import com.test.performance.mysql.dto.ReportCreation;
import com.test.performance.mysql.dto.ReportDTO;
import com.test.performance.mysql.model.Report;
import com.test.performance.mysql.service.ReportService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class ReportControllerTest {

    private static final int PAGE = 0;
    private static final int SIZE = 20;
    private static final int MAX_SIZE = 100;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private Constants constants;

    @BeforeEach
    void init() {
        Mockito.when(constants.getPaginatorPage())
               .thenReturn(PAGE);
        Mockito.when(constants.getPaginatorSize())
               .thenReturn(SIZE);
        Mockito.when(constants.getPaginatorMaxSize())
               .thenReturn(MAX_SIZE);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 5, 2, 0})
    void canCallReportsEndpoint_whenNoParameterProvided_returnReportsUsingDefaultPager(final int elements)
        throws Exception {
        List<Report> reports = IntStream.range(0, elements)
                                        .mapToObj(pos -> ReportDataGenerator.generateReport("gr" + pos))
                                        .collect(
                                            Collectors.toList());

        PageRequest pageable = PageRequest.of(PAGE, SIZE);
        Page<Report> expected = new PageImpl(reports, pageable, elements);
        Mockito.when(reportService.getAll(null, pageable))
               .thenReturn(expected);

        mockMvc.perform(get("/reports")
            .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 5, 2, MAX_SIZE})
    void canCallReportsEndpoint_whenPageSizeIsProvides_returnReportsUsingDefaultPageAndCurrentSize(final int pageSize)
        throws Exception {
        List<Report> reports = IntStream.range(0, pageSize)
                                        .mapToObj(pos -> ReportDataGenerator.generateReport("gp" + pageSize))
                                        .collect(
                                            Collectors.toList());

        PageRequest pageable = PageRequest.of(PAGE, pageSize);
        Page<Report> expected = new PageImpl(reports, pageable, pageSize);
        Mockito.when(reportService.getAll(null, pageable))
               .thenReturn(expected);

        mockMvc.perform(get(String.format("/reports?size=%d", pageSize))
            .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(expected)));

    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100, MAX_SIZE + 1})
    void canCallReportsEndpoint_whenPageSizeIsInvalid_returnReportsUsingDefaultPageAndSize(final int pageSize)
        throws Exception {
        int total = 4;
        List<Report> reports = IntStream.range(0, total)
                                        .mapToObj(pos -> ReportDataGenerator.generateReport("group" + pageSize))
                                        .collect(
                                            Collectors.toList());

        PageRequest pageable = PageRequest.of(PAGE, SIZE);
        Page<Report> expected = new PageImpl(reports, pageable, total);
        Mockito.when(reportService.getAll(null, pageable))
               .thenReturn(expected);

        mockMvc.perform(get(String.format("/reports?size=%d", pageSize))
            .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100, 1000})
    void canCallReportsEndpoint_whenPageIsProvides_returnReportsUsingDefaultSizeAndCurrentPage(final int page)
        throws Exception {
        int total = 3;
        List<Report> reports = IntStream.range(0, total)
                                        .mapToObj(pos -> ReportDataGenerator.generateReport("gr-" + page))
                                        .collect(
                                            Collectors.toList());
        PageRequest pageable = PageRequest.of(page, SIZE);
        Page<Report> expected = new PageImpl(reports, pageable, total);
        Mockito.when(reportService.getAll(null, pageable))
               .thenReturn(expected);

        mockMvc.perform(get(String.format("/reports?page=%d", page))
            .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void canCallReportsEndpoint_whenPagePageIsInvalid_returnReportsUsingDefaultPageAndSize(final int page)
        throws Exception {
        int total = 2;
        List<Report> reports = IntStream.range(0, total)
                                        .mapToObj(pos -> ReportDataGenerator.generateReport("g" + page))
                                        .collect(
                                            Collectors.toList());
        PageRequest pageable = PageRequest.of(PAGE, SIZE);
        Page<Report> expected = new PageImpl(reports, pageable, total);
        Mockito.when(reportService.getAll(null, pageable))
               .thenReturn(expected);

        mockMvc.perform(get(String.format("/reports?page=%d", page))
            .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 10, 5, 2, 0})
    void canCallCreateMultiReportsEndpoint_whenGroupValueIsValid_returnReportsUsingDefaultPager(final long elements)
        throws Exception {
        String group = "group" + elements;
        ReportCreation reportCreation = new ReportCreation(elements, group);

        Mockito.when(reportService.create(ArgumentMatchers.any(Report.class)))
               .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(
            post("/reports/multi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportCreation))
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content()
                   .json(objectMapper.writeValueAsString(reportCreation)));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 10, 5, 2, 0})
    void canCallCreateReportEndpoint_whenGroupValueIsValid_returnCreatedReport(final long groupKey)
        throws Exception {
        String group = "group" + groupKey;
        ReportDTO reportDTO = new ReportDTO(group);
        Report report = ReportDataGenerator.generateReport(group);

        Mockito.when(reportService.create(ArgumentMatchers.any(Report.class)))
               .thenReturn(report);

        mockMvc.perform(
            post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportDTO))
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content()
                   .json(objectMapper.writeValueAsString(report)));
    }

    @ParameterizedTest
    @ValueSource(longs = {10, 110, 15, 22, 30})
    void canCallUpdateReportEndpoint_whenIdExists_returnUpdatedReport(final long groupKey)
        throws Exception {
        String group = "group" + groupKey;
        String newGroup = group + "-new";
        ReportDTO reportDTO = new ReportDTO(newGroup);
        Report report = ReportDataGenerator.generateReport(group);
        Report expected = report.toBuilder()
                                .group(newGroup)
                                .build();

        Mockito.when(reportService.getById(report.getId()))
               .thenReturn(Optional.of(report));
        Mockito.when(reportService.update(report))
               .thenReturn(expected);

        mockMvc.perform(
            put(String.format("/reports/%s", report.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportDTO))
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content()
                   .json(objectMapper.writeValueAsString(expected)));
    }

    @ParameterizedTest
    @ValueSource(longs = {22, 31, 43, 1, 3})
    void canCallUpdateReportEndpoint_whenIdNotExist_returnNotFound(final long groupKey)
        throws Exception {
        String id = UUID.randomUUID()
                        .toString();
        String group = "group" + groupKey;
        ReportDTO reportDTO = new ReportDTO(group);
        Mockito.when(reportService.getById(id))
               .thenReturn(Optional.empty());

        mockMvc.perform(
            put(String.format("/reports/%s", id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportDTO))
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
    }
}