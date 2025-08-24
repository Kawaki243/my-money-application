package com.project.moneymanagerbackend.repositories;

import com.project.moneymanagerbackend.entities.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity,Long> {

    /**
     * Retrieves all expenses for a given profile, ordered by date in descending order.
     *
     * SQL Equivalent:
     * <pre>
     *     SELECT *
     *     FROM tbl_incomes
     *     WHERE profile_id = ?
     *     ORDER BY date DESC;
     * </pre>
     *
     * @param profileId the ID of the profile
     * @return a list of {@link IncomeEntity} sorted by date (newest first)
     */
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    /**
     * Retrieves the top 5 most recent expenses for a given profile.
     *
     * SQL Equivalent:
     * <pre>
     *     SELECT *
     *     FROM tbl_incomes
     *     WHERE profile_id = ?
     *     ORDER BY date DESC
     *     LIMIT 5;
     * </pre>
     *
     * @param profileId the ID of the profile
     * @return a list containing up to 5 most recent {@link IncomeEntity}
     */
    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    /**
     * Calculates the total sum of expenses for a given profile.
     *
     * SQL Equivalent:
     * <pre>
     *     SELECT SUM(amount)
     *     FROM tbl_incomes
     *     WHERE profile_id = ?;
     * </pre>
     *
     * @param profileId the ID of the profile
     * @return the total expense amount as {@link BigDecimal},
     *         or {@code null} if no expenses exist for the profile
     */
    @Query("SELECT SUM(ie.amount) FROM IncomeEntity ie WHERE ie.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    /**
     * Retrieves expenses for a given profile within a date range, filtered by a keyword in the expense name,
     * and ordered by the given {@link Sort} parameter.
     *
     * SQL Equivalent:
     * <pre>
     *     SELECT *
     *     FROM tbl_incomes
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
     * @return a list of matching {@link IncomeEntity}
     */
    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
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
     *     FROM tbl_incomes
     *     WHERE profile_id = ?
     *       AND date BETWEEN ? AND ?;
     * </pre>
     *
     * @param profileId the ID of the profile
     * @param startDate the start date of the range (inclusive)
     * @param endDate   the end date of the range (inclusive)
     * @return a list of {@link IncomeEntity} within the specified date range
     */
    List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);

}
