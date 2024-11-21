package com.example.question_bank.controller;

import com.example.question_bank.entity.Question;
import com.example.question_bank.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    
    @GetMapping
    public Page<Question> getQuestions(
        @RequestParam Long bankId,
        @RequestParam(required = false) String mode,
        @RequestParam(required = false) Long pointId,
        Pageable pageable
    ) {
        return questionService.getQuestions(bankId, mode, pointId, pageable);
    }
    
    @GetMapping("/{id}")
    public Question getQuestion(@PathVariable Long id) {
        return questionService.getQuestion(id);
    }
} 