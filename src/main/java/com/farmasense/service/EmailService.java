package com.farmasense.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @org.springframework.beans.factory.annotation.Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println(">>> Email successfully sent to: " + to);
        } catch (Exception e) {
            System.err.println("!!! FINAL ERROR: Failed to send email via SMTP: " + e.getMessage());
            e.printStackTrace(); // This will show the full error in your terminal
        }
    }

    public void sendVerificationOtp(String to, String fullName, String otp) {
        String subject = "FarmaSense - Account Verification Code";
        String body = "Hello " + fullName + ",\n\n" +
                      "Thank you for joining FarmaSense. Use the following code to verify your account:\n\n" +
                      "Verification Code: " + otp + "\n\n" +
                      "This code will expire in 15 minutes.\n\n" +
                      "Best regards,\n" +
                      "The FarmaSense Team";
        sendEmail(to, subject, body);
    }
}
