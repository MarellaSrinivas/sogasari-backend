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
public class AddressResponse {

    private Long id;

    private String fullName;

    private String phone;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String pincode;

    private Boolean defaultAddress;
}