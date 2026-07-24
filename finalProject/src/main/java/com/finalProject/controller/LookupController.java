package com.finalProject.controller;

import com.finalProject.enums.PartCategory;
import com.finalProject.response.ApiResponse;
import com.finalProject.responseDto.*;
import com.finalProject.service.impl.LookupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lookup")
public class LookupController {
    private final LookupService lookupService;

    public LookupController(LookupService lookupService) {
        this.lookupService = lookupService;
    }


    @PreAuthorize("hasAnyRole('CONFIG','SALES')")
    @GetMapping("/bike-configurations/{bikeConfigId}/variants")
    public ResponseEntity<ApiResponse<List<VariantPriceDto>>> getVariants(
            @PathVariable Long bikeConfigId) {

        List<VariantPriceDto> variants = lookupService.getVariants(bikeConfigId);

        ApiResponse<List<VariantPriceDto>> response = new ApiResponse<>(
                "Variants fetched successfully", variants);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('CONFIG','SALES')")
    @GetMapping("/bike-configurations/{bikeConfigId}/addons")
    public ResponseEntity<ApiResponse<List<AddonsWithPriceResponseDto>>> getValidAddOns(
            @PathVariable Long bikeConfigId) {

        List<AddonsWithPriceResponseDto> addOns =
                lookupService.getValidAddOnsWithPrices(bikeConfigId);

        ApiResponse<List<AddonsWithPriceResponseDto>> response =
                new ApiResponse<>("Add-ons fetched successfully", addOns);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CONFIG')")
    @GetMapping("/dropdowns/parts")
    public ResponseEntity<ApiResponse<List<PartResponseDto>>> getAllParts() {

        List<PartResponseDto> result = lookupService.getAllParts();

        ApiResponse<List<PartResponseDto>> response =
                new ApiResponse<>("Parts fetched successfully", result);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CONFIG')")
    @GetMapping("/dropdowns/addons")
    public ResponseEntity<ApiResponse<List<AddOnDropdownResponseDto>>> getAllAddOns() {

        List<AddOnDropdownResponseDto> result = lookupService.getAllAddOns();

        ApiResponse<List<AddOnDropdownResponseDto>> response =
                new ApiResponse<>("AddOns fetched successfully", result);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('CONFIG','SALES')")
    @GetMapping("/bike-configurations")
    public ResponseEntity<ApiResponse<List<BikeConfigurationResponseDto>>> getAllBikeConfigurations() {

        List<BikeConfigurationResponseDto> result = lookupService.getAllBikeConfigurations();

        ApiResponse<List<BikeConfigurationResponseDto>> response =
                new ApiResponse<>("Bike configurations fetched successfully", result);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('CONFIG','PRICING')")
    @GetMapping("/partCategories")
    public ResponseEntity<ApiResponse<List<PartCategoryResponseDto>>> getAllPartCategories() {

        List<PartCategoryResponseDto> result = lookupService.getAllPartCategories();
        ApiResponse<List<PartCategoryResponseDto>> response =
                new ApiResponse<>("Part categories fetched successfully", result);
        return ResponseEntity.ok(response);
    }



    @PreAuthorize("hasRole('CONFIG')")
    @GetMapping("/parts/{category}")
    public ResponseEntity<ApiResponse<List<PartResponseDto>>>
    getPartsByCategory(@PathVariable PartCategory category) {

        List<PartResponseDto> result = lookupService.getPartsByCategory(category);

        ApiResponse<List<PartResponseDto>> response =
                new ApiResponse<>("Parts fetched successfully", result);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PRICING')")
    @GetMapping("/pricing/parts")
    public ResponseEntity<ApiResponse<List<PartPricingResponseDto>>> getPricingParts() {

        List<PartPricingResponseDto> result = lookupService.getPricingParts();

        ApiResponse<List<PartPricingResponseDto>> response =
                new ApiResponse<>("Pricing parts fetched successfully", result);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PRICING')")
    @GetMapping("/pricing/addons")
    public ResponseEntity<ApiResponse<List<AddOnPricingResponseDto>>> getPricingAddOns() {

        List<AddOnPricingResponseDto> result = lookupService.getPricingAddOns();

        ApiResponse<List<AddOnPricingResponseDto>> response =
                new ApiResponse<>("Pricing add-ons fetched successfully", result);

        return ResponseEntity.ok(response);
    }



}
