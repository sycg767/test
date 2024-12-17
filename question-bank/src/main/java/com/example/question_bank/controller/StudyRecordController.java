package com.example.question_bank.controller;

import com.example.question_bank.entity.StudyRecord;
import com.example.question_bank.service.StudyRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/study-records")
public class StudyRecordController {

    @Autowired
    private StudyRecordService studyRecordService;

    @PostMapping
    public ResponseEntity<StudyRecord> createStudyRecord(@RequestBody StudyRecord record) {
        try {
            StudyRecord savedRecord = studyRecordService.saveStudyRecord(record);
            return ResponseEntity.ok(savedRecord);
        } catch (Exception e) {
            log.error("创建学习记录失败", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<StudyRecord>> getStudyRecords(
            @RequestParam Long userId,
            @RequestParam(required = false) Long bankId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<StudyRecord> records = studyRecordService.getUserStudyRecords(
                userId, bankId, PageRequest.of(page, size));
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            log.error("获取学习记录失败", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStudyStats(@RequestParam Long userId) {
        try {
            Map<String, Object> stats = studyRecordService.getUserStudyStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("获取学习统计失败", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/today-count")
    public ResponseEntity<Long> getTodayStudyCount(@RequestParam Long userId) {
        try {
            Long count = studyRecordService.getTodayStudyCount(userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("获取今日学习次数失败", e);
            return ResponseEntity.badRequest().build();
        }
    }
} 