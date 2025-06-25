package com.example.controller;

import com.example.dto.ArticleCreateDTO;
import com.example.dto.ArticleResponseDTO;
import com.example.service.ArticleService;
import com.example.security.SecurityConfig;
import com.example.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArticleController.class)
@Import({ SecurityConfig.class })
class ArticleControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ArticleService articleService;

        @MockBean
        private UserDetailsServiceImpl userDetailsService;

        @Autowired
        private ObjectMapper objectMapper;

        private ArticleCreateDTO validArticleCreateDTO;
        private ArticleResponseDTO sampleArticleResponseDTO;

        @BeforeEach
        void setUp() {
                validArticleCreateDTO = ArticleCreateDTO.builder()
                                .title("Test Article")
                                .author("Test Author")
                                .content("Test Content")
                                .publicationDate(LocalDateTime.now())
                                .build();

                sampleArticleResponseDTO = ArticleResponseDTO.builder()
                                .id(1L)
                                .title("Test Article")
                                .author("Test Author")
                                .content("Test Content")
                                .publicationDate(LocalDateTime.now())
                                .createdAt(LocalDateTime.now())
                                .build();
        }

        @Test
        @DisplayName("Create article - success")
        @WithMockUser(authorities = "ROLE_USER")
        void createArticleSuccess() throws Exception {
                when(articleService.createArticle(any(ArticleCreateDTO.class)))
                                .thenReturn(sampleArticleResponseDTO);

                mockMvc.perform(post("/api/articles/create")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validArticleCreateDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Test Article"))
                                .andExpect(jsonPath("$.author").value("Test Author"));
        }

        @Test
        @DisplayName("Get all articles - success (public endpoint)")
        @WithAnonymousUser
        void getAllArticlesSuccess() throws Exception {
                List<ArticleResponseDTO> articles = Arrays.asList(sampleArticleResponseDTO);
                PageImpl<ArticleResponseDTO> page = new PageImpl<>(articles, PageRequest.of(0, 10), 1);

                when(articleService.getAllArticles(any(Pageable.class))).thenReturn(page);

                mockMvc.perform(get("/api/articles")
                                .param("page", "0")
                                .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].id").value(1))
                                .andExpect(jsonPath("$.content[0].title").value("Test Article"));
        }

        @Test
        @DisplayName("Create article with invalid data - validation error")
        @WithMockUser(authorities = "ROLE_USER")
        void createArticleWithInvalidData() throws Exception {
                ArticleCreateDTO invalidDTO = ArticleCreateDTO.builder()
                                .title("A".repeat(101)) // Exceeds 100 characters
                                .author("Test Author")
                                .content("Test Content")
                                .publicationDate(LocalDateTime.now())
                                .build();

                mockMvc.perform(post("/api/articles/create")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidDTO)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Create article with empty title - validation error")
        @WithMockUser(authorities = "ROLE_USER")
        void createArticleWithEmptyTitle() throws Exception {
                ArticleCreateDTO invalidDTO = ArticleCreateDTO.builder()
                                .title("")
                                .author("Test Author")
                                .content("Test Content")
                                .publicationDate(LocalDateTime.now())
                                .build();

                mockMvc.perform(post("/api/articles/create")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidDTO)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Create article without authentication - unauthorized")
        void createArticleWithoutAuth() throws Exception {
                mockMvc.perform(post("/api/articles/create")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validArticleCreateDTO)))
                                .andExpect(status().isUnauthorized());
        }
}