package com.sogasari.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sogasari.dto.request.AdminProductCreateRequest;
import com.sogasari.dto.response.ProductImageResponse;
import com.sogasari.dto.response.ProductResponse;

 
 
 
public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductResponse getProductBySlug(String slug);

    List<ProductResponse> getProductsByCategory(String slug);

    List<ProductResponse> getNewArrivals();

    List<ProductResponse> getBestSellers();

    List<ProductResponse> getFeaturedProducts();

    List<ProductResponse> searchProducts(String search);

        ProductResponse getProductById(Long id);

        ProductResponse createProduct(
        AdminProductCreateRequest request
);

List<ProductImageResponse> uploadProductImages(
        Long productId,
        List<MultipartFile> images
);

}