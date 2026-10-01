package com.sogasari.service;

import com.sogasari.dto.request.CustomerCheckoutRequest;
import com.sogasari.dto.response.CustomerResponse;

public interface CustomerService {

    CustomerResponse checkoutCustomer(
            CustomerCheckoutRequest request
    );

    CustomerResponse getCustomerByPhone(
            String phone
    );
}