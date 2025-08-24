package com.project.moneymanagerbackend.repositories;

import com.project.moneymanagerbackend.entities.ExpenseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link ExpenseEntity} persistence operations.
 *
 * <p>Extends {@link JpaRepository} to provide basic CRUD operations and
 * defines custom query methods for retrieving and calculating expenses
 * associated with a specific user (ProfileEntity).</p>
 */
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    /**
     * Retrieves all expenses for a given profile, ordered by date in descending order.
     *
     * SQL Equivalent:
     * <pre>
     *     SELECT *
     *     FROM tbl_expenses
     *     WHERE profile_id = ?
     *     ORDER BY date DESC;
     * </pre>
     *
     * @param profileId the ID of the profile
     * @return a list of {@link ExpenseEntity} sorted by date (newest first)
     */
    List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileId);

    /**
     * Retrieves the top 5 most recent expenses for a given profile.
     *
     * SQL Equivalent:
     * <pre>
     *     SELECT *
     *     FROM tbl_expenses
     *     WHERE profile_id = ?
     *     ORDER BY date DESC
     *     LIMIT 5;
     * </pre>
     *
     * @param profileId the ID of the profile
     * @return a list containing up to 5 most recent {@link ExpenseEntity}
     */
    List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    /**
     * Calculates the total sum of expenses for a given profile.
     *
     * SQL Equivalent:
     * <pre>
     *     SELECT SUM(amount)
     *     FROM tbl_expenses
     *     WHERE profile_id = ?;
     * </pre>
     *
     * @param profileId the ID of the profile
     * @return the total expense amount as {@link BigDecimal},
     *         or {@code null} if no expenses exist for the profile
     */
    @Query("SELECT SUM(ex.amount) FROM ExpenseEntity ex WHERE ex.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    /**
     * Retrieves expenses for a given profile within a date range, filtered by a keyword in the expense name,
     * and ordered by the given {@link Sort} parameter.
     *
     * SQL Equivalent:
     * <pre>
     *     SELECT *
     *     FROM tbl_expenses
     *     WHERE profile_id = ?1
     *       AND date BETWEEN ?2 AND ?3
     *       AND name LIKE %?4%
     *     ORDER BY ?5;
     * </pre>
     *
     * @param profileId the ID of the profile
     * @param startDate the start date of the range (inclusive)
     * @param endDate the end date of the range (inclusive)
     * @param keyword a substring to search for in the expense name (case-insensitive)
     * @param sort sorting instructions (e.g., by date or amount)
     * @return a list of matching {@link ExpenseEntity}
     */
    List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );

    /**
     * Retrieves all expenses for a given profile within a specific date range.
     *
     * SQL Equivalent:
     * <pre>
     *     SELECT *
     *     FROM tbl_expenses
     *     WHERE profile_id = ?
     *       AND date BETWEEN ? AND ?;
     * </pre>
     *
     * @param profileId the ID of the profile
     * @param startDate the start date of the range (inclusive)
     * @param endDate   the end date of the range (inclusive)
     * @return a list of {@link ExpenseEntity} within the specified date range
     */
    List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);


    /**
     * Finds all expenses for a given profile on a specific date.
     *
     * <p>This method will return a list of {@link ExpenseEntity} objects
     * that belong to the specified profile and were recorded exactly on the given date.</p>
     *
     * <p><b>SQL equivalent:</b></p>
     * <pre>
     *     SELECT e.*
     *     FROM expense e
     *     WHERE e.profile_id = ?1
     *       AND e.date = ?2;
     * </pre>
     *
     * @param profileId the ID of the profile (foreign key to identify user’s expenses)
     * @param date the specific date to filter expenses by
     * @return a list of expenses matching the profile ID and date
     */
    List<ExpenseEntity> findByProfileIdAndDate(Long profileId, LocalDate date);

}

