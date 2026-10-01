package com.sogasari.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sogasari.config.FileStorageProperties;
import com.sogasari.dto.response.ProductImageResponse;
import com.sogasari.entity.Product;
import com.sogasari.entity.ProductImage;
import com.sogasari.repository.ProductImageRepository;
import com.sogasari.repository.ProductRepository;
import com.sogasari.service.ProductImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl
        implements ProductImageService {

    private final ProductRepository productRepository;

    private final ProductImageRepository productImageRepository;

    private final FileStorageProperties fileStorageProperties;


    @Override
    @Transactional
    public List<ProductImageResponse> uploadImages(
            Long productId,
            List<MultipartFile> files
    ) {

        if (
                files == null
                        ||
                files.isEmpty()
        ) {

            throw new RuntimeException(
                    "Please select at least one image"
            );
        }


        Product product =
                productRepository
                        .findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found with id: "
                                                + productId
                                )
                        );


        /*
         * Product-specific directory
         */

        Path productDirectory =
                Paths.get(
                        fileStorageProperties
                                .getUploadDir(),
                        "products",
                        String.valueOf(productId)
                );


        try {

            Files.createDirectories(
                    productDirectory
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to create image directory",
                    e
            );
        }


        /*
         * Existing images
         */

        int existingCount =
                productImageRepository
                        .countByProductId(
                                productId
                        );


        List<ProductImageResponse> responses =
                new ArrayList<>();


        for (
                int i = 0;
                i < files.size();
                i++
        ) {

            MultipartFile file =
                    files.get(i);


            if (
                    file == null
                            ||
                    file.isEmpty()
            ) {

                continue;
            }


            validateImage(file);


            int displayOrder =
                    existingCount + i;


            boolean primaryImage =
                    existingCount == 0
                            &&
                    i == 0;


            String extension =
                    getExtension(
                            file.getOriginalFilename()
                    );


            String fileName;


            if (primaryImage) {

                fileName =
                        "product-"
                                + productId
                                + "-main"
                                + extension;

            } else {

                fileName =
                        "product-"
                                + productId
                                + "-"
                                + displayOrder
                                + extension;
            }


            Path target =
                    productDirectory
                            .resolve(fileName);


            try {

                Files.copy(
                        file.getInputStream(),
                        target,
                        StandardCopyOption.REPLACE_EXISTING
                );

            } catch (IOException e) {

                throw new RuntimeException(
                        "Unable to save image: "
                                + fileName,
                        e
                );
            }


            String imageUrl =
                    "/images/products/"
                            + productId
                            + "/"
                            + fileName;


            ProductImage image =
                    ProductImage.builder()

                            .product(product)

                            .imageUrl(
                                    imageUrl
                            )

                            .displayOrder(
                                    displayOrder
                            )

                            .primaryImage(
                                    primaryImage
                            )

                            .build();


            ProductImage saved =
                    productImageRepository.save(
                            image
                    );


            responses.add(
                    ProductImageResponse.builder()

                            .id(
                                    saved.getId()
                            )

                            .imageUrl(
                                    saved.getImageUrl()
                            )

                            .displayOrder(
                                    saved.getDisplayOrder()
                            )

                            .primaryImage(
                                    saved.getPrimaryImage()
                            )

                            .build()
            );
        }


        return responses;
    }


    private void validateImage(
            MultipartFile file
    ) {

        String contentType =
                file.getContentType();


        if (
                contentType == null
                        ||
                !(
                        contentType.equals(
                                "image/jpeg"
                        )
                        ||
                        contentType.equals(
                                "image/png"
                        )
                        ||
                        contentType.equals(
                                "image/webp"
                        )
                )
        ) {

            throw new RuntimeException(
                    "Only JPG, PNG and WEBP images are allowed"
            );
        }
    }


    private String getExtension(
            String fileName
    ) {

        if (
                fileName == null
                        ||
                !fileName.contains(".")
        ) {

            return ".jpg";
        }


        return fileName.substring(
                fileName.lastIndexOf(".")
        ).toLowerCase();
    }
}