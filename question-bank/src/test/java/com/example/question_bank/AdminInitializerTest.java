package com.example.question_bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.question_bank.entity.AdminUser;
import com.example.question_bank.service.AdminService;

@SpringBootTest
public class AdminInitializerTest {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Test
    public void initializeAdmin() {
        try {
            AdminUser admin = new AdminUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setStatus("ACTIVE");
            admin = adminService.register(admin);
            
            System.out.println("Successfully created admin user: " + admin.getUsername());
            System.out.println("Password has been encrypted and saved");
        } catch (Exception e) {
            System.out.println("Admin user might already exist: " + e.getMessage());
        }
    }
} 