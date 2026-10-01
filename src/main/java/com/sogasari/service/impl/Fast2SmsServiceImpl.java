package com.sogasari.service.impl;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.sogasari.config.Fast2SmsProperties;
import com.sogasari.service.Fast2SmsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Fast2SmsServiceImpl
        implements Fast2SmsService {

    private final Fast2SmsProperties properties;

    private final RestClient restClient =
            RestClient.builder()
                    .baseUrl(
                            "https://www.fast2sms.com"
                    )
                    .build();

    @Override
    public void sendOtp(
            String phone
    ) {

        Map<String, Object> request =
                Map.of(
                        "mobile",
                        phone,

                        "otp_id",
                        properties.getOtpId()
                );

        restClient
                .post()
                .uri("/dev/otp/send")
                .header(
                        "Authorization",
                        properties.getApiKey()
                )
                .contentType(
                        MediaType.APPLICATION_JSON
                )
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void verifyOtp(
            String phone,
            String otp
    ) {

        Map<String, Object> request =
                Map.of(
                        "mobile",
                        phone,

                        "otp",
                        otp
                );

        restClient
                .post()
                .uri("/dev/otp/verify")
                .header(
                        "Authorization",
                        properties.getApiKey()
                )
                .contentType(
                        MediaType.APPLICATION_JSON
                )
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}