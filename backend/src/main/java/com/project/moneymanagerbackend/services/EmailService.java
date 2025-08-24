package com.project.moneymanagerbackend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for sending emails.
 * <p>
 * Provides methods for sending simple text emails and emails
 * with file attachments using Spring's {@link JavaMailSender}.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    /**
     * The mail sender bean provided by Spring Boot.
     * Used to send both simple and MIME (attachment) emails.
     */
    private final JavaMailSender mailSender;

    /**
     * The sender's email address, injected from application properties.
     * Configured via {@code spring.mail.properties.mail.smtp.from}.
     */
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    /**
     * Sends a simple text email.
     *
     * @param toEmail recipient email address
     * @param subject subject of the email
     * @param body    body content of the email (plain text)
     *
     * @throws RuntimeException if email sending fails
     */
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            // Create a simple text-based email message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);   // set sender email
            message.setTo(toEmail);       // set recipient email
            message.setSubject(subject);  // set subject line
            message.setText(body);        // set plain text body

            // Send the email
            mailSender.send(message);
        } catch (Exception ex) {
            // Wrap and rethrow as a runtime exception
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Sends an email with an attachment.
     *
     * @param to        recipient email address
     * @param subject   subject of the email
     * @param body      body content of the email (plain text or HTML)
     * @param attachment file content as byte array
     * @param filename   name of the attached file
     *
     * @throws MessagingException if there is a failure in constructing or sending the message
     */
    public void sendEmailWithAttachment(String to,
                                        String subject,
                                        String body,
                                        byte[] attachment,
                                        String filename) throws MessagingException {
        // Create a MIME message (supports attachments, HTML, inline content, etc.)
        MimeMessage message = mailSender.createMimeMessage();

        // Helper for building MIME messages, with "true" enabling multipart
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set sender, recipient, subject, and body
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        // Add attachment from byte array, wrapping in a ByteArrayResource
        helper.addAttachment(filename, new ByteArrayResource(attachment));

        // Send the email with attachment
        mailSender.send(message);
    }
}
