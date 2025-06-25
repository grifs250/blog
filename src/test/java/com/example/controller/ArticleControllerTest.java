package com.example.controller;

import com.example.dto.ArticleCreateDTO;
import com.example.dto.ArticleResponseDTO;
import com.example.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;

    private ArticleCreateDTO articleCreateDTO;
    private ArticleResponseDTO articleResponseDTO;
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

        articleResponseDTO = ArticleResponseDTO.builder()
                .id(1L)
                .title("Test Article")
                .author("Test Author")
                .content("Test Content")
                .publicationDate(now)
                .createdAt(now)
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("Create article - success")
    void createArticleSuccess() throws Exception {
        when(articleService.createArticle(any(ArticleCreateDTO.class))).thenReturn(articleResponseDTO);

        mockMvc.perform(post("/api/articles/create")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Article"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.content").value("Test Content"));
    }

    @Test
    @DisplayName("Get all articles - success")
    void getAllArticlesSuccess() throws Exception {
        PageImpl<ArticleResponseDTO> articlePage = new PageImpl<>(
                Collections.singletonList(articleResponseDTO),
                PageRequest.of(0, 10),
                1);

        when(articleService.getAllArticles(any())).thenReturn(articlePage);

        mockMvc.perform(get("/api/articles")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Test Article"))
                .andExpect(jsonPath("$.content[0].author").value("Test Author"))
                .andExpect(jsonPath("$.content[0].content").value("Test Content"));
    }
}