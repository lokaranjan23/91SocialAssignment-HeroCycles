package com.finalProject.controller;

import com.finalProject.requestDto.CreateVariantRequestDto;
import com.finalProject.response.ApiResponse;
import com.finalProject.responseDto.BikeConfigurationResponseDto;
import com.finalProject.responseDto.VariantResponseDto;
import com.finalProject.service.impl.ConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/config/")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping("/bike-configurations")
    public ResponseEntity<ApiResponse<BikeConfigurationResponseDto>>
    createBikeConfiguration(@RequestParam String name) {

        BikeConfigurationResponseDto dto = configService.createBikeConfig(name);

        ApiResponse<BikeConfigurationResponseDto> response =
                new ApiResponse<>("Bike configuration created successfully", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/variants")
    public ResponseEntity<ApiResponse<VariantResponseDto>>
    createVariant(
            @RequestBody CreateVariantRequestDto requestDto) {

        VariantResponseDto dto = configService.createVariant(requestDto.getName(),
                        requestDto.getBikeConfigId(),
                        requestDto.getPartDtos());

        ApiResponse<VariantResponseDto> response =
                new ApiResponse<>(
                        "Variant created successfully", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
