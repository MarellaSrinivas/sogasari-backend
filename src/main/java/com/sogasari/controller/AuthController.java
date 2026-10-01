package com.sogasari.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.request.SendOtpRequest;
import com.sogasari.dto.request.VerifyOtpRequest;
import com.sogasari.dto.response.OtpLoginResponse;
import com.sogasari.service.OtpAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OtpAuthService otpAuthService;

    @PostMapping("/otp/send")
    public ResponseEntity<?> sendOtp(
            @Valid @RequestBody SendOtpRequest request
    ) {

        otpAuthService.sendOtp(
                request.getPhone()
        );

        return ResponseEntity.ok(
                java.util.Map.of(
                        "message",
                        "OTP sent successfully"
                )
        );
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<OtpLoginResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
    ) {

        return ResponseEntity.ok(
                otpAuthService.verifyOtp(
                        request.getPhone(),
                        request.getOtp()
                )
        );
    }
}