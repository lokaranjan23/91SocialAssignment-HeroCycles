package com.finalProject.service.impl;

import com.finalProject.entity.*;
import com.finalProject.enums.VariantStatus;
import com.finalProject.exception.DuplicatePartCategoryException;
import com.finalProject.exception.InvalidDataEnteredException;
import com.finalProject.exception.PartNotFoundException;
import com.finalProject.repository.*;
import com.finalProject.requestDto.VariantPartRequestDto;
import com.finalProject.responseDto.BikeConfigurationResponseDto;
import com.finalProject.responseDto.VariantResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ConfigService {

    private final BikeConfigurationRepository bikeConfigurationRepository;
    private final VariantRepository variantRepository;
    private final PartRepository partRepository;
    private final VariantPartRepository variantPartRepository;
    private final PricingService pricingService;
    private final PartCategoryRuleRepository partCategoryRuleRepository;

    public ConfigService(BikeConfigurationRepository bikeConfigurationRepository, VariantRepository variantRepository, PartRepository partRepository, VariantPartRepository variantPartRepository, PricingService pricingService, PartCategoryRuleRepository partCategoryRuleRepository) {
        this.bikeConfigurationRepository = bikeConfigurationRepository;
        this.variantRepository = variantRepository;
        this.partRepository = partRepository;
        this.variantPartRepository = variantPartRepository;
        this.pricingService = pricingService;
        this.partCategoryRuleRepository = partCategoryRuleRepository;
    }


    public BikeConfigurationResponseDto createBikeConfig(String name){
        BikeConfiguration bikeConfig = new BikeConfiguration();
        bikeConfig.setName(name);
        bikeConfigurationRepository.save(bikeConfig);
        BikeConfigurationResponseDto dto=new BikeConfigurationResponseDto();
        dto.setName(name);
        return dto;
    }

    @Transactional
    public VariantResponseDto createVariant(String name,
                                            Long bikeConfigId,
                                            List<VariantPartRequestDto> partDtos) {

        validateVariantParts(partDtos);
        BikeConfiguration bikeConfiguration = bikeConfigurationRepository
                .findById(bikeConfigId)
                .orElseThrow(() ->
                        new RuntimeException("Bike Configuration not found"));

        Variant variant = new Variant();
        variant.setName(name);
        variant.setBikeConfiguration(bikeConfiguration);
        variant.setStatus(VariantStatus.ACTIVE);

        variant = variantRepository.save(variant);

        float totalPrice = 0f;

        for (VariantPartRequestDto dto : partDtos) {
            Part part = partRepository.findById(dto.getPartId()).orElseThrow(() ->
                            new PartNotFoundException("Part not found"));
            VariantPart variantPart = new VariantPart();
            variantPart.setVariantId(variant.getId());
            variantPart.setPartId(part.getId());
            variantPartRepository.save(variantPart);
            totalPrice=pricingService.calculateVariantPrice(variant.getId());
        }
        variant.setCurrentPrice(totalPrice);
        variantRepository.save(variant);
        return new VariantResponseDto(variant.getId(), variant.getName(),
                variant.getCurrentPrice()
        );
    }

    private void validateVariantParts(List<VariantPartRequestDto> partDtos) {

        Set<Long> selectedCategoryIds = new HashSet<>();

        for (VariantPartRequestDto dto : partDtos) {

            Part part = partRepository.findById(dto.getPartId())
                    .orElseThrow(() ->
                            new PartNotFoundException("Part not found"));

            Long categoryId = part.getPartCategoryRule().getId();

            if (!selectedCategoryIds.add(categoryId)) {
                throw new DuplicatePartCategoryException(
                        "Only one part can be selected for category: "
                                + part.getPartCategoryRule().getCategory());
            }
        }

        List<PartCategoryRule> allCategories = partCategoryRuleRepository.findAll();

        if (selectedCategoryIds.size() != allCategories.size()) {
            throw new InvalidDataEnteredException(
                    "Exactly one part must be selected for every part category.");
        }
    }

}
