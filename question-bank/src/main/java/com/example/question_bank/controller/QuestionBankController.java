package com.example.question_bank.controller;

import com.example.question_bank.entity.QuestionBank;
import com.example.question_bank.service.QuestionBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/banks")
@Slf4j
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;

    @GetMapping
    public ResponseEntity<Page<QuestionBank>> getQuestionBanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        log.debug("Getting question banks with page: {}, size: {}, categoryId: {}, keyword: {}", 
                  page, size, categoryId, keyword);
        try {
            Page<QuestionBank> banks = questionBankService.getQuestionBanks(
                PageRequest.of(page, size), categoryId, keyword);
            return ResponseEntity.ok(banks);
        } catch (Exception e) {
            log.error("Failed to get question banks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionBank> getQuestionBank(@PathVariable Long id) {
        log.debug("Getting question bank with id: {}", id);
        try {
            return questionBankService.getQuestionBank(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Failed to get question bank with id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 