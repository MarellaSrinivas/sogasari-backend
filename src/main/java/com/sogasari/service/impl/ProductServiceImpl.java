package com.sogasari.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sogasari.dto.request.AdminProductCreateRequest;
 import com.sogasari.dto.response.ProductImageResponse;
import com.sogasari.dto.response.ProductResponse;
import com.sogasari.dto.response.ProductVariantResponse;
import com.sogasari.entity.Category;
import com.sogasari.entity.Product;
import com.sogasari.entity.ProductImage;
import com.sogasari.entity.ProductVariant;
import com.sogasari.repository.CategoryRepository;
import com.sogasari.repository.ProductRepository;
import com.sogasari.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl
        implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final String uploadDirectory = "uploads/products";
 
    @Override
    public List<ProductResponse> getAllProducts() {

        return productRepository
                .findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductBySlug(
            String slug
    ) {

        Product product =
                productRepository
                        .findBySlug(slug)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                )
                        );

        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getProductsByCategory(
            String slug
    ) {

        return productRepository
                .findByCategorySlugAndActiveTrue(slug)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getNewArrivals() {

        return productRepository
                .findByActiveTrueAndNewArrivalTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getBestSellers() {

        return productRepository
                .findByActiveTrueAndBestSellerTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getFeaturedProducts() {

        return productRepository
                .findByActiveTrueAndFeaturedTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> searchProducts(
            String search
    ) {

        return productRepository
                .findByNameContainingIgnoreCaseAndActiveTrue(
                        search
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProductResponse mapToResponse(
            Product product
    ) {

        List<ProductImageResponse> images =
                product.getImages()
                        .stream()
                        .map(image ->
                                ProductImageResponse.builder()
                                        .id(image.getId())
                                        .imageUrl(image.getImageUrl())
                                        .displayOrder(
                                                image.getDisplayOrder()
                                        )
                                        .primaryImage(
                                                image.getPrimaryImage()
                                        )
                                        .build()
                        )
                        .toList();

        List<ProductVariantResponse> variants =
                product.getVariants()
                        .stream()
                        .map(variant ->
                                ProductVariantResponse.builder()
                                        .id(variant.getId())
                                        .colorName(
                                                variant.getColorName()
                                        )
                                        .colorCode(
                                                variant.getColorCode()
                                        )
                                        .size(
                                                variant.getSize()
                                        )
                                        .stock(
                                                variant.getStock()
                                        )
                                        .additionalPrice(
                                                variant.getAdditionalPrice()
                                        )
                                        .active(
                                                variant.getActive()
                                        )
                                        .build()
                        )
                        .toList();

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .sku(product.getSku())
                .categoryId(
                        product.getCategory().getId()
                )
                .categoryName(
                        product.getCategory().getName()
                )
                .categorySlug(
                        product.getCategory().getSlug()
                )
                .shortDescription(
                        product.getShortDescription()
                )
                .description(
                        product.getDescription()
                )
                .price(product.getPrice())
                .originalPrice(
                        product.getOriginalPrice()
                )
                .discount(
                        product.getDiscount()
                )
                .badge(product.getBadge())
                .stock(product.getStock())
                .featured(product.getFeatured())
                .bestSeller(product.getBestSeller())
                .newArrival(product.getNewArrival())
                .active(product.getActive())
                .images(images)
                .variants(variants)
                .build();
    }


    @Override
public ProductResponse getProductById(Long id) {

    Product product = productRepository
            .findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Product not found with id: " + id)
            );

    return mapToResponse(product);
}


// create product


@Override
@Transactional
public ProductResponse createProduct(
        AdminProductCreateRequest request
) {

    // =========================
    // CHECK SLUG
    // =========================

    if (productRepository.existsBySlug(
            request.getSlug()
    )) {

        throw new RuntimeException(
                "Product slug already exists"
        );
    }


    // =========================
    // CHECK SKU
    // =========================

    if (productRepository.existsBySku(
            request.getSku()
    )) {

        throw new RuntimeException(
                "Product SKU already exists"
        );
    }


    // =========================
    // FIND CATEGORY
    // =========================

    Category category =
        categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Category not found"
                        )
                );

 


    // =========================
    // CREATE PRODUCT
    // =========================

    Product product =
            Product.builder()

                    .name(
                            request.getName()
                    )

                    .slug(
                            request.getSlug()
                    )

                    .sku(
                            request.getSku()
                    )

                    .category(
                            category
                    )

                    .shortDescription(
                            request.getShortDescription()
                    )

                    .description(
                            request.getDescription()
                    )

                    .price(
                            request.getPrice()
                    )

                    .originalPrice(
                            request.getOriginalPrice()
                    )

                    .discount(
                            request.getDiscount()
                    )

                    .badge(
                            request.getBadge()
                    )

                    .stock(
                            request.getStock()
                    )

                    .featured(
                            Boolean.TRUE.equals(
                                    request.getFeatured()
                            )
                    )

                    .bestSeller(
                            Boolean.TRUE.equals(
                                    request.getBestSeller()
                            )
                    )

                    .newArrival(
                            Boolean.TRUE.equals(
                                    request.getNewArrival()
                            )
                    )
 

                    .active(
                            request.getActive() == null
                                    || request.getActive()
                    )

                    .build();


    // =========================
    // VARIANTS
    // =========================

    if (
            request.getVariants() != null
                    &&
            !request.getVariants().isEmpty()
    ) {

        request.getVariants()
                .forEach(
                        variantRequest -> {

                            ProductVariant variant =
                                    ProductVariant.builder()

                                            .product(
                                                    product
                                            )

                                            .colorName(
                                                    variantRequest
                                                            .getColorName()
                                            )

                                            .colorCode(
                                                    variantRequest
                                                            .getColorCode()
                                            )

                                            .size(
                                                    variantRequest
                                                            .getSize()
                                            )

                                            .stock(
                                                    variantRequest
                                                            .getStock()
                                            )

                                            .additionalPrice(
                                                    variantRequest
                                                            .getAdditionalPrice()
                                            )

                                            .active(
                                                    variantRequest
                                                            .getActive() == null
                                                            ||
                                                    variantRequest
                                                            .getActive()
                                            )

                                            .build();


                            product.getVariants()
                                    .add(variant);
                        }
                );
    }


    // =========================
    // SAVE
    // =========================

    Product savedProduct =
            productRepository.save(
                    product
            );


    // =========================
    // RESPONSE
    // =========================

    return mapToResponse(
            savedProduct
    );
}




@Override
@Transactional
public List<ProductImageResponse> uploadProductImages(
        Long productId,
        List<MultipartFile> images
) {

    Product product = productRepository
            .findById(productId)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Product not found with id: " + productId
                    )
            );

    if (images == null || images.isEmpty()) {
        throw new RuntimeException("No images provided");
    }

    try {

        // =========================
        // PRODUCT FOLDER
        // =========================

        Path productFolder = Paths.get(
                uploadDirectory,
                String.valueOf(productId)
        );

        Files.createDirectories(productFolder);


        // =========================
        // EXISTING IMAGE COUNT
        // =========================

        int existingImageCount =
                product.getImages().size();


        // =========================
        // UPLOAD IMAGES
        // =========================

        for (int i = 0; i < images.size(); i++) {

            MultipartFile file = images.get(i);

            if (file == null || file.isEmpty()) {
                continue;
            }


            // =========================
            // FILE TYPE CHECK
            // =========================

            String contentType =
                    file.getContentType();

            if (
                    contentType == null
                    ||
                    !contentType.startsWith("image/")
            ) {

                throw new RuntimeException(
                        "Only image files are allowed: "
                                + file.getOriginalFilename()
                );
            }


            // =========================
            // FILE NAME
            // =========================

            int imageNumber =
                    existingImageCount + i + 1;

            String extension = getFileExtension(
                    file.getOriginalFilename()
            );

            String fileName =
                    "product-"
                            + productId
                            + "-"
                            + imageNumber
                            + extension;


            // =========================
            // FILE PATH
            // =========================

            Path filePath =
                    productFolder.resolve(fileName);


            // =========================
            // SAVE FILE
            // =========================

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );


            // =========================
            // PRIMARY IMAGE
            // =========================

            boolean primaryImage =
                    product.getImages().isEmpty()
                            && i == 0;


            // =========================
            // PRODUCT IMAGE
            // =========================

            ProductImage productImage =
                    ProductImage.builder()

                            .product(product)

                            .imageUrl(
                                    "/uploads/products/"
                                            + productId
                                            + "/"
                                            + fileName
                            )

                            .displayOrder(
                                    existingImageCount + i
                            )

                            .primaryImage(
                                    primaryImage
                            )

                            .build();


            product.getImages()
                    .add(productImage);
        }


        // =========================
        // SAVE
        // =========================

        productRepository.save(product);


        // =========================
        // RESPONSE
        // =========================

        return product.getImages()
                .stream()
                .map(image ->
                        ProductImageResponse.builder()
                                .id(image.getId())
                                .imageUrl(image.getImageUrl())
                                .displayOrder(
                                        image.getDisplayOrder()
                                )
                                .primaryImage(
                                        image.getPrimaryImage()
                                )
                                .build()
                )
                .toList();


    } catch (IOException e) {

        throw new RuntimeException(
                "Failed to upload product images",
                e
        );
    }
}


private String getFileExtension(
        String fileName
) {

    if (fileName == null || !fileName.contains(".")) {
        return ".jpg";
    }

    return fileName.substring(
            fileName.lastIndexOf(".")
    ).toLowerCase();
}
}