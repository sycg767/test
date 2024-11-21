package com.example.question_bank.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String avatarUrl;
    private Integer gender;
    private String token;  // 可选，如果使用token认证
} 