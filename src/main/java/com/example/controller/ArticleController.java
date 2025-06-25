package com.example.controller;

import com.example.dto.ArticleCreateDTO;
import com.example.dto.ArticleResponseDTO;
import com.example.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/create")
    public ResponseEntity<ArticleResponseDTO> createArticle(@RequestBody @Valid ArticleCreateDTO articleCreateDTO) {
        log.info("📮 Received request to create a new article");
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(articleCreateDTO));
    }

    @GetMapping
    public ResponseEntity<Page<ArticleResponseDTO>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("📮 Received request to fetch articles page {} with size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(articleService.getAllArticles(pageable));
    }
}