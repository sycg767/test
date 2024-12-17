-- 先删除现有管理员账号
DELETE FROM admin_users WHERE username = 'admin';

-- 插入新的管理员账号
INSERT INTO admin_users (
    username,
    password,
    nickname,
    status,
    roles,
    created_at,
    updated_at
) VALUES (
    'admin',
    'admin123',
    '超级管理员',
    'ACTIVE',
    'ADMIN',
    NOW(),
    NOW()
); 