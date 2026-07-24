package com.finalProject.service.impl;

import com.finalProject.entity.*;
import com.finalProject.enums.PartCategory;
import com.finalProject.enums.Status;
import com.finalProject.enums.VariantStatus;
import com.finalProject.exception.BikeConfigurationNotFoundException;
import com.finalProject.repository.*;
import com.finalProject.responseDto.*;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LookupService {


    private final BikeConfigurationRepository bikeConfigurationRepository;
    private final PartCategoryRuleRepository partCategoryRuleRepository;
    private final PartRepository partRepository;
    private final AddOnRepository addOnRepository;
    private final ConfigurationAddOnRepository configurationAddOnRepository;
    private final VariantRepository variantRepository;

    public LookupService(BikeConfigurationRepository bikeConfigurationRepository, PartCategoryRuleRepository partCategoryRuleRepository, PartRepository partRepository, AddOnRepository addOnRepository, ConfigurationAddOnRepository configurationAddOnRepository, VariantRepository variantRepository) {
        this.bikeConfigurationRepository = bikeConfigurationRepository;
        this.partCategoryRuleRepository = partCategoryRuleRepository;
        this.partRepository = partRepository;
        this.addOnRepository = addOnRepository;
        this.configurationAddOnRepository = configurationAddOnRepository;
        this.variantRepository = variantRepository;
    }

    public List<BikeConfigurationResponseDto> getAllBikeConfigurations() {

        List<BikeConfiguration> bikeConfigurations = bikeConfigurationRepository.findAll();

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
        List<PartCategoryRule> categories = partCategoryRuleRepository.findAll();
        List<PartCategoryResponseDto> response = new ArrayList<>();
        for (PartCategoryRule category : categories) {

            PartCategoryResponseDto dto = new PartCategoryResponseDto();

            dto.setId(category.getId());
            dto.setCategory(category.getCategory());

            response.add(dto);
        }

        return response;
    }

    public List<PartResponseDto> getPartsByCategory(PartCategory category) {

        List<Part> parts = partRepository.findByStatusAndPartCategoryRuleCategory(Status.ACTIVE,category);

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

        List<AddOn> addOns = addOnRepository.findAllByStatus(Status.ACTIVE);
        List<AddOnDropdownResponseDto> response =new ArrayList<>();

        for (AddOn addOn : addOns) {
            AddOnDropdownResponseDto dto = new AddOnDropdownResponseDto();
            dto.setId(addOn.getId());
            dto.setName(addOn.getName());

            response.add(dto);
        }

        return response;
    }

    public List<VariantPriceDto> getVariants(Long bikeConfigId){
        List<VariantPriceDto> result = new ArrayList<>();
        if(!bikeConfigurationRepository.existsById(bikeConfigId)){
            throw new BikeConfigurationNotFoundException("Bike configuration not found");
        }
        List<Variant> variants= variantRepository.findByBikeConfigurationIdAndStatus(bikeConfigId,
                VariantStatus.ACTIVE);
        for(Variant variant : variants){
            VariantPriceDto dto=new VariantPriceDto();
            dto.setVariantId(variant.getId());
            dto.setVariantName(variant.getName());
            dto.setVariantPrice(variant.getCurrentPrice());
            result.add(dto);
        }
        return result;
    }

    public List<AddonsWithPriceResponseDto> getValidAddOnsWithPrices(Long bikeConfigId){
        List<AddonsWithPriceResponseDto> result = new ArrayList<>();
        if(!bikeConfigurationRepository.existsById(bikeConfigId)){
            throw new BikeConfigurationNotFoundException("Bike configuration not found");
        }
        List<Long>  addOnIds= configurationAddOnRepository.findByConfigurationId(bikeConfigId);
        List<AddOn> addOns = addOnRepository.findByStatusAndIdIn(Status.ACTIVE,addOnIds);
        for(AddOn addOn : addOns){
            AddonsWithPriceResponseDto dto = new AddonsWithPriceResponseDto();
            dto.setAddonId(addOn.getId());
            dto.setAddonName(addOn.getName());
            dto.setAddonPrice(addOn.getCurrentPrice());
            result.add(dto);
        }
        return result;
    }

    public List<PartResponseDto> getAllParts() {

        List<Part> parts = partRepository.findAllByStatus(Status.ACTIVE);
        List<PartResponseDto> response = new ArrayList<>();

        for (Part part : parts) {
            PartResponseDto dto = new PartResponseDto();
            dto.setId(part.getId());
            dto.setPartName(part.getName());
            response.add(dto);
        }
        return response;
    }

    public List<PartPricingResponseDto> getPricingParts() {

        List<Part> parts = partRepository.findAll();
        List<PartPricingResponseDto> response = new ArrayList<>();

        for (Part part : parts) {
            PartPricingResponseDto dto = new PartPricingResponseDto();
            dto.setId(part.getId());
            dto.setPartName(part.getName());
            dto.setCategory(part.getPartCategoryRule().getCategory().name());
            dto.setStatus(part.getStatus());
            dto.setCurrentPrice(part.getCurrentPrice());
            dto.setNewPrice(part.getNewPrice());
            dto.setEffectiveFrom(part.getEffectiveFrom());

            response.add(dto);
        }
        return response;
    }

    public List<AddOnPricingResponseDto> getPricingAddOns() {

        List<AddOn> addOns = addOnRepository.findAll();
        List<AddOnPricingResponseDto> response = new ArrayList<>();
        for (AddOn addOn : addOns) {
            AddOnPricingResponseDto dto = new AddOnPricingResponseDto();
            dto.setId(addOn.getId());
            dto.setName(addOn.getName());
            dto.setCurrentPrice(addOn.getCurrentPrice());
            dto.setStatus(addOn.getStatus());
            dto.setNewPrice(addOn.getNewPrice());
            dto.setEffectiveFrom(addOn.getEffectiveFrom());

            response.add(dto);
        }
        return response;
    }
}
