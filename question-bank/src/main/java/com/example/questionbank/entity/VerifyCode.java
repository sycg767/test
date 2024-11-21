package com.example.questionbank.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "verify_codes")
public class VerifyCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String phone;
    private String code;
    private LocalDateTime expireTime;
    private Boolean used = false;
    
    @Column(updatable = false)
    private LocalDateTime createTime = LocalDateTime.now();
} 