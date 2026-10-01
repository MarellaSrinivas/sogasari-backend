package com.sogasari.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.request.AdminSendOtpRequest;
import com.sogasari.dto.request.AdminVerifyOtpRequest;
import com.sogasari.dto.response.AdminLoginResponse;
import com.sogasari.service.AdminOtpAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminOtpAuthService adminOtpAuthService;

    @PostMapping("/otp/send")
    public ResponseEntity<?> sendOtp(
            @Valid @RequestBody AdminSendOtpRequest request
    ) {

        adminOtpAuthService.sendOtp(
                request.getPhone()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP sent successfully"
                )
        );
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<AdminLoginResponse> verifyOtp(
            @Valid @RequestBody AdminVerifyOtpRequest request
    ) {

        return ResponseEntity.ok(
                adminOtpAuthService.verifyOtp(
                        request.getPhone(),
                        request.getOtp()
                )
        );
    }
}