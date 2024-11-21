package com.example.question_bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.question_bank.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
} 