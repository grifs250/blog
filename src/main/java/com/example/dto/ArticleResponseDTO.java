package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleResponseDTO {
    private Long id;
    private String title;
    private String author;
    private String content;
    private LocalDateTime publicationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}