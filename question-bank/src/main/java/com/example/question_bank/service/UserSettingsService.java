package com.example.question_bank.service;

import com.example.question_bank.entity.User;
import com.example.question_bank.entity.UserSettings;
import com.example.question_bank.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {
    
    @Autowired
    private UserSettingsRepository userSettingsRepository;
    
    public UserSettings getSettings(Long userId) {
        return userSettingsRepository.findByUserId(userId)
            .orElseGet(() -> {
                UserSettings settings = new UserSettings();
                User user = new User();
                user.setId(userId);
                settings.setUser(user);
                return userSettingsRepository.save(settings);
            });
    }
    
    public UserSettings updateSettings(Long userId, UserSettings settings) {
        UserSettings existingSettings = getSettings(userId);
        existingSettings.setSoundEnabled(settings.getSoundEnabled());
        existingSettings.setVibrationEnabled(settings.getVibrationEnabled());
        existingSettings.setDefaultQuestionCount(settings.getDefaultQuestionCount());
        existingSettings.setDailyReminder(settings.getDailyReminder());
        existingSettings.setReminderTime(settings.getReminderTime());
        return userSettingsRepository.save(existingSettings);
    }
} 