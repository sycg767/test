package com.example.question_bank.controller;

import com.example.question_bank.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/phone")
public class PhoneController {
    
    @Autowired
    private PhoneService phoneService;
    
    @PostMapping("/verify-code")
    public ResponseEntity<Void> sendVerifyCode(@RequestBody String phone) {
        phoneService.sendVerifyCode(phone);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/bind")
    public ResponseEntity<Void> bindPhone(
            @RequestAttribute Long userId,
            @RequestParam String phone,
            @RequestParam String code) {
        phoneService.bindPhone(userId, phone, code);
        return ResponseEntity.ok().build();
    }
} 