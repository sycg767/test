package com.example.question_bank.repository;

import com.example.question_bank.entity.Question;
import com.example.question_bank.entity.UserAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    
    Page<UserAnswer> findByUserIdAndIsCorrect(Long userId, Boolean isCorrect, Pageable pageable);
    
    List<UserAnswer> findByUserIdAndBankId(Long userId, Long bankId, Pageable pageable);
    
    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.user.id = :userId AND ua.isCorrect = true")
    Long countCorrectAnswers(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.user.id = :userId AND ua.isCorrect = false")
    Long countWrongAnswers(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.user.id = :userId AND ua.bankId = :bankId")
    Long countByUserIdAndBankId(@Param("userId") Long userId, @Param("bankId") Long bankId);
    
    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.user.id = :userId AND ua.bankId = :bankId AND ua.isCorrect = true")
    Long countCorrectByUserIdAndBankId(@Param("userId") Long userId, @Param("bankId") Long bankId);
    
    List<UserAnswer> findByUserIdAndLastReviewAtBefore(Long userId, LocalDateTime threshold, Pageable pageable);
    
    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.user.id = :userId AND ua.bankId = :bankId AND ua.createdAt >= :startTime")
    Long countTodayAnswers(@Param("userId") Long userId, @Param("bankId") Long bankId, @Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.user.id = :userId AND ua.bankId = :bankId AND ua.isCorrect = true AND ua.createdAt >= :startTime")
    Long countTodayCorrectAnswers(@Param("userId") Long userId, @Param("bankId") Long bankId, @Param("startTime") LocalDateTime startTime);
    
    Long countByUserId(Long userId);
    
    Long countByUserIdAndCreatedAtAfter(Long userId, LocalDateTime dateTime);
    
    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.user.id = :userId AND ua.reviewCount > 0")
    Long countReviewedWrongQuestions(@Param("userId") Long userId);
    
    @Query("""
        SELECT COUNT(ua) FROM UserAnswer ua 
        WHERE ua.user.id = :userId 
        AND ua.isCorrect = false 
        AND EXISTS (
            SELECT 1 FROM UserAnswer ua2 
            WHERE ua2.question.id = ua.question.id 
            AND ua2.user.id = ua.user.id 
            AND ua2.isCorrect = true 
            GROUP BY ua2.question.id 
            HAVING COUNT(ua2) >= 3
        )
    """)
    Long countMasteredWrongQuestions(@Param("userId") Long userId);

    @Query("""
        SELECT DISTINCT q FROM Question q 
        JOIN UserAnswer ua ON q.id = ua.question.id 
        WHERE ua.user.id = :userId AND ua.isCorrect = false
    """)
    List<Question> findWrongQuestions(@Param("userId") Long userId, Pageable pageable);
} 