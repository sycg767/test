package com.example.question_bank.controller;

import com.example.question_bank.dto.AnswerSubmitDTO;
import com.example.question_bank.service.UserAnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class PracticeControllerTest {

    @Mock
    private UserAnswerService userAnswerService;

    @InjectMocks
    private PracticeController practiceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void submitAnswer_ShouldReturnSuccess() {
        // 准备测试数据
        AnswerSubmitDTO dto = new AnswerSubmitDTO();
        dto.setQuestionId(1L);
        dto.setBankId(1L);
        dto.setAnswer("A");
        dto.setMode("random");
        dto.setPracticeTime(30);

        // 模拟服务层行为
        when(userAnswerService.submitAnswer(any(AnswerSubmitDTO.class))).thenReturn(true);

        // 执行测试
        ResponseEntity<?> response = practiceController.submitAnswer(dto);

        // 验证结果
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
} 