package com.project.moneymanagerbackend.controllers;


import com.project.moneymanagerbackend.dto.ExpenseDTO;
import com.project.moneymanagerbackend.entities.ExpenseEntity;
import com.project.moneymanagerbackend.services.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO addExpenseDTO = this.expenseService.addExpense(expenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addExpenseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        List<ExpenseDTO> expenseDTOList = this.expenseService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(expenseDTOList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        this.expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
