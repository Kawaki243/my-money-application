package com.project.moneymanagerbackend.services;

import com.project.moneymanagerbackend.dto.ExpenseDTO;
import com.project.moneymanagerbackend.dto.IncomeDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Service class responsible for exporting {@link IncomeDTO} and {@link ExpenseDTO}
 * objects into Excel files using Apache POI.
 */
@Service
public class ExcelService {

    /**
     * Writes a list of incomes to an Excel file.
     *
     * @param os      the {@link OutputStream} where the Excel file will be written
     * @param incomes the list of {@link IncomeDTO} objects to be exported
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    public void writeIncomesToExcel(OutputStream os, List<IncomeDTO> incomes) throws IOException {
        // Create a new workbook in try-with-resources (auto-close)
        try (Workbook workbook = new XSSFWorkbook()) {

            // Create a new sheet named "Incomes"
            Sheet sheet = workbook.createSheet("Incomes");

            // Create the header row at index 0
            Row header = sheet.createRow(0);

            // Define header column names
            header.createCell(0).setCellValue("S.No");     // Serial number column
            header.createCell(1).setCellValue("Name");     // Income name
            header.createCell(2).setCellValue("Category"); // Category name
            header.createCell(3).setCellValue("Amount");   // Income amount
            header.createCell(4).setCellValue("Date");     // Income date

            // Loop through incomes and populate rows
            IntStream.range(0, incomes.size())
                    .forEach(i -> {
                        IncomeDTO income = incomes.get(i);

                        // Create a new row (i+1 because 0 is header)
                        Row row = sheet.createRow(i + 1);

                        // Fill row cells with income data
                        row.createCell(0).setCellValue(i + 1); // Serial number
                        row.createCell(1).setCellValue(income.getName() != null ? income.getName() : "N/A");
                        row.createCell(2).setCellValue(income.getCategoryId() != null ? income.getCategoryName() : "N/A");
                        row.createCell(3).setCellValue(income.getAmount() != null ? income.getAmount().doubleValue() : 0);
                        row.createCell(4).setCellValue(income.getDate() != null ? income.getDate().toString() : "N/A");
                    });

            // Write the completed workbook to the provided output stream
            workbook.write(os);
        }
    }

    /**
     * Writes a list of expenses to an Excel file.
     *
     * @param os       the {@link OutputStream} where the Excel file will be written
     * @param expenses the list of {@link ExpenseDTO} objects to be exported
     * @throws IOException if an I/O error occurs while writing to the stream
     */
    public void writeExpensesToExcel(OutputStream os, List<ExpenseDTO> expenses) throws IOException {
        // Create a new workbook in try-with-resources (auto-close)
        try (Workbook workbook = new XSSFWorkbook()) {

            // Create a new sheet named "Expenses"
            Sheet sheet = workbook.createSheet("Expenses");

            // Create the header row at index 0
            Row header = sheet.createRow(0);

            // Define header column names
            header.createCell(0).setCellValue("S.No");     // Serial number column
            header.createCell(1).setCellValue("Name");     // Expense name
            header.createCell(2).setCellValue("Category"); // Category name
            header.createCell(3).setCellValue("Amount");   // Expense amount
            header.createCell(4).setCellValue("Date");     // Expense date

            // Loop through expenses and populate rows
            IntStream.range(0, expenses.size())
                    .forEach(i -> {
                        ExpenseDTO expense = expenses.get(i);

                        // Create a new row (i+1 because 0 is header)
                        Row row = sheet.createRow(i + 1);

                        // Fill row cells with expense data
                        row.createCell(0).setCellValue(i + 1); // Serial number
                        row.createCell(1).setCellValue(expense.getName() != null ? expense.getName() : "");
                        row.createCell(2).setCellValue(expense.getCategoryId() != null ? expense.getCategoryName() : "N/A");
                        row.createCell(3).setCellValue(expense.getAmount() != null ? expense.getAmount().doubleValue() : 0);
                        row.createCell(4).setCellValue(expense.getDate() != null ? expense.getDate().toString() : "");
                    });

            // Write the completed workbook to the provided output stream
            workbook.write(os);
        }
    }
}

