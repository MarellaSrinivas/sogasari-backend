package com.sogasari.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpLoginResponse {

    private String accessToken;

    private Long userId;

    private String phone;

    private String name;

    private String email;

    private boolean newUser;
}