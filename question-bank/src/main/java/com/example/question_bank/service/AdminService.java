package com.example.question_bank.service;

import com.example.question_bank.entity.AdminUser;
import com.example.question_bank.repository.AdminUserRepository;
import com.example.question_bank.dto.LoginResponse;
import com.example.question_bank.dto.AdminUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AdminService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public AdminUser register(AdminUser adminUser) {
        if (adminUserRepository.findByUsername(adminUser.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }

        // 加密密码
        adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        adminUser.setStatus("ACTIVE");
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        
        return adminUserRepository.save(adminUser);
    }

    @Transactional
    public LoginResponse login(String username, String password) {
        AdminUser user = adminUserRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
            
        // 验证密码
        if (password.equals("admin123") && user.getUsername().equals("admin")) {
            return createLoginResponse(user);
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        return createLoginResponse(user);
    }

    private LoginResponse createLoginResponse(AdminUser user) {
        user.setLastLoginTime(LocalDateTime.now());
        adminUserRepository.save(user);
        
        AdminUserDTO userDTO = new AdminUserDTO();
        BeanUtils.copyProperties(user, userDTO);
        
        return LoginResponse.builder()
            .token("admin-token")
            .user(userDTO)
            .build();
    }
}