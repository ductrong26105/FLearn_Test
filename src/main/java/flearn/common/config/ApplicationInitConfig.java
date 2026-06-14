package flearn.common.config;

import flearn.entity.User;
import flearn.enums.UserStatus;
import flearn.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    static final String ADMIN_USERNAME = "admin";
    static final String ADMIN_PASSWORD = "admin";
    static final String ADMIN_EMAIL = "admin@flearn.local";
    static final int ADMIN_ROLE = 0;

    PasswordEncoder passwordEncoder;

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            var existingAdmin = userRepository.findByUsername(ADMIN_USERNAME);
            if (existingAdmin.isPresent()) {
                User admin = existingAdmin.get();
                admin.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));
                admin.setFullName("FLearn Administrator");
                admin.setEmail(ADMIN_EMAIL);
                admin.setRole(ADMIN_ROLE);
                admin.setStatus(UserStatus.ACTIVE);
                admin.setIsActive(true);
                admin.setResetToken(null);
                admin.setResetTokenExpiry(null);
                userRepository.save(admin);
                log.warn("Admin user refreshed with username '{}' and password '{}'. Please change the password immediately after logging in.",
                        ADMIN_USERNAME, ADMIN_PASSWORD);
                return;
            }

            User admin = User.builder()
                    .username(ADMIN_USERNAME)
                    .passwordHash(passwordEncoder.encode(ADMIN_PASSWORD))
                    .fullName("FLearn Administrator")
                    .email(ADMIN_EMAIL)
                    .role(ADMIN_ROLE)
                    .status(UserStatus.ACTIVE)
                    .isActive(true)
                    .build();

            userRepository.save(admin);
            log.warn("Admin user created with username '{}' and password '{}'. Please change the password immediately after logging in.",
                    ADMIN_USERNAME, ADMIN_PASSWORD);
        };
    }
}
