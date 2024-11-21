package com.example.questionbank.controller;

import com.example.questionbank.entity.UserSettings;
import com.example.questionbank.entity.ReminderSettings;
import com.example.questionbank.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserSettingsController {
    @Autowired
    private UserSettingsService settingsService;
    
    @GetMapping("/settings")
    public ResponseEntity<UserSettings> getSettings(
            @RequestAttribute Long userId) {
        return ResponseEntity.ok(settingsService.getSettings(userId));
    }
    
    @PutMapping("/settings")
    public ResponseEntity<UserSettings> updateSettings(
            @RequestAttribute Long userId,
            @RequestBody UserSettings settings) {
        settings.setUserId(userId);
        return ResponseEntity.ok(settingsService.updateSettings(settings));
    }
    
    @PutMapping("/reminder")
    public ResponseEntity<Void> updateReminder(
            @RequestAttribute Long userId,
            @RequestBody ReminderSettings settings) {
        settingsService.updateReminder(userId, settings);
        return ResponseEntity.ok().build();
    }
} 