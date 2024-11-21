package com.example.questionbank.service;

import com.example.questionbank.entity.UserSettings;
import com.example.questionbank.entity.ReminderSettings;
import com.example.questionbank.repository.UserSettingsRepository;
import com.example.questionbank.exception.SettingsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSettingsService {
    
    @Autowired
    private UserSettingsRepository settingsRepository;
    
    @Transactional(readOnly = true)
    public UserSettings getSettings(Long userId) {
        try {
            return settingsRepository.findByUserId(userId)
                    .orElseGet(() -> {
                        UserSettings settings = new UserSettings();
                        settings.setUserId(userId);
                        return settingsRepository.save(settings);
                    });
        } catch (Exception e) {
            throw new SettingsException("获取用户设置失败", e);
        }
    }
    
    @Transactional
    public UserSettings updateSettings(UserSettings settings) {
        try {
            UserSettings existingSettings = settingsRepository.findByUserId(settings.getUserId())
                    .orElseGet(() -> new UserSettings());
            
            existingSettings.setUserId(settings.getUserId());
            existingSettings.setSoundEnabled(settings.getSoundEnabled());
            existingSettings.setVibrationEnabled(settings.getVibrationEnabled());
            existingSettings.setDefaultQuestionCount(settings.getDefaultQuestionCount());
            existingSettings.setDailyReminder(settings.getDailyReminder());
            existingSettings.setReminderTime(settings.getReminderTime());
            
            return settingsRepository.save(existingSettings);
        } catch (Exception e) {
            throw new SettingsException("更新用户设置失败", e);
        }
    }
    
    @Transactional
    public void updateReminder(Long userId, ReminderSettings reminderSettings) {
        try {
            UserSettings settings = getSettings(userId);
            settings.setDailyReminder(reminderSettings.getEnabled());
            settings.setReminderTime(reminderSettings.getTime());
            settingsRepository.save(settings);
        } catch (Exception e) {
            throw new SettingsException("更新提醒设置失败", e);
        }
    }
} 