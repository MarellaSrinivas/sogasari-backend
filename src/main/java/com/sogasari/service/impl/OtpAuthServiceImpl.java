package com.sogasari.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sogasari.dto.response.OtpLoginResponse;
import com.sogasari.entity.User;
import com.sogasari.repository.UserRepository;
import com.sogasari.security.JwtService;
import com.sogasari.service.Fast2SmsService;
import com.sogasari.service.OtpAuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpAuthServiceImpl
                implements OtpAuthService {

        private final Fast2SmsService fast2SmsService;
        private final UserRepository userRepository;
        private final JwtService jwtService;

        @Override
        public void sendOtp(String phone) {

                String normalizedPhone = normalizePhone(phone);

                fast2SmsService.sendOtp(
                                normalizedPhone);
        }

        @Override
        @Transactional
        public OtpLoginResponse verifyOtp(
                        String phone,
                        String otp) {

                String normalizedPhone = normalizePhone(phone);

                // Fast2SMS verifies the OTP
                fast2SmsService.verifyOtp(
                                normalizedPhone,
                                otp);

                User user = userRepository
                                .findByPhone(normalizedPhone)
                                .orElse(null);

                boolean newUser = false;

                if (user == null) {

                        user = User.builder()
                                        .phone(normalizedPhone)
                                        .build();

                        user = userRepository.save(user);

                        newUser = true;
                }

                String accessToken = jwtService.generateToken(normalizedPhone);

                String refreshToken = jwtService.generateRefreshToken(normalizedPhone);

                return OtpLoginResponse.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .userId(user.getId())
                                .phone(user.getPhone())
                                .name(user.getName())
                                .email(user.getEmail())
                                .newUser(newUser)
                                .build();
        }

        private String normalizePhone(
                        String phone) {

                if (phone == null) {
                        throw new IllegalArgumentException(
                                        "Phone number is required");
                }

                String value = phone.replaceAll("\\s+", "");

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