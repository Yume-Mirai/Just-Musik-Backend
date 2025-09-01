package com.example.justspotify.service;

import com.example.justspotify.model.Payment;
import com.example.justspotify.model.PaymentStatus;
import com.example.justspotify.model.User;
import com.example.justspotify.repository.PaymentRepository;
import com.example.justspotify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 10;

    public Payment initiatePayment(User user, Double amount) {
        Payment payment = new Payment(user, amount);
        String otp = generateOtp();
        payment.setOtp(otp);
        payment.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));

        Payment savedPayment = paymentRepository.save(payment);

        // Send OTP email
        emailService.sendOtpEmail(user.getEmail(), otp);

        return savedPayment;
    }

    public boolean verifyOtp(User user, String otp) {
        Optional<Payment> paymentOpt = paymentRepository.findByUserAndOtp(user, otp);

        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();

            if (payment.getOtpExpiry().isAfter(LocalDateTime.now()) &&
                payment.getStatus() == PaymentStatus.PENDING) {

                payment.setStatus(PaymentStatus.COMPLETED);
                paymentRepository.save(payment);

                // Update user payment status
                user.setIsPaid(true);
                userRepository.save(user);

                return true;
            }
        }
        return false;
    }

    private String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public boolean isUserPaid(User user) {
        return user.getIsPaid() != null && user.getIsPaid();
    }
}