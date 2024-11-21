package com.example.questionbank.repository;

import com.example.questionbank.entity.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, Long> {
    @Query("SELECT v FROM VerifyCode v WHERE v.phone = ?1 ORDER BY v.createTime DESC LIMIT 1")
    Optional<VerifyCode> findLatestByPhone(String phone);
} 