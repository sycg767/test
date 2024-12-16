package com.example.question_bank.controller;

import com.example.question_bank.entity.Question;
import com.example.question_bank.entity.UserAnswer;
import com.example.question_bank.service.QuestionService;
import com.example.question_bank.service.UserAnswerService;
import com.example.question_bank.exception.BusinessException;
import com.example.question_bank.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user-answers")
@Slf4j
public class UserAnswerController {

    @Autowired
    private UserAnswerService userAnswerService;

    @Autowired
    private QuestionService questionService;

    // 获取用户答题统计
    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics(@RequestParam Long userId) {
        try {
            Map<String, Long> stats = userAnswerService.getStatistics(userId);
            return ResponseEntity.ok(stats);
        } catch (BusinessException e) {
            log.error("获取用户答题统计失败", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("获取用户答题统计失败", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("服务器内部错误"));
        }
    }

    // 获取错题列表
    @GetMapping("/wrong")
    public ResponseEntity<?> getWrongQuestions(
        @RequestParam Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        try {
            List<Question> wrongQuestions = questionService.getWrongQuestions(
                userId, 
                PageRequest.of(page, size)
            );
            return ResponseEntity.ok(wrongQuestions);
        } catch (BusinessException e) {
            log.error("获取错题列表失败", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("获取错题列表失败", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("服务器内部错误"));
        }
    }

    // 获取题库练习记录
    @GetMapping("/bank-records")
    public ResponseEntity<?> getBankRecords(
        @RequestParam Long userId,
        @RequestParam Long bankId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        try {
            List<UserAnswer> records = userAnswerService.getBankRecords(
                userId,
                bankId,
                PageRequest.of(page, size)
            );
            return ResponseEntity.ok(records);
        } catch (BusinessException e) {
            log.error("获取题库练习记录失败", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("获取题库练习记录失败", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("服务器内部错误"));
        }
    }

    // 获取需要复习的题目
    @GetMapping("/review")
    public ResponseEntity<?> getReviewQuestions(
        @RequestParam Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        try {
            List<UserAnswer> reviewQuestions = userAnswerService.getReviewQuestions(
                userId,
                PageRequest.of(page, size)
            );
            return ResponseEntity.ok(reviewQuestions);
        } catch (BusinessException e) {
            log.error("获取复习题目失败", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("获取复习题目失败", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("服务器内部错误"));
        }
    }

    // 更新复习记录
    @PostMapping("/{answerId}/review")
    public ResponseEntity<?> updateReviewRecord(@PathVariable Long answerId) {
        try {
            UserAnswer answer = userAnswerService.updateReviewRecord(answerId);
            return ResponseEntity.ok(answer);
        } catch (BusinessException e) {
            log.error("更新复习记录失败", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("更新复习记录失败", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("服务器内部错误"));
        }
    }
} 