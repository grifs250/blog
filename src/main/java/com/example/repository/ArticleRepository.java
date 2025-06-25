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

    @Query("SELECT DATE(a.publicationDate) as date, COUNT(a) as count FROM Article a " +
            "WHERE a.publicationDate >= :startDate AND a.publicationDate <= :endDate " +
            "GROUP BY DATE(a.publicationDate) ORDER BY date DESC")
    List<Object[]> countArticlesByPublicationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}