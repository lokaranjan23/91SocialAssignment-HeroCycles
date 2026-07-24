package com.AuthService.service;

import com.AuthService.entity.User;
import com.AuthService.enums.RegistrationStatus;
import com.AuthService.enums.Role;
import com.AuthService.exception.DuplicateUserException;
import com.AuthService.exception.InvalidDataEnteredException;
import com.AuthService.repository.UserRepository;
import com.AuthService.requestDto.LoginRequestDto;
import com.AuthService.requestDto.RegisterRequestDto;
import com.AuthService.responseDto.LoginResponseDto;
import com.AuthService.responseDto.UserResponseDto;
import com.AuthService.security.JwtService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
    private static final Logger log= LoggerFactory.getLogger(AuthenticationService.class);
    @Transactional
    public UserResponseDto register(RegisterRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            log.error("Email not registered");
            throw new DuplicateUserException("Email already registered");
        }

        if (requestDto.getRole() == Role.ADMIN) {
            log.error("Admin registration is not allowed");
            throw new InvalidDataEnteredException("Admin registration is not allowed");
        }

        User user = new User();
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(requestDto.getRole());
        user.setStatus(RegistrationStatus.PENDING);

        User savedUser = userRepository.save(user);

        UserResponseDto response = new UserResponseDto();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());
        response.setStatus(savedUser.getStatus());

        return response;
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword()
                    )
            );


        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
        System.out.println(passwordEncoder.matches(
                requestDto.getPassword(),
                user.getPassword()
        ));
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return new LoginResponseDto(token, user.getId(), user.getName(),
                user.getEmail(), user.getRole(), user.getStatus());
    }
}