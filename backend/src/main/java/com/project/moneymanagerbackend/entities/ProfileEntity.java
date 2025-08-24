package com.project.moneymanagerbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/* This class is the table of User's Profile */

/** Represent this class as a JPA Entity */
@Entity
/** Maps this entity class to the "tbl_profiles" table in the database */
@Table(name = "tbl_profiles")
/** Lombok annotation: generates getters, setters, toString, equals, and hashCode */
@Data
/** Lombok annotation: generates a constructor with all fields as parameters */
@AllArgsConstructor
/** Lombok annotation: generates a no-argument constructor */
@NoArgsConstructor
/** Lombok annotation: enables the Builder design pattern for this class */
@Builder
public class ProfileEntity {

    /**
     * Primary key of the profile entity.
     * Auto-generated using the database identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** User's full name. */
    private String fullName;

    /** User's email address, typically used for login and communication. */
    @Column(unique = true)
    private String email;

    /** Encrypted/hashed password for authentication. */
    private String password;

    /** URL pointing to the user's profile image. */
    private String profileImageUrl;

    /** Timestamp of when the profile was created. */
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    /** Timestamp of the last update to the profile. */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /** Indicates whether the profile is active (true = active, false = inactive). */
    private Boolean isActive;

    /** Token used for account activation (e.g., email verification). */
    private String activationToken;

    /**
     * Lifecycle callback executed before persisting a new entity.
     * Ensures that the profile is inactive by default if not explicitly set.
     */
    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = false;
        }
    }

}
