package com.example.service;

import com.example.dto.ArticleCreateDTO;
import com.example.dto.ArticleResponseDTO;
import com.example.dto.ArticleStatisticsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {

    ArticleResponseDTO createArticle(ArticleCreateDTO articleCreateDTO);

    Page<ArticleResponseDTO> getAllArticles(Pageable pageable);

    List<ArticleStatisticsDTO> getArticleStatisticsForLast7Days();
}