package com.sogasari.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.request.CustomerCheckoutRequest;
import com.sogasari.dto.response.CustomerResponse;
import com.sogasari.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /*
     * Checkout customer.
     *
     * Creates customer if phone doesn't exist.
     * Updates customer if phone already exists.
     * Saves address when supplied.
     */
    @PostMapping("/checkout")
    public CustomerResponse checkoutCustomer(
            @Valid @RequestBody
            CustomerCheckoutRequest request
    ) {

        return customerService.checkoutCustomer(
                request
        );
    }

    /*
     * Load existing customer by phone.
     */
    @GetMapping("/phone/{phone}")
    public CustomerResponse getCustomerByPhone(
            @PathVariable String phone
    ) {

        return customerService.getCustomerByPhone(
                phone
        );
    }
}