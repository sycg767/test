package com.example.question_bank.controller;

import com.example.question_bank.entity.UserSettings;
import com.example.question_bank.service.UserSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserSettingsController {
    
    @Autowired
    private UserSettingsService userSettingsService;
    
    @GetMapping("/settings")
    public ResponseEntity<UserSettings> getSettings(@RequestParam Long userId) {
        try {
            UserSettings settings = userSettingsService.getSettings(userId);
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            log.error("获取用户设置失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/settings")
    public ResponseEntity<UserSettings> updateSettings(
            @RequestParam Long userId,
            @RequestBody UserSettings settings) {
        try {
            UserSettings updatedSettings = userSettingsService.updateSettings(userId, settings);
            return ResponseEntity.ok(updatedSettings);
        } catch (Exception e) {
            log.error("更新用户设置失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 