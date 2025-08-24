package com.project.moneymanagerbackend.services;


import com.project.moneymanagerbackend.dto.ExpenseDTO;
import com.project.moneymanagerbackend.entities.CategoryEntity;
import com.project.moneymanagerbackend.entities.ExpenseEntity;
import com.project.moneymanagerbackend.entities.ProfileEntity;
import com.project.moneymanagerbackend.repositories.CategoryRepository;
import com.project.moneymanagerbackend.repositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service class for handling Expense-related operations such as
 * creating, fetching, deleting, and aggregating expenses.
 */
@Service
@RequiredArgsConstructor
public class ExpenseService {

    /** Repository for interacting with categories */
    private final CategoryRepository categoryRepository;

    /** Repository for interacting with expenses */
    private final ExpenseRepository expenseRepository;

    /** Service to retrieve the currently authenticated profile */
    private final ProfileService profileService;

    /**
     * Converts an ExpenseDTO into an ExpenseEntity for persistence.
     *
     * @param expenseDTO data transfer object containing expense details
     * @param profileEntity the profile to which the expense belongs
     * @param categoryEntity the category under which the expense falls
     * @return an ExpenseEntity ready for persistence
     */
    private ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity) {
        return ExpenseEntity.builder()
                .id(expenseDTO.getId())               /** Copy expense ID if provided (for updates) */
                .name(expenseDTO.getName())           /** Set expense name */
                .icon(expenseDTO.getIcon())           /** Set associated icon */
                .amount(expenseDTO.getAmount())       /** Set expense amount */
                .date(expenseDTO.getDate())           /** Set expense date */
                .profile(profileEntity)               /** Associate with current profile */
                .category(categoryEntity)             /** Associate with selected category */
                .build();
    }

    /**
     * Converts an ExpenseEntity into an ExpenseDTO for API responses.
     *
     * @param expenseEntity entity retrieved from the database
     * @return an ExpenseDTO containing the mapped fields
     */
    private ExpenseDTO toDTO(ExpenseEntity expenseEntity) {
        return ExpenseDTO.builder()
                .id(expenseEntity.getId())                                            /** Map ID */
                .name(expenseEntity.getName())                                        /** Map name */
                .icon(expenseEntity.getIcon())                                        /** Map icon */
                .categoryId(expenseEntity.getCategory() != null ?
                        expenseEntity.getCategory().getId() : null)               /** Map category ID */
                .categoryName(expenseEntity.getCategory() != null ?
                        expenseEntity.getCategory().getName() : "N/A")          /** Map category name or fallback */
                .amount(expenseEntity.getAmount())                                    /** Map amount */
                .date(expenseEntity.getDate())                                        /** Map expense date */
                .createdAt(expenseEntity.getCreatedAt())                              /** Map creation timestamp */
                .updatedAt(expenseEntity.getUpdatedAt())                              /** Map update timestamp */
                .build();
    }

    /**
     * Adds a new expense for the current user.
     *
     * @param expenseDTO data transfer object containing expense details
     * @return the saved expense as a DTO
     */
    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {
        ProfileEntity profileEntity = this.profileService.getCurrentProfile();   /** Get current profile */
        CategoryEntity categoryEntity = this.categoryRepository
                .findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category Not Found"));  /** Validate category */
        ExpenseEntity expenseEntity = this.toEntity(expenseDTO, profileEntity, categoryEntity); /** Convert to entity */
        expenseEntity = expenseRepository.save(expenseEntity);                   /** Persist expense */
        return toDTO(expenseEntity);                                             /** Return as DTO */
    }

    /**
     * Retrieves all expenses for the current user in the current month.
     *
     * @return list of current month's expenses as DTOs
     */
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser() {
        ProfileEntity profileEntity = this.profileService.getCurrentProfile();   /** Get current profile */
        LocalDate now = LocalDate.now();                                         /** Current date */
        LocalDate startDate = now.withDayOfMonth(1);                             /** First day of month */
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());             /** Last day of month */
        List<ExpenseEntity> expenseEntityList =
                expenseRepository.findByProfileIdAndDateBetween(
                        profileEntity.getId(), startDate, endDate);              /** Query expenses */
        return expenseEntityList.stream().map(this::toDTO).toList();             /** Convert to DTO list */
    }

    /**
     * Deletes an expense if it belongs to the current user.
     *
     * @param expenseId the ID of the expense to delete
     */
    public void deleteExpense(Long expenseId) {
        ProfileEntity profileEntity = this.profileService.getCurrentProfile();   /** Get current profile */
        ExpenseEntity expenseEntity = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense Not Found"));   /** Find expense */
        if (expenseEntity.getProfile().getId().equals(profileEntity.getId())) {  /** Ownership check */
            expenseRepository.delete(expenseEntity);                             /** Delete if owned */
        } else {
            throw new RuntimeException("Unauthorized to delete this Expense !"); /** Otherwise deny */
        }
    }

    /**
     * Fetches the 5 most recent expenses for the current user.
     *
     * @return list of latest 5 expenses as DTOs
     */
    public List<ExpenseDTO> getLastest5ExpensesForCurrentUser() {
        ProfileEntity profileEntity = this.profileService.getCurrentProfile();   /** Get current profile */
        List<ExpenseEntity> expenseEntityList =
                expenseRepository.findTop5ByProfileIdOrderByDateDesc(profileEntity.getId()); /** Query last 5 */
        return expenseEntityList.stream().map(this::toDTO).toList();             /** Convert to DTO list */
    }

    /**
     * Computes the total expenses for the current user.
     *
     * @return total expenses as BigDecimal (defaults to ZERO if null)
     */
    public BigDecimal getTotalExpensesForCurrentUser() {
        ProfileEntity profileEntity = this.profileService.getCurrentProfile();   /** Get current profile */
        BigDecimal totalExpenses =
                this.expenseRepository.findTotalExpenseByProfileId(profileEntity.getId()); /** Aggregate query */
        return totalExpenses != null ? totalExpenses : BigDecimal.ZERO;          /** Return safe value */
    }

    /**
     * Filters expenses for the current user based on:
     * <ul>
     *   <li>Date range (between startDate and endDate)</li>
     *   <li>Keyword (case-insensitive match against expense name)</li>
     *   <li>Sort order (provided dynamically using Spring's {@link Sort})</li>
     * </ul>
     *
     * @param startDate the start of the date range (inclusive)
     * @param endDate the end of the date range (inclusive)
     * @param keyword keyword to search for in the expense name (case-insensitive)
     * @param sort sort order (e.g., by date descending, amount ascending, etc.)
     * @return list of matching expenses as DTOs
     */
    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        // Get the currently authenticated profile
        ProfileEntity profileEntity = this.profileService.getCurrentProfile();

        // Query repository: find all expenses for this profile within date range,
        // where the name contains the keyword (ignoring case),
        // and results are ordered according to the provided Sort.
        List<ExpenseEntity> expenseEntityList =
                this.expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                        profileEntity.getId(),
                        startDate,
                        endDate,
                        keyword,
                        sort
                );

        // Convert list of entities into list of DTOs and return
        return expenseEntityList.stream().map(this::toDTO).toList();
    }

    // Notifications
    public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId, LocalDate date){
        List<ExpenseEntity> expenseEntityList = this.expenseRepository.findByProfileIdAndDate(profileId, date);
        return expenseEntityList.stream().map(this::toDTO).toList();
    }
}

