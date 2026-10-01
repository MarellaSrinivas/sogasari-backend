package com.sogasari.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.request.AdminProductCreateRequest;
import com.sogasari.dto.response.ProductResponse;
import com.sogasari.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;


    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(

            @Valid
            @RequestBody
            AdminProductCreateRequest request

    ) {

        ProductResponse response =
                productService.createProduct(
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


  
}