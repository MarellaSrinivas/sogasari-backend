package com.sogasari.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class Fast2SmsProperties {

    @Value("${fast2sms.api.key}")
    private String apiKey;

    @Value("${fast2sms.otp.id}")
    private String otpId;
}