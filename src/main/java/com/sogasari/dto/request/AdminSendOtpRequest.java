package com.sogasari.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSendOtpRequest {

    @NotBlank(message = "Phone number is required")
    private String phone;
}