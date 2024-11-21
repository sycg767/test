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
    
    @Column(name = "bank_id")
    private Long bankId;
    
    private String answer;
    
    @Column(name = "is_correct")
    private Boolean isCorrect;
    
    private String mode;
    
    @Column(name = "practice_time")
    private Integer practiceTime;
    
    @Column(name = "review_count")
    private Integer reviewCount = 0;
    
    @Column(name = "last_review_at")
    private LocalDateTime lastReviewAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (reviewCount == null) {
            reviewCount = 0;
        }
    }
} 