package com.AuthService.controller;

import com.AuthService.response.ApiResponse;
import com.AuthService.responseDto.PendingUserResponseDto;
import com.AuthService.responseDto.UserResponseDto;
import com.AuthService.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/pending-users")
    public ResponseEntity<ApiResponse<List<PendingUserResponseDto>>>
    getPendingUsers() {

        List<PendingUserResponseDto> result =
                adminService.getPendingUsers();

        ApiResponse<List<PendingUserResponseDto>> response =
                new ApiResponse<>("Pending users fetched successfully", result);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/approve/{userId}")
    public ResponseEntity<ApiResponse<String>>
    approveUser(@PathVariable Long userId) {

        String result = adminService.approveUser(userId);

        ApiResponse<String> response =
                new ApiResponse<>("User approved successfully", result);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/reject/{userId}")
    public ResponseEntity<ApiResponse<String>>
    rejectUser(@PathVariable Long userId) {

        String result = adminService.rejectUser(userId);

        ApiResponse<String> response =
                new ApiResponse<>("User rejected successfully", result);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/block/{userId}")
    public ResponseEntity<ApiResponse<String>> blockUser(
            @PathVariable Long userId) {

        String result = adminService.blockUser(userId);

        ApiResponse<String> response =
                new ApiResponse<>("User blocked successfully.", result);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/unblock/{userId}")
    public ResponseEntity<ApiResponse<String>> unblockUser(
            @PathVariable Long userId) {

        String result = adminService.unblockUser(userId);

        ApiResponse<String> response =
                new ApiResponse<>("User unblocked successfully.", result);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/approved-users")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getApprovedUsers() {

        List<UserResponseDto> users = adminService.getApprovedUsers();

        return ResponseEntity.ok(
                new ApiResponse<>("Approved users fetched successfully", users)
        );
    }

    @GetMapping("/blocked-users")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getBlockedUsers() {

        List<UserResponseDto> users = adminService.getBlockedUsers();

        return ResponseEntity.ok(
                new ApiResponse<>("Blocked users fetched successfully", users)
        );
    }
}
