package com.finalProject.service;

import com.finalProject.requestDto.BulkUpdateAddOnRequestDto;
import com.finalProject.requestDto.BulkUpdatePartRequestDto;
import com.finalProject.requestDto.CalculateTotalPriceRequestDto;
import com.finalProject.requestDto.PartRequestDto;
import com.finalProject.responseDto.*;

import java.util.List;

public interface PricingService {
    Float calculateVariantPrice(Long variantId);
    AddOnResponseDto calculateAddonsPrices(List<Long> addonIds );
    List<VariantPriceDto> getVariants(Long bikeConfigId);
    List<AddonsWithPriceResponseDto> getValidAddOnsWithPrices(Long bikeConfigId);
    TotalPriceDto calculateTotalPrice(CalculateTotalPriceRequestDto requestDto);
    BulkUpdateResponseDto updatePartPrices(BulkUpdatePartRequestDto requestDto);
    String addParts(PartRequestDto requestDto);
    BulkUpdateResponseDto updateAddOnPrices(BulkUpdateAddOnRequestDto requestDto);
    void updateEffectivePrices();
    void updateEffectiveAddOnPrices();
}
