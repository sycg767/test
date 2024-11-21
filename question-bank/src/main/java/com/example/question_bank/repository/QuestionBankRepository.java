package com.example.question_bank.repository;

import com.example.question_bank.entity.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {
    
    Page<QuestionBank> findByCategoryId(Long categoryId, Pageable pageable);
    
    Page<QuestionBank> findByNameContaining(String keyword, Pageable pageable);
    
    Page<QuestionBank> findByCategoryIdAndNameContaining(Long categoryId, String keyword, Pageable pageable);
} 