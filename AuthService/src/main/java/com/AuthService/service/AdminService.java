package com.AuthService.service;

import com.AuthService.entity.User;
import com.AuthService.enums.RegistrationStatus;
import com.AuthService.enums.Role;
import com.AuthService.exception.InvalidUserStatusException;
import com.AuthService.exception.UserNotFoundException;
import com.AuthService.repository.UserRepository;
import com.AuthService.responseDto.PendingUserResponseDto;
import com.AuthService.responseDto.UserResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<PendingUserResponseDto> getPendingUsers() {

        List<User> users = userRepository.findByStatus(RegistrationStatus.PENDING);

        List<PendingUserResponseDto> response = new ArrayList<>();

        for (User user : users) {

            PendingUserResponseDto dto = new PendingUserResponseDto();

            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setRole(user.getRole());

            response.add(dto);
        }

        return response;
    }

    @Transactional
    public String approveUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new InvalidUserStatusException("Admin status cannot be changed");
        }

        if (user.getStatus() != RegistrationStatus.PENDING) {
            throw new InvalidUserStatusException("Only pending users can be approved");
        }

        user.setStatus(RegistrationStatus.APPROVED);

        return "User approved successfully.";
    }

    @Transactional
    public String rejectUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            throw new InvalidUserStatusException("Admin status cannot be changed");
        }

        if (user.getStatus() != RegistrationStatus.PENDING) {
            throw new InvalidUserStatusException("Only pending users can be rejected");
        }

        user.setStatus(RegistrationStatus.REJECTED);

        return "User rejected successfully.";
    }

    @Transactional
    public String blockUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (user.getStatus() == RegistrationStatus.BLOCKED) {
            throw new InvalidUserStatusException("User is already blocked");
        }

        if (user.getStatus() == RegistrationStatus.PENDING) {
            throw new InvalidUserStatusException("Pending users cannot be blocked");
        }

        if (user.getStatus() == RegistrationStatus.REJECTED) {
            throw new InvalidUserStatusException("Rejected users cannot be blocked");
        }

        if (user.getRole() == Role.ADMIN) {
            throw new InvalidUserStatusException("Admin account cannot be blocked");
        }

        user.setStatus(RegistrationStatus.BLOCKED);

        userRepository.save(user);

        return "User blocked successfully.";
    }

    @Transactional
    public String unblockUser(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (user.getStatus() != RegistrationStatus.BLOCKED) {
            throw new InvalidUserStatusException("Only blocked users can be unblocked");
        }

        if (user.getRole() == Role.ADMIN) {
            throw new InvalidUserStatusException("Admin account cannot be blocked");
        }

        user.setStatus(RegistrationStatus.APPROVED);

        userRepository.save(user);

        return "User unblocked successfully.";
    }

    public List<UserResponseDto> getApprovedUsers() {
        List<User> users = userRepository.findByStatus(RegistrationStatus.APPROVED);
        List<UserResponseDto> responseDto= new ArrayList<>();
        for(User user: users){
            UserResponseDto dto=new UserResponseDto();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setName(user.getName());
            dto.setRole(user.getRole());
            dto.setStatus(user.getStatus());
            responseDto.add(dto);
        }
        return responseDto;

    }

    public List<UserResponseDto> getBlockedUsers() {
        List<User> users = userRepository.findByStatus(RegistrationStatus.BLOCKED);
        List<UserResponseDto> responseDto= new ArrayList<>();
        for(User user: users){
            UserResponseDto dto=new UserResponseDto();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setName(user.getName());
            dto.setRole(user.getRole());
            dto.setStatus(user.getStatus());
            responseDto.add(dto);
        }
        return responseDto;

    }
}