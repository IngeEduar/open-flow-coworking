package com.nelumbo.open_flow_coworking.seeders;

import com.nelumbo.open_flow_coworking.entity.User;
import com.nelumbo.open_flow_coworking.repository.UserRepository;
import com.nelumbo.open_flow_coworking.security.utils.PasswordGenerator;
import com.nelumbo.open_flow_coworking.shared.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordGenerator passwordGenerator;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@mail.com";

        if (!userRepository.existsByEmail(adminEmail)) {
            log.info("Admin user not found, creating default admin user...");

            String rawPassword = "admin";
            String salt = passwordGenerator.generateSalt();
            String passwordHash = passwordGenerator.hash(rawPassword, salt);

            User adminUser = User.builder()
                    .name("Administrator")
                    .document("1000000000")
                    .email(adminEmail)
                    .password(passwordHash)
                    .salt(salt)
                    .role(UserRole.ADMIN)
                    .build();

            adminUser.setCreatedAt(OffsetDateTime.now());
            adminUser.setRecycle(false);

            userRepository.save(adminUser);
            log.info("Default Admin user created successfully.");
        } else {
            log.info("Admin user already exists. Skipping creation.");
        }
    }
}
