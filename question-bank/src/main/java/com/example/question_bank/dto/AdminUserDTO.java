package com.example.question_bank.dto;

import lombok.Data;

@Data
public class AdminUserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String roles;
    private String avatar;
    private String email;
    private String phone;
    private String status;
} 