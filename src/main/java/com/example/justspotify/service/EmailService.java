package com.example.justspotify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            // Try Gmail first
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your OTP for Payment Verification");
            message.setText("Your OTP code is: " + otp + "\n\nThis code will expire in 10 minutes.");
            mailSender.send(message);
        } catch (MailException e) {
            // Fallback to SendGrid
            sendOtpEmailViaSendGrid(toEmail, otp);
        }
    }

    private void sendOtpEmailViaSendGrid(String toEmail, String otp) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        String subject = "Your OTP for Payment Verification";
        Content content = new Content("text/plain", "Your OTP code is: " + otp + "\n\nThis code will expire in 10 minutes.");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            // Optionally log response status
        } catch (Exception e) {
            // Handle SendGrid failure, perhaps log or throw
            throw new RuntimeException("Failed to send email via both Gmail and SendGrid", e);
        }
    }
}

// package com.example.justspotify.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

// @Service
// public class EmailService {

//     @Autowired
//     private JavaMailSender mailSender;

//     public void sendOtpEmail(String toEmail, String otp) {
//         SimpleMailMessage message = new SimpleMailMessage();
//         message.setTo(toEmail);
//         message.setSubject("Your OTP for Payment Verification");
//         message.setText("Your OTP code is: " + otp + "\n\nThis code will expire in 10 minutes.");

//         mailSender.send(message);
//     }
// }