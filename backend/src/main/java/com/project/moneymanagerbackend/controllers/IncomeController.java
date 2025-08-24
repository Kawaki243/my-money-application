package com.project.moneymanagerbackend.controllers;


import com.project.moneymanagerbackend.dto.IncomeDTO;
import com.project.moneymanagerbackend.services.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody  IncomeDTO incomeDTO) {
        IncomeDTO addIncomeDTO = this.incomeService.addIncome(incomeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addIncomeDTO);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getIncomes() {
        List<IncomeDTO> incomeDTOList = this.incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(incomeDTOList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        this.incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}
