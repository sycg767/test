package com.example.question_bank.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxConfig {
    private String appid;
    private String secret;
} 