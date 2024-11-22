package com.example.question_bank.controller;

import com.example.question_bank.entity.Question;
import com.example.question_bank.service.UserAnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/wrong-questions")
public class WrongQuestionController {

    @Autowired
    private UserAnswerService userAnswerService;

    @GetMapping
    public ResponseEntity<Page<Question>> getWrongQuestions(
            @RequestParam Long userId,
            Pageable pageable) {
        try {
            Page<Question> questions = userAnswerService.getWrongQuestions(userId, pageable);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            log.error("获取错题失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getWrongStatistics(@RequestParam Long userId) {
        try {
            Map<String, Object> stats = userAnswerService.getWrongStatistics(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取错题统计失败", e);
            return ResponseEntity.badRequest().build();
        }
    }
} 