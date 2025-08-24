package com.project.moneymanagerbackend.services;


import com.project.moneymanagerbackend.dto.ExpenseDTO;
import com.project.moneymanagerbackend.entities.ProfileEntity;
import com.project.moneymanagerbackend.repositories.ExpenseRepository;
import com.project.moneymanagerbackend.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Provides a logger instance (log) for this class
public class NotificationService {

    private final ProfileRepository profileRepository; // Repository to access profile data
    private final EmailService emailService;           // Service for sending emails
    private final ExpenseService expenseService;       // Service to fetch expenses

    @Value("${money.manager.frontend.url}")
    private String frontendUrl; // URL of the frontend app (used in reminder email)

    /**
     * Scheduled job to send a daily reminder email
     * reminding all users to add their incomes & expenses.
     * <p>
     * Runs every day at 10:00 AM Vietnam time (GMT+7).
     */
    @Scheduled(cron = "0 0 10 * * *", zone = "GMT+7")
    public void sendDailyIncomeExpenseReminder() {
        log.info("Job started : sendDailyIncomeExpenseReminder()");

        // Fetch all registered profiles
        List<ProfileEntity> profileEntityList = this.profileRepository.findAll();

        // Send a reminder email to each profile
        for (ProfileEntity profileEntity : profileEntityList) {
            String body = "Hi " + profileEntity.getFullName() + ",<br><br>"
                    + "This is a friendly reminder to add your income and expenses for today in My Money.<br><br>"
                    + "<a href=" + this.frontendUrl + " style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to My Money</a>"
                    + "<br><br>Best regards,<br>My Money Team";

            this.emailService.sendEmail(
                    profileEntity.getEmail(),
                    "Daily reminder: Add your income and expenses",
                    body
            );
        }

        log.info("Job completed : sendDailyIncomeExpenseReminder()");
    }

    /**
     * Scheduled job to send a daily expense summary email
     * to each user, showing a table of today’s expenses.
     * <p>
     * Runs every day at 11:00 AM Vietnam time (GMT+7).
     */
    @Scheduled(cron = "0 0 11 * * *", zone = "GMT+7")
    public void sendDailyExpenseSummary() {
        log.info("Job started : sendDailyExpenseSummary()");

        // Fetch all registered profiles
        List<ProfileEntity> profileEntityList = this.profileRepository.findAll();

        for (ProfileEntity profileEntity : profileEntityList) {
            // Fetch today’s expenses for the user (using Vietnam timezone)
            List<ExpenseDTO> expenseDTOList =
                    this.expenseService.getExpensesForUserOnDate(
                            profileEntity.getId(),
                            LocalDate.now(ZoneId.of("GMT+7"))
                    );

            // Only send if there are expenses today
            if (!expenseDTOList.isEmpty()) {
                StringBuilder table = new StringBuilder();

                // Build expense summary table in HTML
                table.append("<table style='border-collapse:collapse;width:100%;'>");
                table.append("<tr style='background-color:#f2f2f2;'>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>Number</th>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>Name</th>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>Amount</th>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>Category</th>")
                        .append("</tr>");

                int i = 1;
                for (ExpenseDTO expense : expenseDTOList) {
                    table.append("<tr>")
                            .append("<td style='border:1px solid #ddd;padding:8px;'>").append(i++).append("</td>")
                            .append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getName()).append("</td>")
                            .append("<td style='border:1px solid #ddd;padding:8px;'>").append(expense.getAmount()).append("</td>")
                            .append("<td style='border:1px solid #ddd;padding:8px;'>")
                            .append(expense.getCategoryId() != null ? expense.getCategoryName() : "N/A")
                            .append("</td>")
                            .append("</tr>");
                }
                table.append("</table>");

                // Build email body
                String body = "Hi " + profileEntity.getFullName()
                        + ",<br/><br/> Here is a summary of your expenses for today:<br/><br/>"
                        + table
                        + "<br/><br/>Best regards,<br/>My Money Team";

                // Send summary email
                this.emailService.sendEmail(
                        profileEntity.getEmail(),
                        "Your daily Expense summary",
                        body
                );
            }
        }

        log.info("Job completed : sendDailyExpenseSummary()");
    }
}

