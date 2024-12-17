-- 重命名 practice_records 表为 study_records
RENAME TABLE practice_records TO study_records;

-- 更新相关的外键约束（如果有的话）
-- ALTER TABLE related_table 
-- RENAME INDEX idx_practice_record_id TO idx_study_record_id; 