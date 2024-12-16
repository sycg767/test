package com.example.question_bank.repository;

import com.example.question_bank.entity.UserCollection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCollectionRepository extends JpaRepository<UserCollection, Long> {
    boolean existsByUserIdAndQuestionId(Long userId, Long questionId);
    
    void deleteByUserIdAndQuestionId(Long userId, Long questionId);
    
    List<UserCollection> findByUserId(Long userId, Pageable pageable);
} 