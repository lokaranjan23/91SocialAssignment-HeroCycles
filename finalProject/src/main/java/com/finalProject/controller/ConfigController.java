package com.finalProject.controller;

import com.finalProject.enums.PartCategory;
import com.finalProject.requestDto.BikeConfigurationRequestDto;
import com.finalProject.requestDto.ConfigureAddOnsRequestDto;
import com.finalProject.requestDto.CreateVariantRequestDto;
import com.finalProject.response.ApiResponse;
import com.finalProject.responseDto.*;
import com.finalProject.service.impl.ConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/config/")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping("/bike-configurations")
    public ResponseEntity<ApiResponse<BikeConfigurationResponseDto>>
    createBikeConfiguration(@RequestBody BikeConfigurationRequestDto requestDto) {

        BikeConfigurationResponseDto dto = configService.createBikeConfig(requestDto);

        ApiResponse<BikeConfigurationResponseDto> response =
                new ApiResponse<>("Bike configuration created successfully", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/variants")
    public ResponseEntity<ApiResponse<VariantResponseDto>> createVariant(
            @RequestBody CreateVariantRequestDto requestDto) {

        VariantResponseDto dto = configService.createVariant(requestDto);

        ApiResponse<VariantResponseDto> response =
                new ApiResponse<>(
                        "Variant created successfully", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/bike-configurations")
    public ResponseEntity<ApiResponse<List<BikeConfigurationResponseDto>>>
    getAllBikeConfigurations() {

        List<BikeConfigurationResponseDto> result = configService.getAllBikeConfigurations();

        ApiResponse<List<BikeConfigurationResponseDto>> response =
                new ApiResponse<>("Bike configurations fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/partCategories")
    public ResponseEntity<ApiResponse<List<PartCategoryResponseDto>>> getAllPartCategories() {

        List<PartCategoryResponseDto> result = configService.getAllPartCategories();
        ApiResponse<List<PartCategoryResponseDto>> response =
                new ApiResponse<>("Part categories fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/linkAddOns")
    public ResponseEntity<ApiResponse<String>> linkAddOn(
            @RequestBody ConfigureAddOnsRequestDto requestDto){
        String s = configService.linkAddOn(requestDto);
        ApiResponse<String> response=new ApiResponse<>("linking successful",
                s);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/parts/{category}")
    public ResponseEntity<ApiResponse<List<PartResponseDto>>>
    getPartsByCategory(@PathVariable PartCategory category) {

        List<PartResponseDto> result = configService.getPartsByCategory(category);

        ApiResponse<List<PartResponseDto>> response =
                new ApiResponse<>("Parts fetched successfully", result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/addons")
    public ResponseEntity<ApiResponse<List<AddOnDropdownResponseDto>>>
    getAllAddOns() {

        List<AddOnDropdownResponseDto> result = configService.getAllAddOns();

        ApiResponse<List<AddOnDropdownResponseDto>> response = new ApiResponse<>(
                        "AddOns fetched successfully", result);

        return ResponseEntity.ok(response);
    }

}
