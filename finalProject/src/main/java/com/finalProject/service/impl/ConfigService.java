package com.finalProject.service.impl;

import com.finalProject.entity.*;
import com.finalProject.enums.PartCategory;
import com.finalProject.enums.VariantStatus;
import com.finalProject.exception.*;
import com.finalProject.repository.*;
import com.finalProject.requestDto.BikeConfigurationRequestDto;
import com.finalProject.requestDto.ConfigureAddOnsRequestDto;
import com.finalProject.requestDto.CreateVariantRequestDto;
import com.finalProject.requestDto.VariantPartRequestDto;
import com.finalProject.responseDto.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        validateDuplicateVariant(requestDto.getBikeConfigId(), requestDto.getPartDto());
        BikeConfiguration bikeConfiguration = bikeConfigurationRepository
                .findById(requestDto.getBikeConfigId())
                .orElseThrow(() ->
                        new BikeConfigurationNotFoundException("Bike Configuration not found"));

        Variant variant = new Variant();
        variant.setName(requestDto.getName());
        variant.setBikeConfiguration(bikeConfiguration);
        variant.setStatus(VariantStatus.ACTIVE);
        variant.setCurrentPrice(0f);
        variant = variantRepository.save(variant);


        for (VariantPartRequestDto dto : requestDto.getPartDto()) {
            Part part = partRepository.findById(dto.getPartId()).orElseThrow(() ->
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
                variant.getCurrentPrice()
        );
    }

    private void validateDuplicateVariant(Long bikeConfigId,
                                          List<VariantPartRequestDto> partDto) {

        Set<Long> requestedPartIds = new HashSet<>();

        for (VariantPartRequestDto dto : partDto) {
            requestedPartIds.add(dto.getPartId());
        }

        List<Variant> existingVariants =
                variantRepository.findByBikeConfigurationId(bikeConfigId);

        for (Variant variant : existingVariants) {

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


    @Transactional
    public String linkAddOn(ConfigureAddOnsRequestDto requestDto){
        BikeConfiguration bikeConfiguration=bikeConfigurationRepository
                .findById(requestDto.getBikeConfigurationId()).orElseThrow(
                        ()-> new BikeConfigurationNotFoundException("Bike Configuration not found")
                );

        for(Long addOnId:requestDto.getAddOnIds()){
            AddOn addOn = addOnRepository.findById(addOnId)
                    .orElseThrow(() ->
                            new AddOnNotFoundException("Add-on not found"));
            if (configurationAddOnRepository.existsByConfigurationIdAndAddOnId(
                            bikeConfiguration.getId(), addOn.getId())) {
                throw new DuplicateConfigurationAddOnException(
                        "Add-on already linked to this configuration");
            }
            ConfigurationAddOn configAddOn= new ConfigurationAddOn();
            configAddOn.setConfigurationId(bikeConfiguration.getId());
            configAddOn.setAddOnId(addOnId);
            configurationAddOnRepository.save(configAddOn);
        }
        return "AddOns linked to Bike Configuration"+bikeConfiguration.getId();

    }


    public List<BikeConfigurationResponseDto> getAllBikeConfigurations() {

        List<BikeConfiguration> bikeConfigurations =
                bikeConfigurationRepository.findAll();

        List<BikeConfigurationResponseDto> response = new ArrayList<>();

        for (BikeConfiguration bikeConfiguration : bikeConfigurations) {

            BikeConfigurationResponseDto dto =
                    new BikeConfigurationResponseDto();

            dto.setBikeConfigurationId(bikeConfiguration.getId());
            dto.setBikeConfigurationName(bikeConfiguration.getName());

            response.add(dto);
        }

        return response;
    }

    public List<PartCategoryResponseDto> getAllPartCategories() {

        List<PartCategoryRule> categories =
                partCategoryRuleRepository.findAll();

        List<PartCategoryResponseDto> response =
                new ArrayList<>();

        for (PartCategoryRule category : categories) {

            PartCategoryResponseDto dto =
                    new PartCategoryResponseDto();

            dto.setId(category.getId());
            dto.setCategory(category.getCategory());

            response.add(dto);
        }

        return response;
    }

    public List<PartResponseDto> getPartsByCategory(PartCategory category) {

        List<Part> parts = partRepository.findByPartCategoryRuleCategory(category);

        List<PartResponseDto> response =
                new ArrayList<>();

        for (Part part : parts) {

            PartResponseDto dto =
                    new PartResponseDto();

            dto.setId(part.getId());
            dto.setPartName(part.getName());

            response.add(dto);
        }

        return response;
    }

    public List<AddOnDropdownResponseDto> getAllAddOns() {

        List<AddOn> addOns = addOnRepository.findAll();
        List<AddOnDropdownResponseDto> response =new ArrayList<>();

        for (AddOn addOn : addOns) {
            AddOnDropdownResponseDto dto = new AddOnDropdownResponseDto();
            dto.setId(addOn.getId());
            dto.setName(addOn.getName());

            response.add(dto);
        }

        return response;
    }





}
