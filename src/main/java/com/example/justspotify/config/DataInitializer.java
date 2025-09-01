package com.example.justspotify.config;
import com.example.justspotify.model.ERole;
import com.example.justspotify.model.Role;
import com.example.justspotify.model.User;
import com.example.justspotify.repository.RoleRepository;
import com.example.justspotify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        // Create roles if they don't exist
        if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }
        if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_USER));
        }

        // Create admin user if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", "admin@musicplatform.com", encoder.encode("admin123"));
            Set<Role> adminRoles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            adminRoles.add(adminRole);
            admin.setRoles(adminRoles);
            userRepository.save(admin);
        }

        // Create regular user if not exists
        if (!userRepository.existsByUsername("user")) {
            User user = new User("user", "user@musicplatform.com", encoder.encode("user123"));
            Set<Role> userRoles = new HashSet<>();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            userRoles.add(userRole);
            user.setRoles(userRoles);
            userRepository.save(user);
        }
    }
}