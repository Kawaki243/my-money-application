package com.project.moneymanagerbackend.controllers;

import com.project.moneymanagerbackend.dto.CategoryDTO;
import com.project.moneymanagerbackend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing categories.
 * <p>
 * Provides endpoints for creating and retrieving categories
 * associated with the currently authenticated user.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Creates a new category for the currently authenticated user.
     *
     * @param categoryDTO the category details provided in the request body
     * @return a {@link ResponseEntity} containing the created {@link CategoryDTO}
     *         with HTTP status {@code 201 Created}
     */
    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO saveCategoryDTO = categoryService.saveCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveCategoryDTO);
    }

    /**
     * Retrieves all categories for the currently authenticated user.
     *
     * @return a {@link ResponseEntity} containing the list of {@link CategoryDTO}
     *         with HTTP status {@code 200 OK}
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategoriesForCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(categoryDTOList);
    }

    /**
     * Endpoint to retrieve all categories of a given type for the currently authenticated user.
     *
     * <p>Example: {@code GET /categories/expense} will return all categories
     * of type "expense" that belong to the current user.</p>
     *
     * @param type the category type to filter by (e.g., "income", "expense")
     * @return a {@link ResponseEntity} containing a list of {@link CategoryDTO}
     *         objects that match the specified type and belong to the current user,
     *         with HTTP status 200 (OK)
     */
    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDTO>> getAllCategoriesByTypeForCurrentUser(@PathVariable String type) {
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategoriesByTypeForCurrentUser(type);
        return ResponseEntity.status(HttpStatus.OK).body(categoryDTOList);
    }

    /**
     * Updates an existing category for the currently authenticated user.
     *
     * <p>This endpoint allows the logged-in user to update a category they own
     * by providing its ID in the path and the new details in the request body.
     * If the category is not found or does not belong to the current user,
     * an exception is thrown by the service layer.</p>
     *
     * @param categoryId the ID of the category to update (from the URL path)
     * @param categoryDTO the updated category details (name, icon, type)
     * @return a {@link ResponseEntity} containing the updated {@link CategoryDTO} with HTTP 200 (OK)
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryDTO categoryDTO
    ) {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }


}
