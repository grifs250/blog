package com.example.repository;

import com.example.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findAllByOrderByPublicationDateDesc(Pageable pageable);

    @Query("SELECT CAST(a.publicationDate AS DATE) as date, COUNT(a) as count FROM Article a " +
            "WHERE a.publicationDate >= :startDate AND a.publicationDate <= :endDate " +
            "GROUP BY CAST(a.publicationDate AS DATE) ORDER BY CAST(a.publicationDate AS DATE) DESC")
    List<Object[]> countArticlesByPublicationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}