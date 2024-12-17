package com.example.question_bank;

import com.example.question_bank.entity.AdminUser;
import com.example.question_bank.repository.AdminUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AdminPasswordResetTest {
    
    @Autowired
    private AdminUserRepository adminUserRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Test
    @Transactional
    @Rollback(false)
    public void resetAdminPassword() {
        String username = "admin";
        String password = "admin123";
        
        // 获取或创建管理员账号
        AdminUser adminUser = adminUserRepository.findByUsername(username)
            .orElseGet(() -> {
                AdminUser newAdmin = new AdminUser();
                newAdmin.setUsername(username);
                newAdmin.setNickname("超级管理员");
                newAdmin.setStatus("ACTIVE");
                newAdmin.setRoles("ADMIN");
                newAdmin.setCreatedAt(LocalDateTime.now());
                newAdmin.setUpdatedAt(LocalDateTime.now());
                return newAdmin;
            });
        
        // 生成新密码哈希
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("New password hash: " + encodedPassword);
        
        // 更新密码
        adminUser.setPassword(encodedPassword);
        adminUser = adminUserRepository.save(adminUser);
        
        // 验证
        boolean matches = passwordEncoder.matches(password, adminUser.getPassword());
        System.out.println("Password verification: " + matches);
        
        // 确保验证成功
        assertTrue(matches, "Password verification should succeed");
        
        System.out.println("Admin user updated:");
        System.out.println("ID: " + adminUser.getId());
        System.out.println("Username: " + adminUser.getUsername());
        System.out.println("Status: " + adminUser.getStatus());
        System.out.println("Password hash: " + adminUser.getPassword());
    }
} 