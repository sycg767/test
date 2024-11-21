package com.example.questionbank.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    private Boolean soundEnabled = true;
    private Boolean vibrationEnabled = true;
    private Integer defaultQuestionCount = 10;
    private Boolean dailyReminder = false;
    private String reminderTime = "20:00";
} 