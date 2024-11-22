package com.example.question_bank.controller;

import com.example.question_bank.entity.Question;
import com.example.question_bank.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    
    @GetMapping("/batch")
    public ResponseEntity<List<Question>> getQuestionsByIds(@RequestParam List<Long> ids) {
        try {
            List<Question> questions = questionService.getQuestionsByIds(ids);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
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