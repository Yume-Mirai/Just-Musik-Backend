package com.example.justspotify.controller;

import com.example.justspotify.model.Payment;
import com.example.justspotify.model.User;
import com.example.justspotify.repository.UserRepository;
import com.example.justspotify.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/initiate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> initiatePayment(@RequestBody Map<String, Double> request) {
        try {
            User currentUser = getCurrentUser();
            Double amount = request.get("amount");

            if (amount == null || amount <= 0) {
                return ResponseEntity.badRequest().body("Invalid amount");
            }

            Payment payment = paymentService.initiatePayment(currentUser, amount);
            return ResponseEntity.ok(Map.of(
                    "message", "OTP sent to your email",
                    "paymentId", payment.getId(),
                    "otp", payment.getOtp()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // ← ini akan muncul di log Railway
            return ResponseEntity.internalServerError()
                    .body("Payment initiation failed: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        try {
            User currentUser = getCurrentUser();
            String otp = request.get("otp");

            if (otp == null || otp.isEmpty()) {
                return ResponseEntity.badRequest().body("OTP is required");
            }

            boolean verified = paymentService.verifyOtp(currentUser, otp);
            if (verified) {
                return ResponseEntity.ok(Map.of("message", "Payment verified successfully"));
            } else {
                return ResponseEntity.badRequest().body("Invalid or expired OTP");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to verify payment");
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}