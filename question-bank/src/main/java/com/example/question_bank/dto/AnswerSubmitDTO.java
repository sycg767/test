package com.example.question_bank.dto;

import lombok.Data;

/**
 * 答题提交数据传输对象
 */
@Data
public class AnswerSubmitDTO {
    private Long userId;        // 用户ID
    private Long questionId;    // 题目ID
    private Long bankId;        // 题库ID
    private String answer;      // 用户答案
    private String mode;        // 练习模式
    private Integer practiceTime;  // 做题用时(秒)
    private Integer reviewCount;   // 复习次数
    private Boolean isCorrect;     // 是否正确
} 