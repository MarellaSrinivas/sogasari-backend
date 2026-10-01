package com.sogasari.service;

public interface Fast2SmsService {

    void sendOtp(String phone);

    void verifyOtp(
            String phone,
            String otp
    );
}