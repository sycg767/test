package com.example.question_bank.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuestionBankDTO {
    private Long id;
    private String name;
    private String description;
    private Integer questionCount;
    private Integer practiceCount;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 