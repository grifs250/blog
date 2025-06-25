package com.example.service;

import com.example.dto.ArticleCreateDTO;
import com.example.dto.ArticleResponseDTO;
import com.example.dto.ArticleStatisticsDTO;
import com.example.model.Article;
import com.example.repository.ArticleRepository;
import com.example.service.impl.ArticleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private ArticleCreateDTO articleCreateDTO;
    private Article article;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        articleCreateDTO = ArticleCreateDTO.builder()
                .title("Test Article")
                .author("Test Author")
                .content("Test Content")
                .publicationDate(now)
                .build();

        article = Article.builder()
                .id(1L)
                .title("Test Article")
                .author("Test Author")
                .content("Test Content")
                .publicationDate(now)
                .createdAt(now)
                .build();
    }

    @Test
    @DisplayName("Create article - success")
    void createArticleSuccess() {
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        ArticleResponseDTO responseDTO = articleService.createArticle(articleCreateDTO);

        assertNotNull(responseDTO);
        assertEquals(article.getId(), responseDTO.getId());
        assertEquals(article.getTitle(), responseDTO.getTitle());
        assertEquals(article.getAuthor(), responseDTO.getAuthor());
        assertEquals(article.getContent(), responseDTO.getContent());
        assertEquals(article.getPublicationDate(), responseDTO.getPublicationDate());

        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    @DisplayName("Get all articles - success")
    void getAllArticlesSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Article> articles = Arrays.asList(article);
        Page<Article> articlePage = new PageImpl<>(articles, pageable, articles.size());

        when(articleRepository.findAllByOrderByPublicationDateDesc(pageable)).thenReturn(articlePage);

        Page<ArticleResponseDTO> responseDTOs = articleService.getAllArticles(pageable);

        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.getContent().size());
        assertEquals(article.getTitle(), responseDTOs.getContent().get(0).getTitle());

        verify(articleRepository, times(1)).findAllByOrderByPublicationDateDesc(pageable);
    }

    @Test
    @DisplayName("Get article statistics - success")
    void getArticleStatisticsSuccess() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        LocalDateTime startDate = today.minusDays(6).atStartOfDay();
        LocalDateTime endDate = today.atTime(LocalTime.MAX);

        Object[] todayStats = new Object[] { java.sql.Date.valueOf(today), 3L };
        Object[] yesterdayStats = new Object[] { java.sql.Date.valueOf(yesterday), 2L };
        List<Object[]> mockResults = Arrays.asList(todayStats, yesterdayStats);

        when(articleRepository.countArticlesByPublicationDateBetween(any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(mockResults);

        List<ArticleStatisticsDTO> stats = articleService.getArticleStatisticsForLast7Days();

        assertNotNull(stats);
        assertEquals(7, stats.size()); // Should have entries for all 7 days

        // Verify the known data
        boolean foundToday = false;
        boolean foundYesterday = false;

        for (ArticleStatisticsDTO stat : stats) {
            if (stat.getDate().equals(today)) {
                assertEquals(3L, stat.getCount());
                foundToday = true;
            } else if (stat.getDate().equals(yesterday)) {
                assertEquals(2L, stat.getCount());
                foundYesterday = true;
            }
        }

        assert foundToday && foundYesterday;

        verify(articleRepository, times(1))
                .countArticlesByPublicationDateBetween(any(LocalDateTime.class), any(LocalDateTime.class));
    }
}