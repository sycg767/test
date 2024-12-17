package com.example.question_bank.controller;

import com.example.question_bank.entity.User;
import com.example.question_bank.service.UserService;
import com.example.question_bank.service.UserAnswerService;
import com.example.question_bank.dto.LoginRequest;
import com.example.question_bank.dto.UserDTO;
import com.example.question_bank.dto.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserAnswerService userAnswerService;
    
    @PostMapping("/account/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            UserDTO userDTO = userService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("登录失败：" + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String nickname = request.get("nickname");

            if (username == null || password == null) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("用户名和密码不能为空"));
            }

            UserDTO userDTO = userService.register(username, password, nickname);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", userDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{userId}/stats")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Map<String, Object>> getUserStats(@PathVariable Long userId) {
        try {
            Map<String, Object> stats = userAnswerService.getUserStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取用户统计失败", e);
            return ResponseEntity.badRequest().build();
        }
    }
} 