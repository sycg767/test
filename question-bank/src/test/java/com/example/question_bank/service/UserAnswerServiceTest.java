package com.example.question_bank.service;

import com.example.question_bank.entity.Question;
import com.example.question_bank.entity.User;
import com.example.question_bank.entity.UserAnswer;
import com.example.question_bank.repository.UserAnswerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
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
        User user = new User();
        user.setId(1L);
        Question question = new Question();
        question.setId(1L);
        answer.setUser(user);
        answer.setQuestion(question);
        
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
        assertEquals(1L, result.getUser().getId());
        assertEquals(1L, result.getQuestion().getId());
    }

    @Test
    void getWrongQuestions_ShouldReturnListOfWrongQuestions() {
        // Arrange
        List<Question> expectedQuestions = new ArrayList<>();
        Question question = new Question();
        question.setId(1L);
        expectedQuestions.add(question);
        
        when(userAnswerRepository.findWrongQuestions(any(Long.class), any(PageRequest.class)))
            .thenReturn(expectedQuestions);

        // Act
        List<Question> result = userAnswerService.getWrongQuestions(1L, PageRequest.of(0, 10));

        // Assert
        assertNotNull(result);
        assertEquals(expectedQuestions, result);
        assertEquals(1, result.size());
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