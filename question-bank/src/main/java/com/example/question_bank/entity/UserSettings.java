package com.example.question_bank.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private Boolean soundEnabled = true;
    private Boolean vibrationEnabled = true;
    private Integer defaultQuestionCount = 10;
    private Boolean dailyReminder = false;
    private String reminderTime = "20:00";
} 