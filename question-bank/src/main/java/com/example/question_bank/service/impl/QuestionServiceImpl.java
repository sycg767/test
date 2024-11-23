package com.example.question_bank.service.impl;

import com.example.question_bank.entity.Question;
import com.example.question_bank.entity.UserAnswer;
import com.example.question_bank.repository.QuestionRepository;
import com.example.question_bank.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Question getQuestion(Long id) {
        return questionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("题目不存在"));
    }

    @Override
    public Page<Question> getQuestions(Long bankId, String keyword, Long categoryId, Pageable pageable) {
        if (categoryId != null) {
            return questionRepository.findByBankIdAndCategoryId(bankId, categoryId, pageable);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            return questionRepository.findByBankIdAndContentContaining(bankId, keyword, pageable);
        } else {
            return questionRepository.findByBankId(bankId, pageable);
        }
    }

    @Override
    public List<Question> getSequentialQuestions(Long bankId, int count) {
        return questionRepository.findByBankIdOrderById(bankId)
            .stream()
            .limit(count)
            .toList();
    }

    @Override
    public List<Question> getRandomQuestions(Long bankId, int count) {
        return questionRepository.findRandomQuestions(bankId, count);
    }

    @Override
    public List<Question> getChapterQuestions(Long bankId, int count) {
        return questionRepository.findByBankIdOrderByChapter(bankId)
            .stream()
            .limit(count)
            .toList();
    }

    @Override
    public UserAnswer submitAnswer(UserAnswer answer) {
        // 这里应该调用 UserAnswerService 来处理答案提交
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Question> getWrongQuestions(Long userId, Pageable pageable) {
        return questionRepository.findWrongQuestions(userId, pageable).getContent();
    }

    @Override
    public List<Question> getCollectedQuestions(Long userId, Pageable pageable) {
        return questionRepository.findCollectedQuestions(userId, pageable).getContent();
    }

    // 新增方法，用于处理练习题目获取
    public List<Question> getPracticeQuestions(Long bankId, String mode, Integer count) {
        switch (mode) {
            case "sequence":
                return getSequentialQuestions(bankId, count);
            case "random":
                return getRandomQuestions(bankId, count);
            case "chapter":
                return getChapterQuestions(bankId, count);
            default:
                throw new IllegalArgumentException("不支持的练习模式: " + mode);
        }
    }

    @Override
    public List<Question> getQuestionsByIds(List<Long> ids) {
        return questionRepository.findAllById(ids);
    }
} 