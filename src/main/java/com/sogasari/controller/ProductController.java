package com.sogasari.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sogasari.dto.response.ProductResponse;
import com.sogasari.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getAllProducts() {

        return productService.getAllProducts();
    }

    @GetMapping("/new-arrivals")
    public List<ProductResponse> getNewArrivals() {

        return productService.getNewArrivals();
    }

    @GetMapping("/best-sellers")
    public List<ProductResponse> getBestSellers() {

        return productService.getBestSellers();
    }

    @GetMapping("/featured")
    public List<ProductResponse> getFeaturedProducts() {

        return productService.getFeaturedProducts();
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(
            @RequestParam String search
    ) {

        return productService.searchProducts(search);
    }

    @GetMapping("/category/{slug}")
    public List<ProductResponse> getProductsByCategory(
            @PathVariable String slug
    ) {

        return productService.getProductsByCategory(slug);
    }
@GetMapping("/id/{id}")
public ProductResponse getProductById(
        @PathVariable Long id
) {
    return productService.getProductById(id);
}
    @GetMapping("/{slug}")
    public ProductResponse getProduct(
            @PathVariable String slug
    ) {

        return productService.getProductBySlug(slug);
    }
}