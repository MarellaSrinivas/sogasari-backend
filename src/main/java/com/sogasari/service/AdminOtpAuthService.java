package com.sogasari.service;

import com.sogasari.dto.response.AdminLoginResponse;

public interface AdminOtpAuthService {

    void sendOtp(String phone);

    AdminLoginResponse verifyOtp(
            String phone,
            String otp
    );
}