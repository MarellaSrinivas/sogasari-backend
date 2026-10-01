package com.sogasari.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminLoginResponse {

    private String accessToken;
    private Long adminId;
    private String phone;
    private String name;
}