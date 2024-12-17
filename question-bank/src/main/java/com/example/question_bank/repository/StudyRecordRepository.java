package com.example.question_bank.repository;

import com.example.question_bank.entity.StudyRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {
    
    // 获取用户的学习记录，分页
    Page<StudyRecord> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);
    
    // 获取用户在特定题库的学习记录，分页
    Page<StudyRecord> findByUserIdAndBankIdOrderByCreateTimeDesc(Long userId, Long bankId, Pageable pageable);
    
    // 获取用户的学习统计
    @Query("SELECT COUNT(p) as totalCount, " +
           "SUM(p.correctCount) as totalCorrect, " +
           "AVG(p.correctRate) as avgCorrectRate " +
           "FROM StudyRecord p " +
           "WHERE p.userId = :userId")
    List<Object[]> getUserStudyStats(@Param("userId") Long userId);
    
    // 获取用户今日学习次数
    @Query("SELECT COUNT(p) FROM StudyRecord p " +
           "WHERE p.userId = :userId " +
           "AND p.createTime >= :todayStart " +
           "AND p.createTime < :todayEnd")
    Long countTodayStudies(@Param("userId") Long userId, 
                          @Param("todayStart") Long todayStart,
                          @Param("todayEnd") Long todayEnd);
} 