package com.example.question_bank.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class LoginResponse {
    private String token;
    private AdminUserDTO user;
} 