package com.project.moneymanagerbackend.controllers;

import com.project.moneymanagerbackend.entities.ProfileEntity;
import com.project.moneymanagerbackend.services.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * REST controller for sending Excel reports (incomes and expenses)
 * as email attachments to the currently authenticated user.
 *
 * <p>This controller integrates with:
 * <ul>
 *     <li>{@link ExcelService} - for generating Excel reports.</li>
 *     <li>{@link IncomeService} - for retrieving user income data.</li>
 *     <li>{@link ExpenseService} - for retrieving user expense data.</li>
 *     <li>{@link EmailService} - for sending emails with attachments.</li>
 *     <li>{@link ProfileService} - for fetching the currently logged-in user's profile.</li>
 * </ul>
 */
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    /**
     * Service for generating Excel files from income/expense data.
     */
    private final ExcelService excelService;

    /**
     * Service for retrieving income data for the current user.
     */
    private final IncomeService incomeService;

    /**
     * Service for retrieving expense data for the current user.
     */
    private final ExpenseService expenseService;

    /**
     * Service for sending plain or attachment-based emails.
     */
    private final EmailService emailService;

    /**
     * Service for fetching the profile of the currently logged-in user.
     */
    private final ProfileService profileService;

    /**
     * Sends the current month's incomes as an Excel report
     * to the logged-in user's email address.
     *
     * <p>Steps performed:
     * <ol>
     *     <li>Fetch the current user's profile and email.</li>
     *     <li>Generate an Excel file of incomes into a {@link ByteArrayOutputStream}.</li>
     *     <li>Send the Excel as an email attachment via {@link EmailService}.</li>
     * </ol>
     *
     * @return {@link ResponseEntity#ok(Object)} indicating the email was sent
     * @throws IOException if an error occurs while writing the Excel file
     * @throws MessagingException if an error occurs while sending the email
     */
    @GetMapping("/income-excel")
    public ResponseEntity<Void> emailIncomeExcel() throws IOException, MessagingException {
        // Get the current user profile (with email)
        ProfileEntity profile = profileService.getCurrentProfile();

        // Stream for holding Excel file data
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Generate incomes Excel and write to stream
        excelService.writeIncomesToExcel(
                baos,
                incomeService.getCurrentMonthIncomesForCurrentUser()
        );

        // Send the Excel as an email attachment
        emailService.sendEmailWithAttachment(
                profile.getEmail(),
                "Your Income Excel Report",
                "Please find attached your income report",
                baos.toByteArray(),
                "income.xlsx"
        );

        return ResponseEntity.ok(null);
    }

    /**
     * Sends the current month's expenses as an Excel report
     * to the logged-in user's email address.
     *
     * <p>Steps performed:
     * <ol>
     *     <li>Fetch the current user's profile and email.</li>
     *     <li>Generate an Excel file of expenses into a {@link ByteArrayOutputStream}.</li>
     *     <li>Send the Excel as an email attachment via {@link EmailService}.</li>
     * </ol>
     *
     * @return {@link ResponseEntity#ok(Object)} indicating the email was sent
     * @throws IOException if an error occurs while writing the Excel file
     * @throws MessagingException if an error occurs while sending the email
     */
    @GetMapping("/expense-excel")
    public ResponseEntity<Void> emailExpenseExcel() throws IOException, MessagingException {
        // Get the current user profile (with email)
        ProfileEntity profile = profileService.getCurrentProfile();

        // Stream for holding Excel file data
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Generate expenses Excel and write to stream
        excelService.writeExpensesToExcel(
                baos,
                expenseService.getCurrentMonthExpensesForCurrentUser()
        );

        // Send the Excel as an email attachment
        emailService.sendEmailWithAttachment(
                profile.getEmail(),
                "Your Expense Excel Report",
                "Please find attached your expense report.",
                baos.toByteArray(),
                "expenses.xlsx"
        );

        return ResponseEntity.ok(null);
    }
}