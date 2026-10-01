package com.sogasari.service;

import com.sogasari.dto.response.OtpLoginResponse;

public interface OtpAuthService {

    void sendOtp(String phone);

    OtpLoginResponse verifyOtp(
            String phone,
            String otp
    );
}