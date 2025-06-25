package com.example.service.impl;

import com.example.dto.ArticleCreateDTO;
import com.example.dto.ArticleResponseDTO;
import com.example.dto.ArticleStatisticsDTO;
import com.example.model.Article;
import com.example.repository.ArticleRepository;
import com.example.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public ArticleResponseDTO createArticle(ArticleCreateDTO articleCreateDTO) {
        log.info("📝 Creating a new article with title: {}", articleCreateDTO.getTitle());

        Article article = Article.builder()
                .title(articleCreateDTO.getTitle())
                .author(articleCreateDTO.getAuthor())
                .content(articleCreateDTO.getContent())
                .publicationDate(articleCreateDTO.getPublicationDate())
                .build();

        Article savedArticle = articleRepository.save(article);
        log.info("✅ Article created successfully with ID: {}", savedArticle.getId());

        return mapToDTO(savedArticle);
    }

    @Override
    public Page<ArticleResponseDTO> getAllArticles(Pageable pageable) {
        log.info("📋 Fetching articles page {} with size {}",
                pageable.getPageNumber(), pageable.getPageSize());

        return articleRepository.findAllByOrderByPublicationDateDesc(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public List<ArticleStatisticsDTO> getArticleStatisticsForLast7Days() {
        log.info("📊 Generating article statistics for the last 7 days");

        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(6);

        LocalDateTime startDate = sevenDaysAgo.atStartOfDay();
        LocalDateTime endDate = today.atTime(LocalTime.MAX);

        List<Object[]> results = articleRepository.countArticlesByPublicationDateBetween(startDate, endDate);
        List<ArticleStatisticsDTO> statistics = convertToStatisticsDTOs(results);

        // Fill in missing dates with zero counts
        return fillMissingDates(statistics, sevenDaysAgo, today);
    }

    private ArticleResponseDTO mapToDTO(Article article) {
        return ArticleResponseDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .author(article.getAuthor())
                .content(article.getContent())
                .publicationDate(article.getPublicationDate())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }

    private List<ArticleStatisticsDTO> convertToStatisticsDTOs(List<Object[]> results) {
        return results.stream()
                .map(result -> new ArticleStatisticsDTO(
                        ((java.sql.Date) result[0]).toLocalDate(),
                        ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }

    private List<ArticleStatisticsDTO> fillMissingDates(
            List<ArticleStatisticsDTO> statistics, LocalDate startDate, LocalDate endDate) {

        List<ArticleStatisticsDTO> completedStats = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            final LocalDate dateToFind = currentDate;
            ArticleStatisticsDTO dayStats = statistics.stream()
                    .filter(stat -> stat.getDate().equals(dateToFind))
                    .findFirst()
                    .orElse(new ArticleStatisticsDTO(currentDate, 0L));

            completedStats.add(dayStats);
            currentDate = currentDate.plusDays(1);
        }

        return completedStats;
    }
}