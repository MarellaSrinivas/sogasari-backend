
package com.sogasari.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.request.RefreshTokenRequest;
import com.sogasari.dto.request.SendOtpRequest;
import com.sogasari.dto.request.VerifyOtpRequest;
import com.sogasari.dto.response.OtpLoginResponse;
import com.sogasari.dto.response.RefreshTokenResponse;
import com.sogasari.security.JwtService;
import com.sogasari.service.OtpAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

        private final OtpAuthService otpAuthService;
        private final JwtService jwtService;

        @PostMapping("/otp/send")
        public ResponseEntity<?> sendOtp(
                        @Valid @RequestBody SendOtpRequest request) {

                otpAuthService.sendOtp(request.getPhone());

                return ResponseEntity.ok(
                                Map.of("message", "OTP sent successfully"));
        }

        @PostMapping("/otp/verify")
        public ResponseEntity<OtpLoginResponse> verifyOtp(
                        @Valid @RequestBody VerifyOtpRequest request) {

                return ResponseEntity.ok(
                                otpAuthService.verifyOtp(
                                                request.getPhone(),
                                                request.getOtp()));
        }

        @PostMapping("/refresh")
        public ResponseEntity<RefreshTokenResponse> refresh(
                        @Valid @RequestBody RefreshTokenRequest request) {

                String refreshToken = request.getRefreshToken();

                if (refreshToken == null
                                || !jwtService.isValidRefreshToken(refreshToken)) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                        .build();
                }

                String phone = jwtService.extractPhone(refreshToken);

                String newAccessToken = jwtService.generateToken(phone);

                String newRefreshToken = jwtService.generateRefreshToken(phone);

                return ResponseEntity.ok(
                                new RefreshTokenResponse(
                                                newAccessToken,
                                                newRefreshToken));
        }
}
