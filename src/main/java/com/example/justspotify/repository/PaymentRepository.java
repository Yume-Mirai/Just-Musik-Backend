package com.example.justspotify.repository;

import com.example.justspotify.model.Payment;
import com.example.justspotify.model.PaymentStatus;
import com.example.justspotify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser(User user);
    List<Payment> findByUserAndStatus(User user, PaymentStatus status);
    Optional<Payment> findByUserAndOtp(User user, String otp);
}