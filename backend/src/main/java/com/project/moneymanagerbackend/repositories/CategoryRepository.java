package com.project.moneymanagerbackend.repositories;

import com.project.moneymanagerbackend.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link CategoryEntity} persistence operations.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and adds custom
 * query methods for working with categories by profile or type.
 * </p>
 */
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    /**
     * Finds all categories associated with a given profile.
     *
     * Equivalent SQL: {@code SELECT * FROM tbl_categories WHERE profile_id = ?}
     *
     * @param profileId the ID of the profile
     * @return list of categories owned by the profile
     */
    List<CategoryEntity> findByProfileId(Long profileId);

    /**
     * Finds a category by its ID and profile ID.
     *
     * Equivalent SQL: {@code SELECT * FROM tbl_categories WHERE id = ? AND profile_id = ?}
     *
     * @param id        the category ID
     * @param profileId the profile ID
     * @return an {@link Optional} containing the category if found
     */
    Optional<CategoryEntity> findByIdAndProfileId(Long id, Long profileId);

    /**
     * Finds all categories of a given type for a specific profile.
     *
     * Equivalent SQL: {@code SELECT * FROM tbl_categories WHERE type = ? AND profile_id = ?}
     *
     * @param type      the category type (e.g., "income", "expense")
     * @param profileId the profile ID
     * @return list of categories matching the type and profile
     */
    List<CategoryEntity> findByTypeAndProfileId(String type, Long profileId);

    /**
     * Checks if a category with a given name exists for a specific profile.
     *
     * Equivalent SQL: {@code SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
     *                  FROM tbl_categories WHERE name = ? AND profile_id = ?}
     *
     * @param name      the category name
     * @param profileId the profile ID
     * @return {@code true} if a category with the given name exists for the profile,
     *         otherwise {@code false}
     */
    Boolean existsByNameAndProfileId(String name, Long profileId);
}

