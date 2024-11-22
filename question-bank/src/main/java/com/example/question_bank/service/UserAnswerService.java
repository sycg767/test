package com.example.question_bank.service;

import com.example.question_bank.dto.AnswerSubmitDTO;
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
import java.util.HashMap;
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
    
    public Page<Question> getWrongQuestions(Long userId, Pageable pageable) {
        try {
            return userAnswerRepository.findWrongQuestions(userId, pageable);
        } catch (Exception e) {
            log.error("获取错题失败", e);
            throw new RuntimeException("获取错题失败", e);
        }
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
    
    @Transactional
    public UserAnswer submitAnswer(AnswerSubmitDTO dto) {
        try {
            UserAnswer userAnswer = new UserAnswer();
            User user = new User();
            user.setId(dto.getUserId());
            Question question = new Question();
            question.setId(dto.getQuestionId());
            
            userAnswer.setUser(user);
            userAnswer.setQuestion(question);
            userAnswer.setAnswer(dto.getAnswer());
            userAnswer.setIsCorrect(dto.getIsCorrect());
            userAnswer.setMode(dto.getMode());
            userAnswer.setBankId(dto.getBankId());
            userAnswer.setCreatedAt(LocalDateTime.now());
            userAnswer.setPracticeTime(dto.getPracticeTime());
            userAnswer.setReviewCount(dto.getReviewCount());
            
            progressService.updateProgress(dto.getUserId(), dto.getBankId(), dto.getIsCorrect(), dto.getQuestionId());
            
            return userAnswerRepository.save(userAnswer);
        } catch (Exception e) {
            log.error("提交答案失败", e);
            throw new RuntimeException("提交答案失败", e);
        }
    }
    
    public Map<String, Object> getUserStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 获取总答题数
        Long totalQuestions = userAnswerRepository.countByUserId(userId);
        
        // 获取正确答题数
        Long correctCount = userAnswerRepository.countCorrectAnswers(userId);
        
        // 计算正确率
        double correctRate = totalQuestions > 0 
            ? (double) correctCount / totalQuestions * 100 
            : 0;
        
        // 获取今日答题数
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        Long todayCount = userAnswerRepository.countByUserIdAndCreatedAtAfter(userId, todayStart);
        
        stats.put("totalQuestions", totalQuestions);
        stats.put("correctCount", correctCount);
        stats.put("correctRate", String.format("%.1f", correctRate) + "%");
        stats.put("todayCount", todayCount);
        
        return stats;
    }
    
    public Map<String, Object> getWrongStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 获取错题总数
        Long totalWrong = userAnswerRepository.countWrongAnswers(userId);
        
        // 获取已复习错题数
        Long reviewedCount = userAnswerRepository.countReviewedWrongQuestions(userId);
        
        // 获取已掌握错题数（连续答对3次以上）
        Long masteredCount = userAnswerRepository.countMasteredWrongQuestions(userId);
        
        stats.put("totalCount", totalWrong);
        stats.put("reviewedCount", reviewedCount);
        stats.put("masteredCount", masteredCount);
        stats.put("needReviewCount", totalWrong - masteredCount);
        
        return stats;
    }
} 