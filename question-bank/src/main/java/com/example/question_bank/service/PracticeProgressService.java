package com.example.question_bank.service;

import com.example.question_bank.entity.PracticeProgress;
import com.example.question_bank.repository.PracticeProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PracticeProgressService {

    @Autowired
    private PracticeProgressRepository progressRepository;

    // 获取或创建练习进度
    @Transactional
    public PracticeProgress getOrCreateProgress(Long userId, Long bankId) {
        return progressRepository.findByUserIdAndBankId(userId, bankId)
            .orElseGet(() -> {
                PracticeProgress progress = new PracticeProgress();
                progress.getUser().setId(userId);
                progress.getBank().setId(bankId);
                progress.setTotalQuestions(0);
                progress.setFinishedQuestions(0);
                progress.setCorrectCount(0);
                return progressRepository.save(progress);
            });
    }

    // 更新练习进度
    @Transactional
    public PracticeProgress updateProgress(Long userId, Long bankId, boolean isCorrect, Long questionId) {
        PracticeProgress progress = getOrCreateProgress(userId, bankId);
        progress.setFinishedQuestions(progress.getFinishedQuestions() + 1);
        if (isCorrect) {
            progress.setCorrectCount(progress.getCorrectCount() + 1);
        }
        progress.setLastQuestionId(questionId);
        return progressRepository.save(progress);
    }

    // 获取练习统计
    public PracticeProgress getStatistics(Long userId, Long bankId) {
        return progressRepository.findByUserIdAndBankId(userId, bankId)
            .orElseGet(() -> {
                PracticeProgress progress = new PracticeProgress();
                progress.getUser().setId(userId);
                progress.getBank().setId(bankId);
                return progress;
            });
    }
} 