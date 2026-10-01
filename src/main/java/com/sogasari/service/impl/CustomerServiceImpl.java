package com.sogasari.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sogasari.dto.request.AddressRequest;
import com.sogasari.dto.request.CustomerCheckoutRequest;
import com.sogasari.dto.response.AddressResponse;
import com.sogasari.dto.response.CustomerResponse;
import com.sogasari.entity.Address;
import com.sogasari.entity.User;
import com.sogasari.exception.CustomerNotFoundException;
import com.sogasari.repository.AddressRepository;
import com.sogasari.repository.UserRepository;
import com.sogasari.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl
        implements CustomerService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public CustomerResponse checkoutCustomer(
            CustomerCheckoutRequest request
    ) {

        /*
         * Find customer by phone.
         */
        User user =
                userRepository
                        .findByPhone(request.getPhone())
                        .orElse(null);

        /*
         * =========================
         * NEW CUSTOMER
         * =========================
         */

        if (user == null) {

            user = User.builder()
                    .phone(request.getPhone())
                    .name(request.getName())
                    .email(request.getEmail())
                    .build();

            user = userRepository.save(user);
        }

        /*
         * =========================
         * EXISTING CUSTOMER
         * =========================
         *
         * Update information if
         * checkout provides it.
         */

        else {

            if (request.getName() != null
                    && !request.getName().isBlank()) {

                user.setName(
                        request.getName()
                );
            }

            if (request.getEmail() != null
                    && !request.getEmail().isBlank()) {

                user.setEmail(
                        request.getEmail()
                );
            }

            user =
                    userRepository.save(user);
        }

        /*
         * =========================
         * ADDRESS
         * =========================
         */

        if (request.getAddress() != null) {

            saveAddress(
                    user,
                    request.getAddress()
            );
        }

        return buildCustomerResponse(user);
    }

    /*
     * =========================
     * GET CUSTOMER BY PHONE
     * =========================
     */

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByPhone(
            String phone
    ) {

       User user =
        userRepository
                .findByPhone(phone)
                .orElseThrow(
                        () -> new CustomerNotFoundException(
                                "Customer not found"
                        )
                );

        return buildCustomerResponse(user);
    }

    /*
     * =========================
     * SAVE ADDRESS
     * =========================
     */

    private Address saveAddress(
            User user,
            AddressRequest request
    ) {

        List<Address> existingAddresses =
                addressRepository.findByUserId(
                        user.getId()
                );

        boolean makeDefault =
                Boolean.TRUE.equals(
                        request.getDefaultAddress()
                );

        /*
         * First address should automatically
         * become default.
         */

        if (existingAddresses.isEmpty()) {
            makeDefault = true;
        }

        /*
         * If this address is default,
         * remove default from old addresses.
         */

        if (makeDefault) {

            existingAddresses.forEach(
                    address ->
                            address.setDefaultAddress(false)
            );

            addressRepository.saveAll(
                    existingAddresses
            );
        }

        Address address =
                Address.builder()
                        .user(user)
                        .fullName(
                                request.getFullName()
                        )
                        .phone(
                                request.getPhone()
                        )
                        .addressLine1(
                                request.getAddressLine1()
                        )
                        .addressLine2(
                                request.getAddressLine2()
                        )
                        .city(
                                request.getCity()
                        )
                        .state(
                                request.getState()
                        )
                        .pincode(
                                request.getPincode()
                        )
                        .defaultAddress(
                                makeDefault
                        )
                        .build();

        return addressRepository.save(address);
    }

    /*
     * =========================
     * RESPONSE
     * =========================
     */

    private CustomerResponse buildCustomerResponse(
            User user
    ) {

        List<AddressResponse> addresses =
                addressRepository
                        .findByUserIdOrderByDefaultAddressDesc(
                                user.getId()
                        )
                        .stream()
                        .map(this::mapAddress)
                        .toList();

        return CustomerResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .name(user.getName())
                .email(user.getEmail())
                .addresses(addresses)
                .build();
    }

    private AddressResponse mapAddress(
            Address address
    ) {

        return AddressResponse.builder()
                .id(address.getId())
                .fullName(
                        address.getFullName()
                )
                .phone(
                        address.getPhone()
                )
                .addressLine1(
                        address.getAddressLine1()
                )
                .addressLine2(
                        address.getAddressLine2()
                )
                .city(
                        address.getCity()
                )
                .state(
                        address.getState()
                )
                .pincode(
                        address.getPincode()
                )
                .defaultAddress(
                        address.getDefaultAddress()
                )
                .build();
    }
}