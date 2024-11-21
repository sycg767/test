package com.example.question_bank.repository;

import com.example.question_bank.entity.PracticeProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PracticeProgressRepository extends JpaRepository<PracticeProgress, Long> {
    Optional<PracticeProgress> findByUserIdAndBankId(Long userId, Long bankId);
} 