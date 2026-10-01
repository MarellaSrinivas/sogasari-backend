package com.sogasari.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCheckoutRequest {

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Enter a valid Indian mobile number"
    )
    private String phone;

    @Size(max = 100, message = "Name is too long")
    private String name;

    @Email(message = "Enter a valid email")
    @Size(max = 150)
    private String email;

    @Valid
    private AddressRequest address;
}