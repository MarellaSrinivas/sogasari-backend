package com.sogasari.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sogasari.dto.response.AdminLoginResponse;
import com.sogasari.entity.Admin;
import com.sogasari.repository.AdminRepository;
import com.sogasari.security.JwtService;
import com.sogasari.service.AdminOtpAuthService;
import com.sogasari.service.Fast2SmsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminOtpAuthServiceImpl
        implements AdminOtpAuthService {

    private final Fast2SmsService fast2SmsService;

    private final AdminRepository adminRepository;

    private final JwtService jwtService;

    @Override
    public void sendOtp(String phone) {

        String normalizedPhone =
                normalizePhone(phone);

        // IMPORTANT:
        // Only an existing active admin
        // can receive an OTP.

        Admin admin =
                adminRepository
                        .findByPhoneAndActiveTrue(
                                normalizedPhone
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Unauthorized admin phone number"
                                )
                        );

        // Reuse your existing Fast2SMS service
        fast2SmsService.sendOtp(
                admin.getPhone()
        );
    }

    @Override
    @Transactional
    public AdminLoginResponse verifyOtp(
            String phone,
            String otp
    ) {

        String normalizedPhone =
                normalizePhone(phone);

        // Check admin again.
        // Do NOT trust the previous send-OTP request.

        Admin admin =
                adminRepository
                        .findByPhoneAndActiveTrue(
                                normalizedPhone
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Unauthorized admin"
                                )
                        );

        // Fast2SMS verifies OTP
        fast2SmsService.verifyOtp(
                normalizedPhone,
                otp
        );

        // Generate JWT
        String token =
        jwtService.generateToken(
                normalizedPhone,
                "ADMIN"
        );

        return AdminLoginResponse.builder()
                .accessToken(token)
                .adminId(admin.getId())
                .phone(admin.getPhone())
                .name(admin.getName())
                .build();
    }

    private String normalizePhone(
            String phone
    ) {

        if (phone == null) {
            throw new IllegalArgumentException(
                    "Phone number is required"
            );
        }

        String value =
                phone.replaceAll("\\s+", "");

        if (value.startsWith("+91")) {
            value = value.substring(3);
        }

        if (value.startsWith("91")
                && value.length() == 12) {
            value = value.substring(2);
        }

        return value;
    }
}