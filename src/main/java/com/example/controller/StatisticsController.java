package com.example.controller;

import com.example.dto.ArticleStatisticsDTO;
import com.example.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {

    private final ArticleService articleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ArticleStatisticsDTO>> getArticleStatistics() {
        log.info("📊 Received request to fetch article statistics for the last 7 days");
        return ResponseEntity.ok(articleService.getArticleStatisticsForLast7Days());
    }
}