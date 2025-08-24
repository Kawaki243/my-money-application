package com.project.moneymanagerbackend.controllers;


import com.project.moneymanagerbackend.dto.ExpenseDTO;
import com.project.moneymanagerbackend.dto.FilterDTO;
import com.project.moneymanagerbackend.dto.IncomeDTO;
import com.project.moneymanagerbackend.services.ExpenseService;
import com.project.moneymanagerbackend.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    /**
     * Filters transactions (income or expense) based on criteria provided in {@link FilterDTO}.
     *
     * Supported filters:
     * <ul>
     *   <li>Date range (startDate to endDate)</li>
     *   <li>Keyword search (case-insensitive match on transaction name)</li>
     *   <li>Sorting (by field and order)</li>
     *   <li>Type (income or expense)</li>
     * </ul>
     *
     * @param filterDTO request body containing filter parameters
     * @return ResponseEntity containing list of filtered IncomeDTOs or ExpenseDTOs
     */
    @PostMapping
    public ResponseEntity<?> filterTransaction(@RequestBody FilterDTO filterDTO) {

        // Default startDate: if not provided, set to LocalDate.MIN (earliest possible date).
        LocalDate startDate = filterDTO.getStartDate() != null ? filterDTO.getStartDate() : LocalDate.MIN;

        // Default endDate: if not provided, use today's date.
        LocalDate endDate = filterDTO.getEndDate() != null ? filterDTO.getEndDate() : LocalDate.now();

        // Default keyword: if not provided, use empty string (matches all).
        String keyword = filterDTO.getKeyword() != null ? filterDTO.getKeyword() : "";

        // Default sort field: if not provided, sort by "date".
        String sortField = filterDTO.getSortField() != null ? filterDTO.getSortField() : "date";

        // Default sort direction: if "desc", use DESC; otherwise, ASC.
        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(filterDTO.getSortOrder())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        // Create Sort object dynamically using sort field + direction.
        Sort sort = Sort.by(sortDirection, sortField);

        // If type is "income", filter incomes.
        if (filterDTO.getType().equalsIgnoreCase("income")) {
            List<IncomeDTO> incomeDTOList =
                    this.incomeService.filterIncomes(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(incomeDTOList);

            // If type is "expense", filter expenses.
        } else if (filterDTO.getType().equalsIgnoreCase("expense")) {
            List<ExpenseDTO> expenseDTOList =
                    this.expenseService.filterExpenses(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(expenseDTOList);

            // If type is invalid, return 400 Bad Request.
        } else {
            return ResponseEntity.badRequest()
                    .body("Invalid filter type. Must be \"income\" or \"expense\" !");
        }
    }
}

