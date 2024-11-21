package com.example.question_bank.controller;

import com.example.question_bank.entity.BankCategory;
import com.example.question_bank.service.BankCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bank-categories")
public class BankCategoryController {
    @Autowired
    private BankCategoryService bankCategoryService;
    
    @GetMapping
    public List<BankCategory> getAllCategories() {
        return bankCategoryService.getAllCategories();
    }
} 