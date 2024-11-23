package com.example.question_bank.service;

import com.example.question_bank.entity.Question;
import com.example.question_bank.entity.UserAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    // 基本的CRUD操作
    Question getQuestion(Long id);
    Page<Question> getQuestions(Long bankId, String keyword, Long categoryId, Pageable pageable);
    
    // 练习相关的方法
    List<Question> getSequentialQuestions(Long bankId, int count);
    List<Question> getRandomQuestions(Long bankId, int count);
    List<Question> getChapterQuestions(Long bankId, int count);
    List<Question> getPracticeQuestions(Long bankId, String mode, Integer count);
    
    // 答题相关的方法
    UserAnswer submitAnswer(UserAnswer answer);
    List<Question> getWrongQuestions(Long userId, Pageable pageable);
    List<Question> getCollectedQuestions(Long userId, Pageable pageable);
    
    // 添加根据ID列表获取题目的方法
    List<Question> getQuestionsByIds(List<Long> ids);
} 