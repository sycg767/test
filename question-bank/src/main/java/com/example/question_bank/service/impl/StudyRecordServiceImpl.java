package com.example.question_bank.service.impl;

import com.example.question_bank.entity.StudyRecord;
import com.example.question_bank.repository.StudyRecordRepository;
import com.example.question_bank.service.StudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudyRecordServiceImpl implements StudyRecordService {

    @Autowired
    private StudyRecordRepository studyRecordRepository;

    @Override
    public StudyRecord saveStudyRecord(StudyRecord record) {
        if (record.getCreateTime() == null) {
            record.setCreateTime(System.currentTimeMillis());
        }
        return studyRecordRepository.save(record);
    }

    @Override
    public Page<StudyRecord> getUserStudyRecords(Long userId, Long bankId, Pageable pageable) {
        if (bankId != null) {
            return studyRecordRepository.findByUserIdAndBankIdOrderByCreateTimeDesc(userId, bankId, pageable);
        }
        return studyRecordRepository.findByUserIdOrderByCreateTimeDesc(userId, pageable);
    }

    @Override
    public Map<String, Object> getUserStudyStats(Long userId) {
        List<Object[]> stats = studyRecordRepository.getUserStudyStats(userId);
        Map<String, Object> result = new HashMap<>();
        
        if (!stats.isEmpty()) {
            Object[] stat = stats.get(0);
            result.put("totalCount", stat[0]);
            result.put("totalCorrect", stat[1]);
            result.put("avgCorrectRate", stat[2]);
        } else {
            result.put("totalCount", 0);
            result.put("totalCorrect", 0);
            result.put("avgCorrectRate", 0.0);
        }
        
        // 添加今日学习次数
        result.put("todayCount", getTodayStudyCount(userId));
        
        return result;
    }

    @Override
    public Long getTodayStudyCount(Long userId) {
        LocalDate today = LocalDate.now();
        long todayStart = today.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        long todayEnd = today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        
        return studyRecordRepository.countTodayStudies(userId, todayStart, todayEnd);
    }
} 