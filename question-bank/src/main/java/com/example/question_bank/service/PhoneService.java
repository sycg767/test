package com.example.question_bank.service;

import com.example.question_bank.entity.User;
import com.example.question_bank.entity.VerifyCode;
import com.example.question_bank.repository.UserRepository;
import com.example.question_bank.repository.VerifyCodeRepository;
import com.example.question_bank.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PhoneService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VerifyCodeRepository verifyCodeRepository;
    
    // 发送验证码
    @Transactional
    public void sendVerifyCode(String phone) {
        // 验证手机号格式
        if (!isValidPhone(phone)) {
            throw new BusinessException("手机号格式不正确");
        }
        
        // 检查手机号是否已被绑定
        if (userRepository.existsByPhone(phone)) {
            throw new BusinessException("该手机号已被绑定");
        }
        
        // 检查是否频繁发送
        verifyCodeRepository.findLatestByPhone(phone).ifPresent(code -> {
            if (code.getCreateTime().plusMinutes(1).isAfter(LocalDateTime.now())) {
                throw new BusinessException("请勿频繁发送验证码");
            }
        });
        
        // 生成验证码
        String code = generateVerifyCode();
        
        // 保存验证码
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setPhone(phone);
        verifyCode.setCode(code);
        verifyCode.setExpireTime(LocalDateTime.now().plusMinutes(5));
        verifyCodeRepository.save(verifyCode);
        
        // TODO: 调用短信服务发送验证码
        System.out.println("发送验证码到手机: " + phone + ", 验证码: " + code);
    }
    
    // 绑定手机号
    @Transactional
    public void bindPhone(Long userId, String phone, String code) {
        // 验证手机号格式
        if (!isValidPhone(phone)) {
            throw new BusinessException("手机号格式不正确");
        }
        
        // 检查手机号是否已被绑定
        if (userRepository.existsByPhone(phone)) {
            throw new BusinessException("该手机号已被绑定");
        }
        
        // 验证验证码
        VerifyCode verifyCode = verifyCodeRepository.findLatestByPhone(phone)
                .orElseThrow(() -> new BusinessException("验证码不存在"));
                
        if (verifyCode.getUsed()) {
            throw new BusinessException("验证码已使用");
        }
        
        if (verifyCode.getExpireTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("验证码已过期");
        }
        
        if (!verifyCode.getCode().equals(code)) {
            throw new BusinessException("验证码错误");
        }
        
        // 更新用户手机号
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
                
        user.setPhone(phone);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        // 标记验证码已使用
        verifyCode.setUsed(true);
        verifyCodeRepository.save(verifyCode);
    }
    
    // 验证手机号格式
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^1[3-9]\\d{9}$");
    }
    
    // 生成6位数字验证码
    private String generateVerifyCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
} 