package com.sogasari.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(
                        name = "idx_product_slug",
                        columnList = "slug"
                ),
                @Index(
                        name = "idx_product_active",
                        columnList = "active"
                ),
                @Index(
                        name = "idx_product_new_arrival",
                        columnList = "new_arrival"
                ),
                @Index(
                        name = "idx_product_best_seller",
                        columnList = "best_seller"
                ),
                @Index(
                        name = "idx_product_featured",
                        columnList = "featured"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // BASIC INFORMATION
    // =========================

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, unique = true)
    private String sku;

    // =========================
    // CATEGORY
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Category category;

    // =========================
    // DESCRIPTION
    // =========================

    @Column(length = 1000)
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    // =========================
    // PRICE
    // =========================

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal price;

    @Column(
            precision = 12,
            scale = 2
    )
    private BigDecimal originalPrice;

    private Integer discount;

    // =========================
    // PRODUCT CARD
    // =========================

    private String badge;

    // =========================
    // INVENTORY
    // =========================

    @Builder.Default
    @Column(
            nullable = false
    )
    private Integer stock = 0;

    // =========================
    // PRODUCT FLAGS
    // =========================

    @Builder.Default
    @Column(
            nullable = false
    )
    private Boolean featured = false;

    @Builder.Default
    @Column(
            name = "best_seller",
            nullable = false
    )
    private Boolean bestSeller = false;

    @Builder.Default
    @Column(
            name = "new_arrival",
            nullable = false
    )
    private Boolean newArrival = false;

    @Builder.Default
    @Column(
            nullable = false
    )
    private Boolean active = true;

    // =========================
    // PRODUCT IMAGES
    // =========================

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    // =========================
    // PRODUCT VARIANTS
    // =========================

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ProductVariant> variants = new ArrayList<>();

    // =========================
    // DATES
    // =========================

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =========================
    // JPA CALLBACKS
    // =========================

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }

  
}