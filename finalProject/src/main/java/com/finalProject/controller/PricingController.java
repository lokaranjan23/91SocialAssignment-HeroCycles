package com.finalProject.controller;

import com.finalProject.requestDto.*;
import com.finalProject.response.ApiResponse;
import com.finalProject.responseDto.*;
import com.finalProject.service.impl.LookupService;
import com.finalProject.service.impl.PricingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/pricing/")
public class PricingController {
    private final PricingService pricingService;

    public PricingController(PricingService pricingService, LookupService lookupService) {
        this.pricingService = pricingService;
    }

    @PreAuthorize("hasRole('SALES')")
    @PostMapping("/calculateTotalPrice")
    public ResponseEntity<ApiResponse<TotalPriceDto>> calculateTotalPrice(
            @RequestBody CalculateTotalPriceRequestDto requestDto){
        TotalPriceDto totalPriceDto =
                pricingService.calculateTotalPrice(requestDto);
        ApiResponse<TotalPriceDto> response=new ApiResponse<>(
                "Total price fetched successfully", totalPriceDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PreAuthorize("hasRole('PRICING')")
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

    @PreAuthorize("hasRole('PRICING')")
    @PostMapping("/addons")
    public ResponseEntity<ApiResponse<String>> addAddons(
            @RequestBody AddOnRequestDto requestDto) {

        String response = pricingService.addAddOns(requestDto);

        ApiResponse<String> apiResponse = new ApiResponse<>(
                "AddOn added successfully",
                response
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PreAuthorize("hasRole('PRICING')")
    @PutMapping("/parts/prices")
    public ResponseEntity<ApiResponse<BulkUpdateResponseDto>> updatePartPrices(
            @RequestBody BulkUpdatePartRequestDto requestDto) {

        BulkUpdateResponseDto response = pricingService.updatePartPrices(requestDto);
        ApiResponse<BulkUpdateResponseDto> apiResponse = new ApiResponse<>(
                        "Part prices updated successfully", response);

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('PRICING')")
    @PutMapping("/addons/prices")
    public ResponseEntity<ApiResponse<BulkUpdateResponseDto>> updateAddOnPrices(
            @RequestBody BulkUpdateAddOnRequestDto requestDto) {

        BulkUpdateResponseDto response = pricingService.updateAddOnPrices(requestDto);

        ApiResponse<BulkUpdateResponseDto> apiResponse = new ApiResponse<>(
                        "Add-on prices updated successfully", response);

        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasRole('PRICING')")
    @GetMapping("/parts/search")
    public ResponseEntity<ApiResponse<List<PartPricingResponseDto>>> searchParts(
            @RequestParam(required = false) String keyword) {

        List<PartPricingResponseDto> result =
                pricingService.searchParts(keyword);

        ApiResponse<List<PartPricingResponseDto>> response =
                new ApiResponse<>(
                        "Parts fetched successfully.",
                        result
                );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PRICING')")
    @PutMapping("/parts/{partId}")
    public ResponseEntity<ApiResponse<String>> updatePart(
            @PathVariable Long partId,
            @RequestBody UpdatePartRequestDto requestDto) {

        String result =
                pricingService.updatePart(partId, requestDto);

        ApiResponse<String> response =
                new ApiResponse<>(
                        "Part updated successfully.",
                        result
                );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PRICING')")
    @PutMapping("/parts/{partId}/activate")
    public ResponseEntity<ApiResponse<String>> activatePart(
            @PathVariable Long partId) {

        String result =
                pricingService.activatePart(partId);

        ApiResponse<String> response =
                new ApiResponse<>(
                        "Part activated successfully.",
                        result
                );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PRICING')")
    @PutMapping("/parts/{partId}/deactivate")
    public ResponseEntity<ApiResponse<String>> deactivatePart(
            @PathVariable Long partId) {

        String result =
                pricingService.deactivatePart(partId);

        ApiResponse<String> response =
                new ApiResponse<>(
                        "Part deactivated successfully.",
                        result
                );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PRICING')")
    @GetMapping("/addons/search")
    public ResponseEntity<ApiResponse<List<AddOnPricingResponseDto>>> searchAddOns(
            @RequestParam(required = false) String keyword) {

        List<AddOnPricingResponseDto> result =
                pricingService.searchAddOns(keyword);

        ApiResponse<List<AddOnPricingResponseDto>> response =
                new ApiResponse<>(
                        "Add-ons fetched successfully.",
                        result
                );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PRICING')")
    @PutMapping("/addons/{addOnId}")
    public ResponseEntity<ApiResponse<String>> updateAddOn(
            @PathVariable Long addOnId,
            @RequestBody UpdateAddOnRequestDto requestDto) {

        String result =
                pricingService.updateAddOn(addOnId, requestDto);

        ApiResponse<String> response =
                new ApiResponse<>(
                        "Add-on updated successfully.",
                        result
                );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PRICING')")
    @PutMapping("/addons/{addOnId}/activate")
    public ResponseEntity<ApiResponse<String>> activateAddOn(
            @PathVariable Long addOnId) {

        String result =
                pricingService.activateAddOn(addOnId);

        ApiResponse<String> response =
                new ApiResponse<>(
                        "Add-on activated successfully.",
                        result
                );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PRICING')")
    @PutMapping("/addons/{addOnId}/deactivate")
    public ResponseEntity<ApiResponse<String>> deactivateAddOn(
            @PathVariable Long addOnId) {

        String result =
                pricingService.deactivateAddOn(addOnId);

        ApiResponse<String> response =
                new ApiResponse<>(
                        "Add-on deactivated successfully.",
                        result
                );

        return ResponseEntity.ok(response);
    }




}
