package com.example.question_bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WrongQuestionDTO {
    private Long id;
    private String content;
    private String type;
    private Integer reviewCount;
    private Long wrongCount;
    private String status;
    private LocalDateTime createdAt;
    private Long categoryId;
    private String categoryName;
} 