package com.example.question_bank.controller;

import com.example.question_bank.entity.Question;
import com.example.question_bank.service.QuestionService;
import com.example.question_bank.exception.BusinessException;
import com.example.question_bank.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    
    @GetMapping("/batch")
    public ResponseEntity<?> getQuestionsByIds(@RequestParam List<Long> ids) {
        try {
            List<Question> questions = questionService.getQuestionsByIds(ids);
            return ResponseEntity.ok(questions);
        } catch (BusinessException e) {
            log.error("获取题目失败", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("获取题目失败", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("服务器内部错误"));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable Long id) {
        try {
            Question question = questionService.getQuestion(id);
            return ResponseEntity.ok(question);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 