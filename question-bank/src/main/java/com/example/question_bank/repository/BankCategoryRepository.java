package com.example.question_bank.repository;

import com.example.question_bank.entity.BankCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCategoryRepository extends JpaRepository<BankCategory, Long> {
} 