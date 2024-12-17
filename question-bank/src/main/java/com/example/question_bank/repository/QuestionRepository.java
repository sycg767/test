package com.example.question_bank.repository;

import com.example.question_bank.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // 基本查询
    Page<Question> findByBankId(Long bankId, Pageable pageable);
    Page<Question> findByBankIdAndCategoryId(Long bankId, Long categoryId, Pageable pageable);
    Page<Question> findByBankIdAndContentContaining(Long bankId, String keyword, Pageable pageable);
    
    // 练习相关查询
    List<Question> findByBankIdOrderById(Long bankId);
    
    @Query(value = "SELECT * FROM questions WHERE bank_id = :bankId ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Question> findRandomQuestions(@Param("bankId") Long bankId, @Param("count") int count);
    
    // 按章节顺序查���
    List<Question> findByBankIdOrderByChapterAscIdAsc(Long bankId);
    
    // 错题和收藏查询
    @Query("SELECT q FROM Question q JOIN UserAnswer ua ON q.id = ua.question.id " +
           "WHERE ua.user.id = :userId AND ua.isCorrect = false")
    List<Question> findWrongQuestions(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT q FROM Question q JOIN UserCollection uc ON q.id = uc.question.id " +
           "WHERE uc.user.id = :userId")
    List<Question> findCollectedQuestions(@Param("userId") Long userId, Pageable pageable);
    
    @Query(value = "SELECT * FROM questions WHERE bank_id = :bankId ORDER BY id LIMIT :count", nativeQuery = true)
    List<Question> findByBankIdOrderById(@Param("bankId") Long bankId, @Param("count") Integer count);
    
    @Query(value = "SELECT * FROM questions WHERE bank_id = :bankId ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Question> findRandomQuestionsByBankId(@Param("bankId") Long bankId, @Param("count") Integer count);
    
    @Query(value = """
        SELECT q.* FROM questions q 
        JOIN user_answers ua ON q.id = ua.question_id 
        WHERE q.bank_id = :bankId AND ua.is_correct = false 
        GROUP BY q.id 
        LIMIT :count
        """, nativeQuery = true)
    List<Question> findWrongQuestionsByBankId(@Param("bankId") Long bankId, @Param("count") Integer count);
    
    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.options WHERE q.bankId = :bankId")
    List<Question> findByBankIdWithOptions(@Param("bankId") Long bankId);
    
    @Query("SELECT DISTINCT q FROM Question q LEFT JOIN FETCH q.options " +
           "WHERE q.id IN (SELECT ua.question.id FROM UserAnswer ua " +
           "WHERE ua.user.id = :userId AND ua.isCorrect = false) " +
           "ORDER BY q.id")
    List<Question> findWrongQuestionsWithOptions(@Param("userId") Long userId, Pageable pageable);
} 