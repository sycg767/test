package com.example.question_bank.service;

import com.example.question_bank.entity.Admin;
import com.example.question_bank.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, Object> login(String username, String password) {
        log.debug("Attempting login for admin user: {}", username);
        
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("管理员账号不存在"));

        log.debug("Found admin user: {}", admin.getUsername());
        log.debug("Stored password hash: {}", admin.getPassword());
        log.debug("Input password: {}", password);
        
        // 使用PasswordEncoder验证密码
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 更新最后登录时间
        admin.setLastLoginTime(LocalDateTime.now());
        adminRepository.save(admin);

        Map<String, Object> result = new HashMap<>();
        result.put("id", admin.getId());
        result.put("username", admin.getUsername());
        result.put("nickname", admin.getNickname());
        result.put("roles", admin.getRoles());
        result.put("status", admin.getStatus());
        
        return result;
    }
}