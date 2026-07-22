package com.finalProject.controller;

import com.finalProject.requestDto.BulkUpdateAddOnRequestDto;
import com.finalProject.requestDto.BulkUpdatePartRequestDto;
import com.finalProject.requestDto.CalculateTotalPriceRequestDto;
import com.finalProject.requestDto.PartRequestDto;
import com.finalProject.response.ApiResponse;
import com.finalProject.responseDto.*;
import com.finalProject.service.PricingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/pricing/")
public class PricingController {
    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping("/calculateTotalPrice")
    public ResponseEntity<ApiResponse<TotalPriceDto>> calculateTotalPrice(
            @RequestBody CalculateTotalPriceRequestDto requestDto){
        TotalPriceDto totalPriceDto =
                pricingService.calculateTotalPrice(requestDto);
        ApiResponse<TotalPriceDto> response=new ApiResponse<>(
                "Total price fetched successfully", totalPriceDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/parts")
    public ResponseEntity<ApiResponse<String>> addPart(
            @RequestBody PartRequestDto requestDto) {

        String response = pricingService.addParts(requestDto);

        ApiResponse<String> apiResponse = new ApiResponse<>(
                "Part added successfully",
                response
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/parts/prices")
    public ResponseEntity<ApiResponse<BulkUpdateResponseDto>> updatePartPrices(
            @RequestBody BulkUpdatePartRequestDto requestDto) {

        BulkUpdateResponseDto response = pricingService.updatePartPrices(requestDto);
        ApiResponse<BulkUpdateResponseDto> apiResponse = new ApiResponse<>(
                        "Part prices updated successfully", response);

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/addons/prices")
    public ResponseEntity<ApiResponse<BulkUpdateResponseDto>> updateAddOnPrices(
            @RequestBody BulkUpdateAddOnRequestDto requestDto) {

        BulkUpdateResponseDto response = pricingService.updateAddOnPrices(requestDto);

        ApiResponse<BulkUpdateResponseDto> apiResponse = new ApiResponse<>(
                        "Add-on prices updated successfully", response);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/bike-configurations/{bikeConfigId}/variants")
    public ResponseEntity<ApiResponse<List<VariantPriceDto>>> getVariants(
            @PathVariable Long bikeConfigId) {

        List<VariantPriceDto> variants = pricingService.getVariants(bikeConfigId);

        ApiResponse<List<VariantPriceDto>> response = new ApiResponse<>(
                        "Variants fetched successfully", variants);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/bike-configurations/{bikeConfigId}/addons")
    public ResponseEntity<ApiResponse<List<AddonsWithPriceResponseDto>>> getValidAddOns(
            @PathVariable Long bikeConfigId) {

        List<AddonsWithPriceResponseDto> addOns =
                pricingService.getValidAddOnsWithPrices(bikeConfigId);

        ApiResponse<List<AddonsWithPriceResponseDto>> response =
                new ApiResponse<>("Add-ons fetched successfully", addOns);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/parts")
    public ResponseEntity<ApiResponse<List<PartResponseDto>>>
    getAllParts() {

        List<PartResponseDto> result = pricingService.getAllParts();

        ApiResponse<List<PartResponseDto>> response =
                new ApiResponse<>("Parts fetched successfully", result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/addons")
    public ResponseEntity<ApiResponse<List<AddOnDropdownResponseDto>>>
    getAllAddOns() {

        List<AddOnDropdownResponseDto> result = pricingService.getAllAddOns();

        ApiResponse<List<AddOnDropdownResponseDto>> response =
                new ApiResponse<>("AddOns fetched successfully", result);

        return ResponseEntity.ok(response);
    }


}
