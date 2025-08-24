package com.project.moneymanagerbackend.repositories;

import com.project.moneymanagerbackend.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link ProfileEntity}.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and query methods
 * for the ProfileEntity class.
 */
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    /**
     * Retrieves a profile by its email.
     *
     * @param email the email of the profile to search for
     * @return an {@link Optional} containing the found {@link ProfileEntity},
     *         or an empty Optional if no profile is found
     */
    Optional<ProfileEntity> findByEmail(String email);

    /**
     * Retrieves a profile by its activationToken.
     *
     * @param activationToken the activationToken of the profile to search for
     * @return an {@link Optional} containing the found {@link ProfileEntity},
     *         or an empty Optional if no profile is found
     */
    // SELECT * FROM tbl_profiles WHERE activation_token = ?
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}

