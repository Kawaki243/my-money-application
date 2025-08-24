package com.project.moneymanagerbackend.services;

import com.project.moneymanagerbackend.dto.IncomeDTO;
import com.project.moneymanagerbackend.entities.CategoryEntity;
import com.project.moneymanagerbackend.entities.IncomeEntity;
import com.project.moneymanagerbackend.entities.ProfileEntity;
import com.project.moneymanagerbackend.repositories.CategoryRepository;
import com.project.moneymanagerbackend.repositories.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


/**
 * Service class for handling income-related operations such as
 * creating, fetching, deleting, filtering, and aggregating incomes.
 */
@Service
@RequiredArgsConstructor
public class IncomeService {

    /** Repository for interacting with categories */
    private final CategoryRepository categoryRepository;

    /** Repository for interacting with incomes */
    private final IncomeRepository incomeRepository;

    /** Service to retrieve the currently authenticated profile */
    private final ProfileService profileService;

    /**
     * Adds a new income for the current user.
     *
     * @param dto data transfer object containing income details
     * @return the saved income as a DTO
     */
    public IncomeDTO addIncome(IncomeDTO dto) {
        ProfileEntity profile = profileService.getCurrentProfile();                       /** Get current profile */
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())        /** Find category */
                .orElseThrow(() -> new RuntimeException("Category not found"));           /** Throw if missing */
        IncomeEntity newExpense = toEntity(dto, profile, category);                       /** Convert DTO to entity */
        newExpense = incomeRepository.save(newExpense);                                   /** Save income */
        return toDTO(newExpense);                                                         /** Convert entity to DTO */
    }

    /**
     * Retrieves all incomes for the current month for the logged-in user.
     *
     * @return list of current month's incomes as DTOs
     */
    public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();                       /** Get current profile */
        LocalDate now = LocalDate.now();                                                  /** Today's date */
        LocalDate startDate = now.withDayOfMonth(1);                                      /** Start of current month */
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());                      /** End of current month */
        List<IncomeEntity> list =
                incomeRepository.findByProfileIdAndDateBetween(
                        profile.getId(), startDate, endDate);                             /** Query incomes */
        return list.stream().map(this::toDTO).toList();                                   /** Convert to DTO list */
    }

    /**
     * Deletes an income if it belongs to the current user.
     *
     * @param incomeId the ID of the income to delete
     */
    public void deleteIncome(Long incomeId) {
        ProfileEntity profile = profileService.getCurrentProfile();                       /** Get current profile */
        IncomeEntity entity = incomeRepository.findById(incomeId)                         /** Find income */
                .orElseThrow(() -> new RuntimeException("Income not found"));             /** Throw if missing */
        if (!entity.getProfile().getId().equals(profile.getId())) {                       /** Ownership check */
            throw new RuntimeException("Unauthorized to delete this income");             /** Deny if not owner */
        }
        incomeRepository.delete(entity);                                                  /** Delete if authorized */
    }

    /**
     * Fetches the latest 5 incomes for the current user.
     *
     * @return list of latest 5 incomes as DTOs
     */
    public List<IncomeDTO> getLatest5IncomesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();                       /** Get current profile */
        List<IncomeEntity> list =
                incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());     /** Query top 5 incomes */
        return list.stream().map(this::toDTO).toList();                                   /** Convert to DTO list */
    }

    /**
     * Computes the total incomes for the current user.
     *
     * @return total income as BigDecimal (defaults to ZERO if null)
     */
    public BigDecimal getTotalIncomeForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();                       /** Get current profile */
        BigDecimal total =
                incomeRepository.findTotalExpenseByProfileId(profile.getId());            /** Aggregate query */
        return total != null ? total : BigDecimal.ZERO;                                   /** Return safe value */
    }

    /**
     * Filters incomes based on date range, keyword, and sort order.
     *
     * @param startDate start of the date range
     * @param endDate end of the date range
     * @param keyword keyword to search income names
     * @param sort sorting criteria
     * @return filtered list of incomes as DTOs
     */
    public List<IncomeDTO> filterIncomes(LocalDate startDate,
                                         LocalDate endDate,
                                         String keyword,
                                         Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();                       /** Get current profile */
        List<IncomeEntity> list =
                incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                        profile.getId(), startDate, endDate, keyword, sort);              /** Query with filters */
        return list.stream().map(this::toDTO).toList();                                   /** Convert to DTO list */
    }

    /**
     * Converts an IncomeDTO into an IncomeEntity for persistence.
     *
     * @param dto DTO containing income details
     * @param profile profile entity the income belongs to
     * @param category category entity associated with the income
     * @return an IncomeEntity ready to save
     */
    private IncomeEntity toEntity(IncomeDTO dto,
                                  ProfileEntity profile,
                                  CategoryEntity category) {
        return IncomeEntity.builder()
                .name(dto.getName())                                                      /** Set income name */
                .icon(dto.getIcon())                                                      /** Set icon */
                .amount(dto.getAmount())                                                  /** Set amount */
                .date(dto.getDate())                                                      /** Set income date */
                .profile(profile)                                                         /** Associate profile */
                .category(category)                                                       /** Associate category */
                .build();
    }

    /**
     * Converts an IncomeEntity into an IncomeDTO for API responses.
     *
     * @param entity income entity retrieved from DB
     * @return DTO representation of the income
     */
    private IncomeDTO toDTO(IncomeEntity entity) {
        return IncomeDTO.builder()
                .id(entity.getId())                                                       /** Map ID */
                .name(entity.getName())                                                   /** Map name */
                .icon(entity.getIcon())                                                   /** Map icon */
                .categoryId(entity.getCategory() != null ?
                        entity.getCategory().getId() : null)                          /** Map category ID */
                .categoryName(entity.getCategory() != null ?
                        entity.getCategory().getName() : "N/A")                     /** Map category name */
                .amount(entity.getAmount())                                               /** Map amount */
                .date(entity.getDate())                                                   /** Map date */
                .createdAt(entity.getCreatedAt())                                         /** Map creation timestamp */
                .updatedAt(entity.getUpdatedAt())                                         /** Map update timestamp */
                .build();
    }
}


