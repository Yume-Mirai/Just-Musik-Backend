package com.example.justspotify.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from.email:noreply@yourdomain.com}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(fromEmail)
                    .to(toEmail)
                    .subject("Your OTP for Payment Verification")
                    .html("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                          "<h2 style='color: #333;'>Payment Verification</h2>" +
                          "<p>Your OTP code is:</p>" +
                          "<div style='background-color: #f0f0f0; padding: 20px; text-align: center; font-size: 24px; font-weight: bold; letter-spacing: 3px; margin: 20px 0;'>" +
                          otp +
                          "</div>" +
                          "<p style='color: #666;'>This code will expire in 10 minutes.</p>" +
                          "<p style='color: #999; font-size: 12px;'>If you didn't request this code, please ignore this email.</p>" +
                          "</div>")
                    .build();

            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("Email sent successfully with ID: " + data.getId());
            
        } catch (ResendException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}