package com.example.question_bank.controller;

import com.example.question_bank.entity.QuestionBank;
import com.example.question_bank.service.QuestionBankService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(QuestionBankController.class)
class QuestionBankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionBankService questionBankService;

    @Test
    void getQuestionBanks_ShouldReturnBanksList() throws Exception {
        // Arrange
        QuestionBank bank = new QuestionBank();
        bank.setId(1L);
        bank.setName("Test Bank");
        Page<QuestionBank> page = new PageImpl<>(List.of(bank));
        
        when(questionBankService.getQuestionBanks(any(), any(), any()))
            .thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/banks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(1))
            .andExpect(jsonPath("$.content[0].name").value("Test Bank"));
    }
} 