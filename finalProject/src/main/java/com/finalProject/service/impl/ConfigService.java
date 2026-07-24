package com.finalProject.service.impl;

import com.finalProject.entity.*;
import com.finalProject.enums.Status;
import com.finalProject.enums.VariantStatus;
import com.finalProject.exception.*;
import com.finalProject.repository.*;
import com.finalProject.requestDto.*;
import com.finalProject.responseDto.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConfigService {

    private final BikeConfigurationRepository bikeConfigurationRepository;
    private final VariantRepository variantRepository;
    private final PartRepository partRepository;
    private final VariantPartRepository variantPartRepository;
    private final PricingService pricingService;
    private final PartCategoryRuleRepository partCategoryRuleRepository;
    private final ConfigurationAddOnRepository configurationAddOnRepository;
    private final AddOnRepository addOnRepository;

    public ConfigService(BikeConfigurationRepository bikeConfigurationRepository, VariantRepository variantRepository, PartRepository partRepository, VariantPartRepository variantPartRepository, PricingService pricingService, PartCategoryRuleRepository partCategoryRuleRepository, ConfigurationAddOnRepository configurationAddOnRepository, AddOnRepository addOnRepository) {
        this.bikeConfigurationRepository = bikeConfigurationRepository;
        this.variantRepository = variantRepository;
        this.partRepository = partRepository;
        this.variantPartRepository = variantPartRepository;
        this.pricingService = pricingService;
        this.partCategoryRuleRepository = partCategoryRuleRepository;
        this.configurationAddOnRepository = configurationAddOnRepository;
        this.addOnRepository = addOnRepository;
    }


    public BikeConfigurationResponseDto createBikeConfig(BikeConfigurationRequestDto requestDto){
        if (bikeConfigurationRepository.existsByName(requestDto.getBikeConfigurationName())) {
            throw new DuplicateBikeConfigurationException(
                    "Bike configuration already exists");
        }
        BikeConfiguration bikeConfig = new BikeConfiguration();
        bikeConfig.setName(requestDto.getBikeConfigurationName());
        bikeConfigurationRepository.save(bikeConfig);
        BikeConfigurationResponseDto dto=new BikeConfigurationResponseDto();
        System.out.println(bikeConfig.getId());
        dto.setBikeConfigurationId(bikeConfig.getId());
        dto.setBikeConfigurationName(requestDto.getBikeConfigurationName());
        return dto;
    }

    @Transactional
    public VariantResponseDto createVariant(CreateVariantRequestDto requestDto) {

        validateVariantParts(requestDto.getPartDto());
        validateDuplicateVariant(requestDto.getBikeConfigId(), requestDto.getName(),
                requestDto.getPartDto());
        BikeConfiguration bikeConfiguration = bikeConfigurationRepository
                .findById(requestDto.getBikeConfigId())
                .orElseThrow(() ->
                        new BikeConfigurationNotFoundException("Bike Configuration not found"));

        Variant variant = new Variant();
        variant.setName(requestDto.getName().toLowerCase());
        variant.setBikeConfiguration(bikeConfiguration);
        variant.setStatus(VariantStatus.ACTIVE);
        variant.setCurrentPrice(0f);
        variant = variantRepository.save(variant);


        for (VariantPartRequestDto dto : requestDto.getPartDto()) {
            Part part = partRepository.findByIdAndStatus(dto.getPartId(), Status.ACTIVE)
                    .orElseThrow(() ->
                            new PartNotFoundException("Part not found"));
            VariantPart variantPart = new VariantPart();
            variantPart.setVariantId(variant.getId());
            variantPart.setPartId(part.getId());
            variantPartRepository.save(variantPart);

        }
        float totalPrice=pricingService.calculateVariantPrice(variant.getId());
        variant.setCurrentPrice(totalPrice);
        variantRepository.save(variant);
        return new VariantResponseDto(variant.getId(), variant.getName(),
                variant.getCurrentPrice(),variant.getStatus(),bikeConfiguration.getName());
    }

    private void validateDuplicateVariant(Long bikeConfigId, String name,
                                          List<VariantPartRequestDto> partDto) {

        Set<Long> requestedPartIds = new HashSet<>();

        for (VariantPartRequestDto dto : partDto) {
            requestedPartIds.add(dto.getPartId());
        }

        List<Variant> existingVariants =
                variantRepository.findByBikeConfigurationId(bikeConfigId);

        for (Variant variant : existingVariants) {
            if(variant.getName().equals(name)){
                throw new DuplicatePartException("Part with same name already exists");
            }
            List<VariantPart> variantParts =
                    variantPartRepository.findByVariantId(variant.getId());

            Set<Long> existingPartIds = new HashSet<>();

            for (VariantPart variantPart : variantParts) {
                existingPartIds.add(variantPart.getPartId());
            }

            if (existingPartIds.equals(requestedPartIds)) {
                throw new DuplicateVariantException(
                        "A variant with the same parts already exists.");
            }
        }
    }

    private void validateVariantParts(List<VariantPartRequestDto> partDto) {

        Set<Long> selectedCategoryIds = new HashSet<>();

        for (VariantPartRequestDto dto : partDto) {

            Part part = partRepository.findByIdAndStatus(dto.getPartId(), Status.ACTIVE)
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


    @Transactional
    public String linkAddOn(ConfigureAddOnsRequestDto requestDto) {

        if (requestDto.getBikeConfigurationId() == null) {
            throw new InvalidDataEnteredException("Bike Configuration is required.");
        }

        if (requestDto.getAddOnIds() == null || requestDto.getAddOnIds().isEmpty()) {
            throw new InvalidDataEnteredException("Please select at least one add-on.");
        }

        BikeConfiguration bikeConfiguration = bikeConfigurationRepository
                .findById(requestDto.getBikeConfigurationId())
                .orElseThrow(() ->
                        new BikeConfigurationNotFoundException("Bike Configuration not found"));

        Set<Long> uniqueAddOnIds = new HashSet<>();
        for (Long addOnId : requestDto.getAddOnIds()) {
            if (addOnId == null) {
                throw new InvalidDataEnteredException("Add-on ID cannot be null.");
            }

            if (!uniqueAddOnIds.add(addOnId)) {
                throw new DuplicateAddOnException("Duplicate add-on selected.");
            }
        }

        for (Long addOnId : requestDto.getAddOnIds()) {
            AddOn addOn = addOnRepository
                    .findByIdAndStatus(addOnId, Status.ACTIVE)
                    .orElseThrow(() ->
                            new AddOnNotFoundException("Add-on not found"));

            if (configurationAddOnRepository.existsByConfigurationIdAndAddOnId(
                    bikeConfiguration.getId(), addOn.getId())) {

                throw new DuplicateConfigurationAddOnException(
                        "Add-on already linked to this configuration.");
            }

            ConfigurationAddOn configurationAddOn = new ConfigurationAddOn();
            configurationAddOn.setConfigurationId(bikeConfiguration.getId());
            configurationAddOn.setAddOnId(addOn.getId());

            configurationAddOnRepository.save(configurationAddOn);
        }
        return "Add-ons linked successfully to Bike Configuration: "
                + bikeConfiguration.getName();
    }

    public List<VariantResponseDto> searchVariants(String keyword) {

        List<Variant> variants;

        if (keyword == null || keyword.isBlank()) {
            variants = variantRepository.findAll();
        } else {
            variants = variantRepository.findByNameContainingIgnoreCase(keyword);
        }

        List<VariantResponseDto> response = new ArrayList<>();

        for (Variant variant : variants) {

            VariantResponseDto dto = new VariantResponseDto();
            dto.setId(variant.getId());
            dto.setName(variant.getName());
            dto.setCurrentPrice(variant.getCurrentPrice());
            dto.setStatus(variant.getStatus());
            dto.setBikeConfigurationName(variant.getBikeConfiguration().getName());
            response.add(dto);
        }

        return response;
    }




    @Transactional
    public String updateVariantStatus(Long variantId, UpdateVariantStatusRequestDto requestDto) {

        Variant variant = variantRepository.findById(variantId).orElseThrow(
                () -> new VariantNotFoundException("Variant not found"));

        variant.setStatus(requestDto.getStatus());
        return "Variant status updated successfully.";
    }






}
