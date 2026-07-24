package com.AuthService.config;

import com.AuthService.entity.User;
import com.AuthService.enums.RegistrationStatus;
import com.AuthService.enums.Role;
import com.AuthService.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (!userRepository.existsByEmail("admin@91social.com")) {

            User admin = new User();

            admin.setName("System Admin");
            admin.setEmail("admin@91social.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));

            admin.setRole(Role.ADMIN);
            admin.setStatus(RegistrationStatus.APPROVED);

            userRepository.save(admin);

            System.out.println("Default Admin Created Successfully");
        }
    }
}