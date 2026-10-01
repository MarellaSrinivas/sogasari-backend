package com.sogasari.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sogasari.entity.Category;
import com.sogasari.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryDataInitializer
        implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {

        createCategories();
    }

    private void createCategories() {

        createCategory(
                "Kalamkari",
                "kalamkari",
                List.of(
                        "Kalamkari Sarees",
                        "Kalamkari Dresses",
                        "Kalamkari Sets",
                        "Kalamkari Dupattas",
                        "Kalamkari Collections"
                )
        );

        createCategory(
                "Sarees",
                "sarees",
                List.of(
                        "Kalamkari Sarees",
                        "Silk Sarees",
                        "Banaras Sarees",
                        "Designer Sarees"
                )
        );

        createCategory(
                "Weaves & Crafts",
                "weaves-crafts",
                List.of(
                        "Handloom Sarees",
                        "Cotton Sarees",
                        "Linen Sarees"
                )
        );

        createCategory(
                "Salwar Sets",
                "salwar-sets",
                List.of()
        );

        createCategory(
                "Lehenga",
                "lehenga",
                List.of()
        );

        createCategory(
                "Fusion Wear",
                "fusion-wear",
                List.of()
        );

        createCategory(
                "Mens",
                "mens",
                List.of()
        );
    }

    private void createCategory(
            String name,
            String slug,
            List<String> children
    ) {

        Category parent =
                categoryRepository
                        .findBySlug(slug)
                        .orElseGet(() -> {

                            Category category =
                                    Category.builder()
                                            .name(name)
                                            .slug(slug)
                                            .active(true)
                                            .build();

                            return categoryRepository
                                    .save(category);
                        });

        for (String childName : children) {

            String childSlug =
                    childName
                            .toLowerCase()
                            .replaceAll(
                                    "[^a-z0-9]+",
                                    "-"
                            )
                            .replaceAll(
                                    "^-|-$",
                                    ""
                            );

            if (!categoryRepository
                    .existsBySlug(childSlug)) {

                Category child =
                        Category.builder()
                                .name(childName)
                                .slug(childSlug)
                                .parent(parent)
                                .active(true)
                                .build();

                categoryRepository.save(child);
            }
        }
    }
}