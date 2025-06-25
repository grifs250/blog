package com.example.controller;

import com.example.dto.ArticleStatisticsDTO;
import com.example.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    private List<ArticleStatisticsDTO> statisticsDTOs;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        ArticleStatisticsDTO todayStats = ArticleStatisticsDTO.builder()
                .date(today)
                .count(3L)
                .build();

        ArticleStatisticsDTO yesterdayStats = ArticleStatisticsDTO.builder()
                .date(yesterday)
                .count(2L)
                .build();

        statisticsDTOs = Arrays.asList(todayStats, yesterdayStats);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get statistics - access denied")
    void getStatisticsAccessDenied() throws Exception {
        mockMvc.perform(get("/api/statistics"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Get statistics - success")
    void getStatisticsSuccess() throws Exception {
        when(articleService.getArticleStatisticsForLast7Days()).thenReturn(statisticsDTOs);

        mockMvc.perform(get("/api/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].date").exists())
                .andExpect(jsonPath("$[0].count").value(3));
    }
}