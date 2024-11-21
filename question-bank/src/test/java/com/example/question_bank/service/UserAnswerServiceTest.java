package com.example.question_bank.service;

import com.example.question_bank.entity.UserAnswer;
import com.example.question_bank.repository.UserAnswerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserAnswerServiceTest {

    @Autowired
    private UserAnswerService userAnswerService;

    @MockBean
    private UserAnswerRepository userAnswerRepository;

    @MockBean
    private PracticeProgressService progressService;

    @Test
    void submitAnswer_ShouldReturnSavedAnswer() {
        // Arrange
        UserAnswer answer = new UserAnswer();
        when(userAnswerRepository.save(any(UserAnswer.class)))
            .thenReturn(answer);

        // Act
        UserAnswer result = userAnswerService.submitAnswer(
            1L,    // userId
            1L,    // questionId
            1L,    // bankId
            "A",   // answer
            true,  // isCorrect
            "practice"  // mode
        );

        // Assert
        assertNotNull(result);
    }

    @Test
    void getWrongQuestions_ShouldReturnPageOfWrongQuestions() {
        // Arrange
        Page<UserAnswer> expectedPage = new PageImpl<>(Collections.emptyList());
        when(userAnswerRepository.findByUserIdAndIsCorrect(any(Long.class), any(Boolean.class), any(PageRequest.class)))
            .thenReturn(expectedPage);

        // Act
        Page<UserAnswer> result = userAnswerService.getWrongQuestions(1L, PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
    }

    @Test
    void getStatistics_ShouldReturnCorrectStats() {
        // Arrange
        when(userAnswerRepository.countCorrectAnswers(any(Long.class))).thenReturn(8L);
        when(userAnswerRepository.countWrongAnswers(any(Long.class))).thenReturn(2L);

        // Act
        Map<String, Long> stats = userAnswerService.getStatistics(1L);

        // Assert
        assertEquals(10L, stats.get("totalCount"));
        assertEquals(8L, stats.get("correctCount"));
        assertEquals(2L, stats.get("wrongCount"));
    }
} 