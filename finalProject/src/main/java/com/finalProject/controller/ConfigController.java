package com.finalProject.controller;

import com.finalProject.enums.PartCategory;
import com.finalProject.requestDto.BikeConfigurationRequestDto;
import com.finalProject.requestDto.ConfigureAddOnsRequestDto;
import com.finalProject.requestDto.CreateVariantRequestDto;
import com.finalProject.requestDto.UpdateVariantStatusRequestDto;
import com.finalProject.response.ApiResponse;
import com.finalProject.responseDto.*;
import com.finalProject.service.impl.ConfigService;
import com.finalProject.service.impl.LookupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/config/")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService, LookupService lookupService) {
        this.configService = configService;
    }

    @PreAuthorize("hasRole('CONFIG')")
    @PostMapping("/bike-configurations")
    public ResponseEntity<ApiResponse<BikeConfigurationResponseDto>>
    createBikeConfiguration(@RequestBody BikeConfigurationRequestDto requestDto) {

        BikeConfigurationResponseDto dto = configService.createBikeConfig(requestDto);

        ApiResponse<BikeConfigurationResponseDto> response =
                new ApiResponse<>("Bike configuration created successfully", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('CONFIG')")
    @PostMapping("/variants")
    public ResponseEntity<ApiResponse<VariantResponseDto>> createVariant(
            @RequestBody CreateVariantRequestDto requestDto) {

        VariantResponseDto dto = configService.createVariant(requestDto);

        ApiResponse<VariantResponseDto> response =
                new ApiResponse<>(
                        "Variant created successfully", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('CONFIG')")
    @PostMapping("/linkAddOns")
    public ResponseEntity<ApiResponse<String>> linkAddOn(
            @RequestBody ConfigureAddOnsRequestDto requestDto){
        String s = configService.linkAddOn(requestDto);
        ApiResponse<String> response=new ApiResponse<>("linking successful",
                s);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }


    @PreAuthorize("hasRole('CONFIG')")
    @PutMapping("/variants/{variantId}/status")
    public ResponseEntity<ApiResponse<String>> updateVariantStatus(@PathVariable Long variantId,
            @RequestBody UpdateVariantStatusRequestDto requestDto) {
        String result = configService.updateVariantStatus(variantId, requestDto);

        ApiResponse<String> response = new ApiResponse<>(
                "Variant status updated successfully", result);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CONFIG')")
    @GetMapping("/variants/search")
    public ResponseEntity<ApiResponse<List<VariantResponseDto>>> searchVariants(
            @RequestParam(required = false) String keyword) {
        List<VariantResponseDto> result =
                configService.searchVariants(keyword);

        ApiResponse<List<VariantResponseDto>> response =
                new ApiResponse<>(
                        "Variants fetched successfully.",
                        result
                );

        return ResponseEntity.ok(response);
    }


}
