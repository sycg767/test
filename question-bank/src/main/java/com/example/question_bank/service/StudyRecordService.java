package com.example.question_bank.service;

import com.example.question_bank.entity.StudyRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface StudyRecordService {
    
    // 保存学习记录
    StudyRecord saveStudyRecord(StudyRecord record);
    
    // 获取用户的学习记录
    Page<StudyRecord> getUserStudyRecords(Long userId, Long bankId, Pageable pageable);
    
    // 获取用户的学习统计
    Map<String, Object> getUserStudyStats(Long userId);
    
    // 获取用户今日学习次数
    Long getTodayStudyCount(Long userId);
} 