-- 插入测试学习记录
INSERT INTO study_records 
(user_id, bank_id, mode, total_questions, correct_count, spend_time, correct_rate, create_time)
VALUES 
(28, 1, 'sequence', 10, 7, 300, 70.0, UNIX_TIMESTAMP() * 1000),
(28, 1, 'random', 10, 8, 250, 80.0, UNIX_TIMESTAMP() * 1000),
(29, 1, 'sequence', 10, 6, 280, 60.0, UNIX_TIMESTAMP() * 1000),
(29, 2, 'random', 10, 9, 320, 90.0, UNIX_TIMESTAMP() * 1000); 