package com.example.question_bank.service;

import com.example.question_bank.entity.BankCategory;
import com.example.question_bank.repository.BankCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankCategoryService {
    @Autowired
    private BankCategoryRepository bankCategoryRepository;
    
    public List<BankCategory> getAllCategories() {
        return bankCategoryRepository.findAll();
    }
} 