package com.sogasari.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class AddressRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100)
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Enter a valid Indian mobile number"
    )
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 250)
    private String addressLine1;

    @Size(max = 250)
    private String addressLine2;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100)
    private String state;

    @NotBlank(message = "Pincode is required")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Enter a valid 6 digit pincode"
    )
    private String pincode;

    private Boolean defaultAddress;
}