package com.example.question_bank.service;

import com.example.question_bank.entity.QuestionBank;
import com.example.question_bank.repository.QuestionBankRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionBankServiceTest {

    @Autowired
    private QuestionBankService questionBankService;

    @MockBean
    private QuestionBankRepository questionBankRepository;

    @Test
    void getQuestionBanks_ShouldReturnPageOfBanks() {
        // Arrange
        QuestionBank bank = new QuestionBank();
        bank.setId(1L);
        bank.setName("Test Bank");
        Page<QuestionBank> expectedPage = new PageImpl<>(List.of(bank));
        
        when(questionBankRepository.findAll(any(Pageable.class)))
            .thenReturn(expectedPage);

        // Act
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<QuestionBank> result = questionBankService.getQuestionBanks(pageRequest, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Bank", result.getContent().get(0).getName());
    }

    @Test
    void getQuestionBank_ShouldReturnOptionalBank() {
        // Arrange
        QuestionBank bank = new QuestionBank();
        bank.setId(1L);
        bank.setName("Test Bank");
        
        when(questionBankRepository.findById(1L))
            .thenReturn(Optional.of(bank));

        // Act
        Optional<QuestionBank> result = questionBankService.getQuestionBank(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Bank", result.get().getName());
    }
} 