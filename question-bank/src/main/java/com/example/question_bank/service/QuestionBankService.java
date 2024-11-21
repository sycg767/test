package com.example.question_bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.question_bank.repository.QuestionBankRepository;
import com.example.question_bank.entity.QuestionBank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

@Service
@Slf4j
public class QuestionBankService {
    
    @Autowired
    private QuestionBankRepository questionBankRepository;
    
    public Page<QuestionBank> getQuestionBanks(PageRequest pageRequest, Long categoryId, String keyword) {
        log.debug("Getting question banks with pageRequest: {}, categoryId: {}, keyword: {}", 
                  pageRequest, categoryId, keyword);
        
        if (categoryId != null && keyword != null && !keyword.isEmpty()) {
            return questionBankRepository.findByCategoryIdAndNameContaining(
                categoryId, keyword, pageRequest);
        } else if (categoryId != null) {
            return questionBankRepository.findByCategoryId(categoryId, pageRequest);
        } else if (keyword != null && !keyword.isEmpty()) {
            return questionBankRepository.findByNameContaining(keyword, pageRequest);
        } else {
            return questionBankRepository.findAll(pageRequest);
        }
    }
    
    public Optional<QuestionBank> getQuestionBank(Long id) {
        log.debug("Getting question bank with id: {}", id);
        return questionBankRepository.findById(id);
    }
} 