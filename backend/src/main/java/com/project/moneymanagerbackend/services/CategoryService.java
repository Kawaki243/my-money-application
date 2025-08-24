package com.project.moneymanagerbackend.services;

import com.project.moneymanagerbackend.dto.CategoryDTO;
import com.project.moneymanagerbackend.entities.CategoryEntity;
import com.project.moneymanagerbackend.entities.ProfileEntity;
import com.project.moneymanagerbackend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing categories.
 * <p>
 * Provides business logic for creating and mapping categories to and from DTOs.
 * Works in collaboration with {@link ProfileService} and {@link CategoryRepository}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;

    /**
     * Saves a new category for the currently authenticated profile.
     * <p>
     * Before saving, it checks if a category with the same name already exists
     * for the profile. If it does, a {@link ResponseStatusException} with
     * {@link HttpStatus#CONFLICT} is thrown.
     * </p>
     *
     * @param categoryDTO the DTO containing category details
     * @return the saved category as a DTO
     * @throws ResponseStatusException if a duplicate category name exists
     */
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();

        // Check for duplicate category name within the same profile
        if (categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), profile.getId())) {
            throw new RuntimeException( "Category with the name: " + categoryDTO.getName() + " already exists" );
        }

        // Convert DTO to entity, save it, and return the DTO response
        CategoryEntity newCategoryEntity = toEntity(categoryDTO, profile);
        newCategoryEntity = categoryRepository.save(newCategoryEntity);
        return toDTO(newCategoryEntity);
    }

    /**
     * Converts a {@link CategoryDTO} into a {@link CategoryEntity}.
     *
     * @param categoryDTO   the category DTO to convert
     * @param profileEntity the associated profile entity
     * @return a new {@link CategoryEntity} instance
     */
    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profileEntity) {
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profileEntity)
                .type(categoryDTO.getType())
                .build();
    }

    /**
     * Retrieves all categories associated with the currently authenticated user.
     * <p>
     * This method fetches the current user's profile using {@link ProfileService#getCurrentProfile()},
     * then queries the {@link CategoryRepository} to find all categories belonging
     * to that profile. The resulting list of {@link CategoryEntity} objects is mapped
     * into a list of {@link CategoryDTO}.
     * </p>
     *
     * @return a list of {@link CategoryDTO} representing the categories of the current user
     */
    public List<CategoryDTO> getAllCategoriesForCurrentUser() {
        // Get the currently logged-in profile from the security context
        ProfileEntity profile = profileService.getCurrentProfile();

        // Fetch all categories belonging to this profile from the database
        List<CategoryEntity> categoryEntityList = categoryRepository.findByProfileId(profile.getId());

        // Convert entities to DTOs for response
        return categoryEntityList.stream().map(this::toDTO).toList();
    }


    /**
     * Converts a {@link CategoryEntity} into a {@link CategoryDTO}.
     *
     * @param categoryEntity the category entity to convert
     * @return a new {@link CategoryDTO} instance
     */
    private CategoryDTO toDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
                .name(categoryEntity.getName())
                .icon(categoryEntity.getIcon())
                .type(categoryEntity.getType())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .build();
    }

    /**
     * Retrieves all categories of a given type for the currently authenticated user.
     *
     * @param type the category type to filter by (e.g., "income", "expense")
     * @return a list of {@link CategoryDTO} objects that belong to the current user
     *         and match the specified type
     */
    public List<CategoryDTO> getAllCategoriesByTypeForCurrentUser(String type) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categoryEntityList = categoryRepository.findByTypeAndProfileId(type, profile.getId());
        return categoryEntityList.stream().map(this::toDTO).toList();
    }

    /**
     * Updates an existing category for the currently authenticated user.
     *
     * <p>This method ensures that the category belongs to the logged-in user
     * before updating it. If the category with the given ID is not found for
     * the current user's profile, a {@link ResponseStatusException} with
     * {@link HttpStatus#NOT_FOUND} is thrown.</p>
     *
     * @param categoryId the ID of the category to update
     * @param categoryDTO the new category data to update (name, icon, type)
     * @return the updated {@link CategoryDTO} after saving to the database
     * @throws ResponseStatusException if the category does not exist or does not belong to the current user
     */
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id: " + categoryId + " not found"));
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setIcon(categoryDTO.getIcon());
        existingCategory.setType(categoryDTO.getType());
        existingCategory = categoryRepository.save(existingCategory);
        return toDTO(existingCategory);
    }


}

