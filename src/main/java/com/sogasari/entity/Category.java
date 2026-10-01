// package com.sogasari.entity;

// import java.time.LocalDateTime;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.PrePersist;
// import jakarta.persistence.PreUpdate;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// @Entity
// @Table(name = "categories")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class Category {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(nullable = false, unique = true)
//     private String name;

//     @Column(nullable = false, unique = true)
//     private String slug;

//     @Column(length = 1000)
//     private String description;

//     private String image;

//     @Builder.Default
//     private Boolean active = true;

//     @Column(name = "created_at")
//     private LocalDateTime createdAt;

//     @Column(name = "updated_at")
//     private LocalDateTime updatedAt;

//     @PrePersist
//     protected void onCreate() {
//         createdAt = LocalDateTime.now();
//         updatedAt = LocalDateTime.now();
//     }

//     @PreUpdate
//     protected void onUpdate() {
//         updatedAt = LocalDateTime.now();
//     }
// }



package com.sogasari.entity;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

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

    @Column(length = 1000)
    private String description;

    private String image;

    // =========================
    // PARENT CATEGORY
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // =========================
    // CHILD CATEGORIES
    // =========================

    @OneToMany(
            mappedBy = "parent",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Category> children = new ArrayList<>();

    // =========================
    // STATUS
    // =========================

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

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