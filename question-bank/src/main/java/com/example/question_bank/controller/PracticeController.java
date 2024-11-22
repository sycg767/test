package com.example.question_bank.controller;

import com.example.question_bank.dto.AnswerSubmitDTO;
import com.example.question_bank.entity.Question;
import com.example.question_bank.entity.UserAnswer;
import com.example.question_bank.service.UserAnswerService;
import com.example.question_bank.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/practice")
public class PracticeController {

    @Autowired
    private UserAnswerService userAnswerService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getPracticeQuestions(
            @RequestParam Long bankId,
            @RequestParam String mode,
            @RequestParam(defaultValue = "10") Integer count) {
        try {
            List<Question> questions = questionService.getPracticeQuestions(bankId, mode, count);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitAnswer(@RequestBody AnswerSubmitDTO dto) {
        try {
            UserAnswer userAnswer = userAnswerService.submitAnswer(dto);
            return ResponseEntity.ok(userAnswer);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 