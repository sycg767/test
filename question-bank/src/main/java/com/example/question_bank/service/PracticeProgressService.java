package com.example.question_bank.service;

import com.example.question_bank.entity.PracticeProgress;
import com.example.question_bank.entity.User;
import com.example.question_bank.repository.PracticeProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PracticeProgressService {

    @Autowired
    private PracticeProgressRepository progressRepository;

    public PracticeProgress getOrCreateProgress(Long userId, Long bankId) {
        return progressRepository.findByUserIdAndBankId(userId, bankId)
            .orElseGet(() -> {
                PracticeProgress progress = new PracticeProgress();
                User user = new User();
                user.setId(userId);
                progress.setUser(user);
                progress.setBankId(bankId);
                progress.setTotalQuestions(0);
                progress.setFinishedQuestions(0);
                progress.setCorrectCount(0);
                return progressRepository.save(progress);
            });
    }

    @Transactional
    public void updateProgress(Long userId, Long bankId, Boolean isCorrect, Long questionId) {
        PracticeProgress progress = getOrCreateProgress(userId, bankId);
        progress.setFinishedQuestions(progress.getFinishedQuestions() + 1);
        if (isCorrect) {
            progress.setCorrectCount(progress.getCorrectCount() + 1);
        }
        progress.setLastQuestionId(questionId);
        progressRepository.save(progress);
    }

    // 获取练习统计
    public PracticeProgress getStatistics(Long userId, Long bankId) {
        return progressRepository.findByUserIdAndBankId(userId, bankId)
            .orElseGet(() -> {
                PracticeProgress progress = new PracticeProgress();
                User user = new User();
                user.setId(userId);
                progress.setUser(user);
                progress.setBankId(bankId);
                return progress;
            });
    }
} 