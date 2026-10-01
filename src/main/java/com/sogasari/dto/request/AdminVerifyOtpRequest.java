package com.sogasari.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminVerifyOtpRequest {

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "OTP is required")
    private String otp;
}