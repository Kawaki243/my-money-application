package com.project.moneymanagerbackend.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity class representing a Category.
 * <p>
 * Each category belongs to a specific user profile and stores metadata such as
 * name, icon, type, and timestamps for creation and updates.
 * </p>
 *
 * <ul>
 *     <li>Maps to the database table <b>tbl_categories</b></li>
 *     <li>Supports automatic timestamps for creation and update events</li>
 *     <li>Each category is linked to a {@link ProfileEntity}</li>
 * </ul>
 */
@Entity
@Table(name = "tbl_categories")
@Data // Lombok: generates getters, setters, equals, hashCode, and toString
@AllArgsConstructor // Lombok: generates a constructor with all fields
@NoArgsConstructor  // Lombok: generates a no-argument constructor
@Builder            // Lombok: enables builder pattern for this class
public class CategoryEntity {

    /** Unique identifier for the category (Primary Key, auto-incremented). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name of the category (e.g., "Food", "Travel"). */
    private String name;

    /** Timestamp when the category was created (set automatically, not updatable). */
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    /** Timestamp when the category was last updated (set automatically). */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /** Icon representation for the category (optional, for UI display). */
    private String icon;

    /** The type of category (could be income or expense). */
    private String type;

    /**
     * The user profile that owns this category.
     * <p>
     * This is a Many-to-One relationship where many categories can belong to one profile.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private ProfileEntity profile;
}

