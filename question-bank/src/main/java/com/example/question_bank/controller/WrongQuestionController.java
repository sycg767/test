package com.example.question_bank.controller;

import com.example.question_bank.entity.Question;
import com.example.question_bank.service.UserAnswerService;
import com.example.question_bank.exception.BusinessException;
import com.example.question_bank.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/wrong-questions")
public class WrongQuestionController {

    @Autowired
    private UserAnswerService userAnswerService;

    @GetMapping
    public ResponseEntity<?> getWrongQuestions(
            @RequestParam Long userId,
            Pageable pageable) {
        try {
            List<Question> questions = userAnswerService.getWrongQuestions(userId, pageable);
            return ResponseEntity.ok(questions);
        } catch (BusinessException e) {
            log.error("获取错题失败", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("获取错题失败", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("服务器内部错误"));
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getWrongStatistics(@RequestParam Long userId) {
        try {
            Map<String, Object> stats = userAnswerService.getWrongStatistics(userId);
            return ResponseEntity.ok(stats);
        } catch (BusinessException e) {
            log.error("获取错题统计失败", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("获取错题统计失败", e);
            return ResponseEntity.internalServerError().body(new ErrorResponse("服务器内部错误"));
        }
    }
} 