package com.sogasari.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sogasari.dto.response.ProductImageResponse;
import com.sogasari.service.ProductImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductImageController {

    private final ProductImageService productImageService;


    @PostMapping(
            value = "/{productId}/images",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<List<ProductImageResponse>>
    uploadImages(

            @PathVariable
            Long productId,

            @RequestParam("images")
            List<MultipartFile> images

    ) {

        return ResponseEntity.ok(
                productImageService.uploadImages(
                        productId,
                        images
                )
        );
    }
}