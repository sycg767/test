package com.example.question_bank.service;

import com.example.question_bank.entity.Question;
import com.example.question_bank.entity.User;
import com.example.question_bank.entity.UserAnswer;
import com.example.question_bank.repository.UserAnswerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
public class UserAnswerService {
    @Autowired
    private UserAnswerRepository userAnswerRepository;
    
    @Autowired
    private PracticeProgressService progressService;
    
    @Transactional
    public UserAnswer submitAnswer(Long userId, Long questionId, Long bankId, String answer, Boolean isCorrect, String mode) {
        UserAnswer userAnswer = new UserAnswer();
        User user = new User();
        user.setId(userId);
        Question question = new Question();
        question.setId(questionId);
        
        userAnswer.setUser(user);
        userAnswer.setQuestion(question);
        userAnswer.setAnswer(answer);
        userAnswer.setIsCorrect(isCorrect);
        userAnswer.setMode(mode);
        userAnswer.setBankId(bankId);
        userAnswer.setCreatedAt(LocalDateTime.now());
        userAnswer.setPracticeTime(0);
        userAnswer.setReviewCount(0);
        
        progressService.updateProgress(userId, bankId, isCorrect, questionId);
        
        return userAnswerRepository.save(userAnswer);
    }
    
    public Page<UserAnswer> getWrongQuestions(Long userId, Pageable pageable) {
        return userAnswerRepository.findByUserIdAndIsCorrect(userId, false, pageable);
    }
    
    public Map<String, Long> getStatistics(Long userId) {
        Long correctCount = userAnswerRepository.countCorrectAnswers(userId);
        Long wrongCount = userAnswerRepository.countWrongAnswers(userId);
        return Map.of(
            "correctCount", correctCount,
            "wrongCount", wrongCount,
            "totalCount", correctCount + wrongCount
        );
    }
    
    // 获取题库练习记录
    public Page<UserAnswer> getBankRecords(Long userId, Long bankId, Pageable pageable) {
        return userAnswerRepository.findByUserIdAndBankId(userId, bankId, pageable);
    }
    
    // 获取题库练习统计
    public Map<String, Long> getBankStatistics(Long userId, Long bankId) {
        Long totalCount = userAnswerRepository.countByUserIdAndBankId(userId, bankId);
        Long correctCount = userAnswerRepository.countCorrectByUserIdAndBankId(userId, bankId);
        return Map.of(
            "totalCount", totalCount,
            "correctCount", correctCount,
            "wrongCount", totalCount - correctCount
        );
    }
    
    // 更新复习记录
    @Transactional
    public UserAnswer updateReviewRecord(Long answerId) {
        UserAnswer answer = userAnswerRepository.findById(answerId)
            .orElseThrow(() -> new RuntimeException("答题记录不存在"));
        
        answer.setReviewCount(answer.getReviewCount() + 1);
        answer.setLastReviewAt(LocalDateTime.now());
        
        return userAnswerRepository.save(answer);
    }
    
    // 获取需要复习的题目
    public Page<UserAnswer> getReviewQuestions(Long userId, Pageable pageable) {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        return userAnswerRepository.findByUserIdAndLastReviewAtBefore(userId, threshold, pageable);
    }
} 