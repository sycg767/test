package com.example.question_bank;

import com.example.question_bank.entity.AdminUser;
import com.example.question_bank.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminUserTest {
    
    @Autowired
    private AdminService adminService;
    
    @Test
    public void createAdmin() {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername("admin");
        adminUser.setPassword("admin123");
        adminUser.setNickname("超级管理员");
        adminUser.setRoles("ADMIN");
        
        AdminUser saved = adminService.register(adminUser);
        System.out.println("Created admin user with id: " + saved.getId());
        System.out.println("Encrypted password: " + saved.getPassword());
    }
} 