package com.sogasari.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sogasari.entity.Category;
import com.sogasari.entity.Product;
import com.sogasari.entity.ProductImage;
import com.sogasari.entity.ProductVariant;
import com.sogasari.repository.CategoryRepository;
import com.sogasari.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {

        seedCategories();
        seedProducts();
    }

    // =====================================================
    // CATEGORIES
    // =====================================================

    private void seedCategories() {

        if (categoryRepository.count() > 0) {
            return;
        }

        createCategory(
                "Kanjivaram Sarees",
                "kanjivaram-sarees",
                "Traditional Kanjivaram silk sarees",
                "/images/categories/sarees.jpg"
        );

        createCategory(
                "Banarasi Sarees",
                "banarasi-sarees",
                "Elegant Banarasi silk sarees",
                "/images/categories/sarees.jpg"
        );

        createCategory(
                "Organza Sarees",
                "organza-sarees",
                "Lightweight and elegant organza sarees",
                "/images/categories/sarees.jpg"
        );

        createCategory(
                "Handloom Sarees",
                "handloom-sarees",
                "Beautiful traditional handloom sarees",
                "/images/categories/sarees.jpg"
        );

        createCategory(
                "Lehengas",
                "lehengas",
                "Designer and bridal lehengas",
                "/images/categories/lehengas.jpg"
        );

        createCategory(
                "Salwar Suits",
                "salwar-suits",
                "Elegant salwar suits and anarkalis",
                "/images/categories/salwar-suits.jpg"
        );

        createCategory(
                "Men's Wear",
                "mens-wear",
                "Traditional and contemporary men's ethnic wear",
                "/images/categories/mens-wear.jpg"
        );

        createCategory(
                "Wedding Wear",
                "wedding-wear",
                "Premium wedding and bridal collections",
                "/images/categories/wedding-wear.jpg"
        );
    }

    private void createCategory(
            String name,
            String slug,
            String description,
            String image
    ) {

        Category category = Category.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .image(image)
                .active(true)
                .build();

        categoryRepository.save(category);
    }

    // =====================================================
    // PRODUCTS
    // =====================================================

    private void seedProducts() {

        if (productRepository.count() > 0) {
            return;
        }

        // =================================================
        // NEW ARRIVALS
        // =================================================

        createProduct(
                "Handwoven Kanjivaram Silk Saree",
                "handwoven-kanjivaram-silk-saree",
                "SGS-KAN-001",
                "kanjivaram-sarees",
                "A timeless handwoven Kanjivaram silk saree.",
                "A timeless Kanjivaram silk saree crafted with traditional weaving techniques and rich detailing. Designed for weddings, festive occasions and celebrations.",
                18999,
                21999,
                14,
                "New",
                10,
                false,
                false,
                true,
                "/images/products/new-arrivals/product-1.jpg"
        );

        createProduct(
                "Banarasi Tissue Silk Saree",
                "banarasi-tissue-silk-saree",
                "SGS-BAN-001",
                "banarasi-sarees",
                "Elegant Banarasi tissue silk saree.",
                "A beautifully crafted Banarasi tissue silk saree with elegant detailing, perfect for festive occasions and celebrations.",
                12499,
                14999,
                17,
                "New",
                10,
                false,
                false,
                true,
                "/images/products/new-arrivals/product-2.jpg"
        );

        createProduct(
                "Embroidered Bridal Lehenga",
                "embroidered-bridal-lehenga",
                "SGS-LEH-001",
                "lehengas",
                "Premium embroidered bridal lehenga.",
                "A beautifully embroidered bridal lehenga designed for weddings and grand celebrations.",
                28999,
                34999,
                17,
                "New",
                5,
                false,
                false,
                true,
                "/images/products/new-arrivals/product-3.jpg"
        );

        createProduct(
                "Designer Organza Saree",
                "designer-organza-saree",
                "SGS-ORG-001",
                "organza-sarees",
                "Elegant designer organza saree.",
                "A lightweight designer organza saree featuring elegant detailing for festive and party occasions.",
                8999,
                10999,
                18,
                "New",
                12,
                false,
                false,
                true,
                "/images/products/new-arrivals/product-4.jpg"
        );

        createProduct(
                "Festive Anarkali Suit",
                "festive-anarkali-suit",
                "SGS-SAL-001",
                "salwar-suits",
                "Elegant festive Anarkali suit.",
                "A beautifully designed Anarkali suit perfect for festive occasions and celebrations.",
                7499,
                8999,
                17,
                "New",
                15,
                false,
                false,
                true,
                "/images/products/new-arrivals/product-5.jpg"
        );

        createProduct(
                "Traditional Silk Sherwani",
                "traditional-silk-sherwani",
                "SGS-MEN-001",
                "mens-wear",
                "Traditional silk sherwani.",
                "A sophisticated traditional silk sherwani designed for weddings and special occasions.",
                15999,
                18999,
                16,
                "New",
                8,
                false,
                false,
                true,
                "/images/products/new-arrivals/product-6.jpg"
        );

        // =================================================
        // BEST SELLERS
        // =================================================

        createProduct(
                "Pure Kanjivaram Silk Saree",
                "pure-kanjivaram-silk-saree",
                "SGS-KAN-002",
                "kanjivaram-sarees",
                "Pure Kanjivaram silk saree.",
                "A premium Kanjivaram silk saree with traditional craftsmanship and elegant zari detailing.",
                15999,
                18999,
                16,
                "Best Seller",
                15,
                false,
                true,
                false,
                "/images/products/best-sellers/product-1.jpg"
        );

        createProduct(
                "Banarasi Handwoven Silk Saree",
                "banarasi-handwoven-silk-saree",
                "SGS-BAN-002",
                "banarasi-sarees",
                "Handwoven Banarasi silk saree.",
                "A luxurious handwoven Banarasi silk saree designed for festive and wedding occasions.",
                13999,
                16999,
                18,
                "Best Seller",
                12,
                false,
                true,
                false,
                "/images/products/best-sellers/product-2.jpg"
        );

        createProduct(
                "Designer Festive Lehenga",
                "designer-festive-lehenga",
                "SGS-LEH-002",
                "lehengas",
                "Designer festive lehenga.",
                "A stylish designer lehenga created for festive celebrations and special occasions.",
                24999,
                29999,
                17,
                "Best Seller",
                7,
                false,
                true,
                false,
                "/images/products/best-sellers/product-3.jpg"
        );

        createProduct(
                "Embroidered Anarkali Suit",
                "embroidered-anarkali-suit",
                "SGS-SAL-002",
                "salwar-suits",
                "Beautiful embroidered Anarkali suit.",
                "A sophisticated embroidered Anarkali suit designed for festive and party occasions.",
                8499,
                9999,
                15,
                "Best Seller",
                10,
                false,
                true,
                false,
                "/images/products/best-sellers/product-4.jpg"
        );

        createProduct(
                "Chanderi Handloom Saree",
                "chanderi-handloom-saree",
                "SGS-HAN-001",
                "handloom-sarees",
                "Traditional Chanderi handloom saree.",
                "A graceful Chanderi handloom saree combining traditional craftsmanship with contemporary elegance.",
                6999,
                8499,
                18,
                "Popular",
                20,
                false,
                true,
                false,
                "/images/products/best-sellers/product-5.jpg"
        );

        createProduct(
                "Wedding Silk Sherwani",
                "wedding-silk-sherwani",
                "SGS-MEN-002",
                "mens-wear",
                "Premium wedding silk sherwani.",
                "A premium silk sherwani designed for weddings, receptions and special celebrations.",
                17999,
                21999,
                18,
                "Best Seller",
                8,
                false,
                true,
                false,
                "/images/products/best-sellers/product-6.jpg"
        );
    }

    // =====================================================
    // CREATE PRODUCT
    // =====================================================

    private void createProduct(
            String name,
            String slug,
            String sku,
            String categorySlug,
            String shortDescription,
            String description,
            double price,
            double originalPrice,
            int discount,
            String badge,
            int stock,
            boolean featured,
            boolean bestSeller,
            boolean newArrival,
            String imageUrl
    ) {

        Category category =
                categoryRepository
                        .findBySlug(categorySlug)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found: "
                                                + categorySlug
                                )
                        );

        Product product = Product.builder()
                .name(name)
                .slug(slug)
                .sku(sku)
                .category(category)
                .shortDescription(shortDescription)
                .description(description)
                .price(BigDecimal.valueOf(price))
                .originalPrice(
                        BigDecimal.valueOf(originalPrice)
                )
                .discount(discount)
                .badge(badge)
                .stock(stock)
                .featured(featured)
                .bestSeller(bestSeller)
                .newArrival(newArrival)
                .active(true)
                .build();

        // =============================================
        // PRIMARY IMAGE
        // =============================================

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl(imageUrl)
                .displayOrder(0)
                .primaryImage(true)
                .build();

        product.getImages().add(image);

        // =============================================
        // DEFAULT VARIANT
        // =============================================

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .colorName("Maroon")
                .colorCode("#7d1d2b")
                .size("Free Size")
                .stock(stock)
                .additionalPrice(BigDecimal.ZERO)
                .active(true)
                .build();

        product.getVariants().add(variant);

        productRepository.save(product);
    }
}