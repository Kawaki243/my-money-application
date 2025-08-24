package com.project.moneymanagerbackend.services;


import com.project.moneymanagerbackend.dto.ExpenseDTO;
import com.project.moneymanagerbackend.dto.IncomeDTO;
import com.project.moneymanagerbackend.dto.RecentTransactionDTO;
import com.project.moneymanagerbackend.entities.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    /**
     * Retrieves the dashboard data for the currently authenticated user.
     * <p>
     * This method aggregates income and expense information for the current profile,
     * including totals and the most recent transactions, and returns it in a structured
     * map that can be used directly in the frontend.
     * </p>
     *
     * <ul>
     *   <li><b>Total Balance</b> → total income minus total expenses.</li>
     *   <li><b>Total Income</b> → sum of all incomes for the user.</li>
     *   <li><b>Total Expenses</b> → sum of all expenses for the user.</li>
     *   <li><b>Recent 5 Expenses</b> → list of the latest five expenses.</li>
     *   <li><b>Recent 5 Incomes</b> → list of the latest five incomes.</li>
     *   <li><b>Recent Transactions</b> → unified and chronologically sorted list
     *       of the latest incomes and expenses, limited to the most recent ones.</li>
     * </ul>
     *
     * @return a map containing dashboard data with keys:
     *         <ul>
     *             <li>"totalBalance"</li>
     *             <li>"totalIncome"</li>
     *             <li>"totalExpenses"</li>
     *             <li>"recent5Expenses"</li>
     *             <li>"recent5Income"</li>
     *             <li>"recentTransactions"</li>
     *         </ul>
     */
    public Map<String, Object> getDashboardData() {

        /** Get the currently authenticated profile entity */
        ProfileEntity profileEntity = this.profileService.getCurrentProfile();

        /** Prepare a LinkedHashMap to store ordered dashboard data */
        Map<String, Object> returnValue = new LinkedHashMap<>();

        /** Fetch the latest 5 incomes for the current user */
        List<IncomeDTO> lastestIncome = this.incomeService.getLatest5IncomesForCurrentUser();

        /** Fetch the latest 5 expenses for the current user */
        List<ExpenseDTO> lastestExpenses = this.expenseService.getLastest5ExpensesForCurrentUser();

        /**
         * Merge incomes and expenses into a unified list of RecentTransactionDTO objects.
         * Each income/expense is mapped to a common DTO structure for easier handling.
         */
        List<RecentTransactionDTO> recentTransactionDTOList = Stream.concat(

                        /** Map IncomeDTO -> RecentTransactionDTO */
                        lastestIncome.stream().map(income ->
                                RecentTransactionDTO.builder()
                                        .id(income.getId())
                                        .profileId(profileEntity.getId())
                                        .icon(income.getIcon())
                                        .name(income.getName())
                                        .amount(income.getAmount())
                                        .date(income.getDate())
                                        .createdAt(income.getCreatedAt())
                                        .updatedAt(income.getUpdatedAt())
                                        .type("income") // Mark as income
                                        .build()
                        ),

                        /** Map ExpenseDTO -> RecentTransactionDTO */
                        lastestExpenses.stream().map(expense ->
                                RecentTransactionDTO.builder()
                                        .id(expense.getId())
                                        .icon(expense.getIcon())
                                        .profileId(profileEntity.getId())
                                        .name(expense.getName())
                                        .amount(expense.getAmount())
                                        .date(expense.getDate())
                                        .createdAt(expense.getCreatedAt())
                                        .updatedAt(expense.getUpdatedAt())
                                        .type("expense") // Mark as expense
                                        .build()
                        )

                )
                /**
                 * Sort the merged transactions:
                 *  - First by transaction date in descending order
                 *  - If dates are equal, compare by creation timestamp in descending order
                 */
                .sorted((a, b) -> {
                    int cmp = b.getDate().compareTo(a.getDate());
                    if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                })
                /** Collect the sorted stream into a List */
                .collect(Collectors.toList());

        /** Add total balance (income - expenses) to response */
        returnValue.put("totalBalance", this.incomeService.getTotalIncomeForCurrentUser()
                .subtract(this.expenseService.getTotalExpensesForCurrentUser()));

        /** Add total income to response */
        returnValue.put("totalIncome", this.incomeService.getTotalIncomeForCurrentUser());

        /** Add total expenses to response */
        returnValue.put("totalExpenses", this.expenseService.getTotalExpensesForCurrentUser());

        /** Add the 5 most recent expenses to response */
        returnValue.put("recent5Expenses", lastestExpenses);

        /** Add the 5 most recent incomes to response */
        returnValue.put("recent5Income", lastestIncome);

        /** Add the unified recent transactions list to response */
        returnValue.put("recentTransactions", recentTransactionDTOList);

        /** Return the completed dashboard data map */
        return returnValue;
    }
}

