package com.project.moneymanagerbackend.controllers;

import com.project.moneymanagerbackend.services.ExcelService;
import com.project.moneymanagerbackend.services.ExpenseService;
import com.project.moneymanagerbackend.services.IncomeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * REST controller for exporting incomes and expenses to Excel files.
 * <p>
 * Provides endpoints to download the current month's incomes and expenses
 * as Excel files using the {@link ExcelService}.
 */
@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {

    /**
     * Service responsible for generating Excel files for incomes and expenses.
     */
    private final ExcelService excelService;

    /**
     * Service for fetching income data of the current user.
     */
    private final IncomeService incomeService;

    /**
     * Service for fetching expense data of the current user.
     */
    private final ExpenseService expenseService;

    /**
     * Endpoint for downloading the current month's incomes as an Excel file.
     * <p>
     * The method sets the response headers to indicate a downloadable
     * Excel file and then streams the generated file to the client.
     *
     * @param response the HTTP response used to send the Excel file
     * @throws IOException if an I/O error occurs while writing to the output stream
     */
    @GetMapping("/download/incomes")
    public void downloadIncomeExcel(HttpServletResponse response) throws IOException {
        // Set response content type to Excel MIME type
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // Set header so the browser treats response as a downloadable file
        response.setHeader("Content-Disposition", "attachment; filename=income.xlsx");

        // Generate Excel file and write it to the response output stream
        excelService.writeIncomesToExcel(
                response.getOutputStream(),
                incomeService.getCurrentMonthIncomesForCurrentUser()
        );
    }

    /**
     * Endpoint for downloading the current month's expenses as an Excel file.
     * <p>
     * The method sets the response headers to indicate a downloadable
     * Excel file and then streams the generated file to the client.
     *
     * @param response the HTTP response used to send the Excel file
     * @throws IOException if an I/O error occurs while writing to the output stream
     */
    @GetMapping("/download/expenses")
    public void downloadExpenseExcel(HttpServletResponse response) throws IOException {
        // Set response content type to Excel MIME type
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        // Set header so the browser treats response as a downloadable file
        response.setHeader("Content-Disposition", "attachment; filename=expense.xlsx");

        // Generate Excel file and write it to the response output stream
        excelService.writeExpensesToExcel(
                response.getOutputStream(),
                expenseService.getCurrentMonthExpensesForCurrentUser()
        );
    }
}