package com.example.question_bank.controller;

import com.example.question_bank.entity.PracticeProgress;
import com.example.question_bank.entity.Question;
import com.example.question_bank.entity.UserAnswer;
import com.example.question_bank.service.PracticeProgressService;
import com.example.question_bank.service.QuestionService;
import com.example.question_bank.service.UserAnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/practice")
@Slf4j
public class PracticeController {

    @Autowired
    private UserAnswerService userAnswerService;
    
    @Autowired
    private PracticeProgressService progressService;
    
    @Autowired
    private QuestionService questionService;

    // 获取练习题目
    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions(
        @RequestParam Long bankId,
        @RequestParam String mode,
        @RequestParam(defaultValue = "10") Integer count
    ) {
        try {
            List<Question> questions = questionService.getPracticeQuestions(bankId, mode, count);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            log.error("获取练习题目失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 提交答案
    @PostMapping("/submit")
    public ResponseEntity<?> submitAnswer(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(String.valueOf(request.get("userId")));
            Long questionId = Long.valueOf(String.valueOf(request.get("questionId")));
            Long bankId = Long.valueOf(String.valueOf(request.get("bankId")));
            String answer = (String) request.get("answer");
            Boolean isCorrect = (Boolean) request.get("isCorrect");
            String mode = (String) request.get("mode");
            Integer practiceTime = (Integer) request.get("practiceTime");

            UserAnswer userAnswer = userAnswerService.submitAnswer(
                userId, questionId, bankId, answer, isCorrect, mode
            );
            
            return ResponseEntity.ok(userAnswer);
        } catch (Exception e) {
            log.error("提交答案失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 获取练习进度
    @GetMapping("/progress")
    public ResponseEntity<?> getProgress(
        @RequestParam Long userId,
        @RequestParam Long bankId
    ) {
        try {
            PracticeProgress progress = progressService.getStatistics(userId, bankId);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            log.error("获取练习进度失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 获取错题列表
    @GetMapping("/wrong")
    public ResponseEntity<?> getWrongQuestions(
        @RequestParam Long userId,
        @RequestParam(required = false) Long bankId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        try {
            return ResponseEntity.ok(userAnswerService.getWrongQuestions(
                userId, 
                PageRequest.of(page, size)
            ));
        } catch (Exception e) {
            log.error("获取错题列表失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 获取练习统计
    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics(@RequestParam Long userId) {
        try {
            return ResponseEntity.ok(userAnswerService.getStatistics(userId));
        } catch (Exception e) {
            log.error("获取练习统计失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
} 