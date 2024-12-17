package com.example.question_bank.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "study_records")  // 更新表名
public class StudyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "bank_id")
    private Long bankId;

    @Column(nullable = false)
    private String mode;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name = "correct_count", nullable = false)
    private Integer correctCount;

    @Column(name = "spend_time", nullable = false)
    private Integer spendTime;

    @Column(name = "correct_rate", nullable = false)
    private Double correctRate;

    @Column(name = "create_time", nullable = false)
    private Long createTime;
} 