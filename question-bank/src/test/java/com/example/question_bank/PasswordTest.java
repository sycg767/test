package com.example.question_bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTest {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Test
    public void generatePassword() {
        String password = "admin123";
        
        // 生成新的密码哈希
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("Generated password hash: " + encodedPassword);
        
        // 验证新生成的哈希
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        System.out.println("Verification result with new hash: " + matches);
        
        // 验证数据库中的密码
        String dbPassword = "$2a$10$aeFRH0HWg6BtCsShv8B0Ke1Bh84qm2g1Eq/GTczXpgf1W3tPgrzae";
        boolean dbMatches = passwordEncoder.matches(password, dbPassword);
        System.out.println("Database password verification: " + dbMatches);
        
        // 测试带空格的密码
        String paddedPassword = " admin123 ";
        boolean paddedMatches = passwordEncoder.matches(paddedPassword.trim(), dbPassword);
        System.out.println("Padded password verification: " + paddedMatches);
        
        // 生成多个哈希值比较
        System.out.println("\nGenerating multiple hashes for the same password:");
        for (int i = 0; i < 3; i++) {
            String hash = passwordEncoder.encode(password);
            System.out.println("Hash " + (i+1) + ": " + hash);
            System.out.println("Matches original: " + passwordEncoder.matches(password, hash));
            System.out.println("Matches DB hash: " + passwordEncoder.matches(password, dbPassword));
        }
    }
} 