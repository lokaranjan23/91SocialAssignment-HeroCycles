package com.AuthService.controller;

import com.AuthService.requestDto.LoginRequestDto;


import com.AuthService.requestDto.RegisterRequestDto;
import com.AuthService.response.ApiResponse;
import com.AuthService.responseDto.LoginResponseDto;
import com.AuthService.responseDto.UserResponseDto;
import com.AuthService.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(
            @Valid @RequestBody RegisterRequestDto requestDto) {

        UserResponseDto dto = authenticationService.register(requestDto);

        ApiResponse<UserResponseDto> response =
                new ApiResponse<>(
                        "Registration request submitted successfully. Awaiting admin approval.",
                        dto
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @RequestBody LoginRequestDto requestDto) {

        LoginResponseDto response = authenticationService.login(requestDto);

        ApiResponse<LoginResponseDto> apiResponse =
                new ApiResponse<>("Login successful", response);

        return ResponseEntity.ok(apiResponse);
    }
}