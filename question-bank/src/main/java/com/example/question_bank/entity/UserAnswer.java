package com.example.question_bank.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_answers")
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    
    private Long bankId;
    private String answer;
    private Boolean isCorrect;
    private String mode;
    private Integer practiceTime;
    private Integer reviewCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastReviewAt;
} 