package com.example.config;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            // Skip if users already exist
            if (userRepository.count() > 0) {
                log.info("🔍 Users already exist. Skipping initialization.");
                return;
            }

            // Create an admin user
            User adminUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Admin User")
                    .role(Role.ROLE_ADMIN)
                    .enabled(true)
                    .build();

            // Create a regular user
            User regularUser = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .fullName("Regular User")
                    .role(Role.ROLE_USER)
                    .enabled(true)
                    .build();

            userRepository.save(adminUser);
            userRepository.save(regularUser);

            log.info("✅ Initial users created successfully!");
        };
    }
}