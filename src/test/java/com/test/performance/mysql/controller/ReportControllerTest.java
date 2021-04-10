package com.test.performance.mysql.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.performance.mysql.common.Constants;
import com.test.performance.mysql.common.ReportDataGenerator;
import com.test.performance.mysql.dto.ReportCreation;
import com.test.performance.mysql.model.Report;
import com.test.performance.mysql.service.ReportService;
import java.util.List;
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

        Mockito.when(reportService.getAll(null, PageRequest.of(PAGE, SIZE)))
               .thenReturn(reports);

        mockMvc.perform(get("/reports")
            .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(reports)));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 5, 2, MAX_SIZE})
    void canCallReportsEndpoint_whenPageSizeIsProvides_returnReportsUsingDefaultPageAndCurrentSize(final int pageSize)
        throws Exception {
        List<Report> reports = IntStream.range(0, pageSize)
                                        .mapToObj(pos -> ReportDataGenerator.generateReport("gp" + pageSize))
                                        .collect(
                                            Collectors.toList());

        Mockito.when(reportService.getAll(null, PageRequest.of(PAGE, pageSize)))
               .thenReturn(reports);

        mockMvc.perform(get(String.format("/reports?size=%d", pageSize))
            .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(reports)));

    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, -100, MAX_SIZE + 1})
    void canCallReportsEndpoint_whenPageSizeIsInvalid_returnReportsUsingDefaultPageAndSize(final int pageSize)
        throws Exception {
        List<Report> reports = IntStream.range(0, 2)
                                        .mapToObj(pos -> ReportDataGenerator.generateReport("group" + pageSize))
                                        .collect(
                                            Collectors.toList());

        Mockito.when(reportService.getAll(null, PageRequest.of(PAGE, SIZE)))
               .thenReturn(reports);

        mockMvc.perform(get(String.format("/reports?size=%d", pageSize))
            .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(reports)));
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100, 1000})
    void canCallReportsEndpoint_whenPageIsProvides_returnReportsUsingDefaultSizeAndCurrentPage(final int page)
        throws Exception {
        List<Report> reports = IntStream.range(0, 3)
                                        .mapToObj(pos -> ReportDataGenerator.generateReport("gr-" + page))
                                        .collect(
                                            Collectors.toList());

        Mockito.when(reportService.getAll(null, PageRequest.of(page, SIZE)))
               .thenReturn(reports);

        mockMvc.perform(get(String.format("/reports?page=%d", page))
            .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(reports)));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
    void canCallReportsEndpoint_whenPagePageIsInvalid_returnReportsUsingDefaultPageAndSize(final int page)
        throws Exception {
        List<Report> reports = IntStream.range(0, 2)
                                        .mapToObj(pos -> ReportDataGenerator.generateReport("g" + page))
                                        .collect(
                                            Collectors.toList());

        Mockito.when(reportService.getAll(null, PageRequest.of(PAGE, SIZE)))
               .thenReturn(reports);

        mockMvc.perform(get(String.format("/reports?page=%d", page))
            .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(reports)));
    }


    @ParameterizedTest
    @ValueSource(longs = {1, 10, 5, 2, 0})
    void canCallCreateReportEndpoint_whenGroupV_returnReportsUsingDefaultPager(final long elements)
        throws Exception {
        String group = "group" + elements;
        ReportCreation reportCreation = new ReportCreation(elements, group);

        Mockito.when(reportService.create(ArgumentMatchers.any(Report.class)))
               .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(
            post("/reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportCreation))
                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content()
                   .json(objectMapper.writeValueAsString(reportCreation)));
    }
}