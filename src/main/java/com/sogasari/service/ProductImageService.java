package com.sogasari.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sogasari.dto.response.ProductImageResponse;

public interface ProductImageService {

    List<ProductImageResponse> uploadImages(
            Long productId,
            List<MultipartFile> files
    );
}