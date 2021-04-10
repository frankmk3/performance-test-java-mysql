package com.test.performance.mysql;

import com.test.performance.mysql.controller.ReportController;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReportApplicationTest {
    @Autowired
    private ReportController reportController;

    @Test
    void contextLoads() {
        assertThat(reportController).isNotNull();
    }
}