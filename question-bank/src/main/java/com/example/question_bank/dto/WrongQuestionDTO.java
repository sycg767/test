package com.example.question_bank.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WrongQuestionDTO {
    private Long id;
    private String content;
    private String type;
    private String answer;
    private String analysis;
    private Integer reviewCount;
    private Boolean mastered;
    private LocalDateTime lastReviewTime;
} 