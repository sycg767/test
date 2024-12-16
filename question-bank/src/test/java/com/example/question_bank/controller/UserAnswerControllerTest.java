package com.example.question_bank.controller;

import com.example.question_bank.entity.Question;
import com.example.question_bank.entity.UserAnswer;
import com.example.question_bank.service.QuestionService;
import com.example.question_bank.service.UserAnswerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserAnswerController.class)
public class UserAnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAnswerService userAnswerService;

    @MockBean
    private QuestionService questionService;

    @Test
    void submitAnswer_ShouldReturnCreatedAnswer() throws Exception {
        // Arrange
        UserAnswer answer = new UserAnswer();
        when(userAnswerService.submitAnswer(
            any(Long.class),  // userId
            any(Long.class),  // questionId
            any(Long.class),  // bankId
            any(String.class),  // answer
            any(Boolean.class),  // isCorrect
            any(String.class)   // mode
        )).thenReturn(answer);

        // Act & Assert
        mockMvc.perform(post("/api/v1/user-answers")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"userId\":1,\"questionId\":1,\"bankId\":1,\"answer\":\"A\",\"isCorrect\":true,\"mode\":\"practice\"}"))
            .andExpect(status().isOk());
    }

    @Test
    void getWrongQuestions_ShouldReturnListOfQuestions() throws Exception {
        // Arrange
        List<Question> questions = new ArrayList<>();
        when(questionService.getWrongQuestions(any(Long.class), any(PageRequest.class)))
            .thenReturn(questions);

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-answers/wrong")
            .param("userId", "1")
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk());
    }

    @Test
    void getStatistics_ShouldReturnUserStatistics() throws Exception {
        // Arrange
        Map<String, Long> stats = Map.of(
            "totalCount", 10L,
            "correctCount", 8L,
            "wrongCount", 2L
        );
        when(userAnswerService.getStatistics(any(Long.class)))
            .thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/api/v1/user-answers/statistics")
            .param("userId", "1"))
            .andExpect(status().isOk());
    }
} 