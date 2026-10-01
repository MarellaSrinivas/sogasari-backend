package com.sogasari.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpRequest {

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Enter a valid Indian mobile number"
    )
    private String phone;

    @NotBlank(message = "OTP is required")
    @Pattern(
            regexp = "^[0-9]{4,10}$",
            message = "Enter a valid OTP"
    )
    private String otp;
}