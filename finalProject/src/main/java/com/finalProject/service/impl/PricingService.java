package com.finalProject.service.impl;

import com.finalProject.entity.*;
import com.finalProject.enums.PartCategory;
import com.finalProject.enums.VariantStatus;
import com.finalProject.exception.*;
import com.finalProject.repository.*;
import com.finalProject.requestDto.*;
import com.finalProject.responseDto.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class PricingService implements com.finalProject.service.PricingService {

    private final VariantPartRepository variantPartRepository;
    private final PartRepository partRepository;
    private final ConfigurationAddOnRepository configurationAddOnRepository;
    private final VariantRepository variantRepository;
    private final AddOnRepository addOnRepository;
    private final PartCategoryRuleRepository partCategoryRuleRepository;

    public PricingService(VariantPartRepository variantPartRepository,
                          PartRepository partRepository,
                          ConfigurationAddOnRepository configurationAddOnRepository,
                          VariantRepository variantRepository,
                          AddOnRepository addOnRepository,
                          PartCategoryRuleRepository partCategoryRuleRepository) {
        this.variantPartRepository = variantPartRepository;
        this.partRepository = partRepository;
        this.configurationAddOnRepository = configurationAddOnRepository;
        this.variantRepository=variantRepository;
        this.addOnRepository = addOnRepository;
        this.partCategoryRuleRepository = partCategoryRuleRepository;
    }

    @Transactional
    public String addParts(PartRequestDto requestDto){
        if(partRepository.existsByName(requestDto.getPartName())){
            throw new DuplicatePartException("Part name already exists");
        }
        Part part= new Part();
        part.setName(requestDto.getPartName());
        part.setCurrentPrice(requestDto.getCurrentPrice());
        PartCategoryRule category=partCategoryRuleRepository.findByCategory(requestDto.getCategory()).orElseThrow(
                ()->new PartCategoryNotFoundException("Category not found")
        );
        part.setPartCategoryRule(category);
        partRepository.save(part);
        return "Part added successful";
    }

    @Transactional
    public String addAddOns(AddOnRequestDto requestDto){
        if(addOnRepository.existsByName(requestDto.getAddOnName())){
            throw new DuplicateAddOnException("AddOn with name:"+requestDto.getAddOnName()+" " +
                    "already exists");
        }
        AddOn addOn=new AddOn();
        addOn.setName(requestDto.getAddOnName());
        addOn.setCurrentPrice(requestDto.getCurrentPrice());
        addOnRepository.save(addOn);
        return "Part added successful";
    }


    public Float calculateVariantPrice(Long variantId){
        List<VariantPart> variantParts= variantPartRepository.findByVariantId(variantId);

        float total = 0f;
        for(VariantPart varPart : variantParts){

            Part part = partRepository.findById(varPart.getPartId()).orElseThrow(
                    ()->new PartNotFoundException("Part not found")
            );

            total += part.getCurrentPrice() * part.getPartCategoryRule().getDefaultQuantity();
        }
        return total;}

    public AddOnResponseDto calculateAddonsPrices(List<Long> addonIds ){
        float total = 0f;
        List<AddOn> addons = addOnRepository.findByIdIn(addonIds);
        AddOnResponseDto response = new AddOnResponseDto();
        for(AddOn addon :addons){
            total = total + addon.getCurrentPrice();
        }
        response.setAddons(addons);
        response.setTotalPrice(total);
        return response;
    }

    public List<VariantPriceDto> getVariants(Long bikeConfigId){
        List<VariantPriceDto> result = new ArrayList<>();
        List<Variant> variants= variantRepository.findByBikeConfigurationIdAndStatus(bikeConfigId,
                VariantStatus.ACTIVE);
        for(Variant variant : variants){
            VariantPriceDto dto=new VariantPriceDto();
            dto.setVariantName(variant.getName());
            dto.setVariantPrice(variant.getCurrentPrice());
            result.add(dto);
        }
        return result;
    }

    public List<AddonsWithPriceResponseDto> getValidAddOnsWithPrices(Long bikeConfigId){
        List<AddonsWithPriceResponseDto> result = new ArrayList<>();
        List<Long>  addOnIds= configurationAddOnRepository.findByConfigurationId(bikeConfigId);
        List<AddOn> addOns = addOnRepository.findByIdIn(addOnIds);
        for(AddOn addOn : addOns){
            AddonsWithPriceResponseDto dto = new AddonsWithPriceResponseDto();
            dto.setAddonName(addOn.getName());
            dto.setAddonPrice(addOn.getCurrentPrice());
            result.add(dto);
        }
        return result;
    }

    public TotalPriceDto calculateTotalPrice(CalculateTotalPriceRequestDto requestDto){
        Variant variant= variantRepository.findById(requestDto.getVariantId()).orElseThrow(
                ()-> new VariantNotFoundException("Variant not found")
        );
        AddOnResponseDto addOnResponse = calculateAddonsPrices(requestDto.getAddonIds());
        float finalTotal = variant.getCurrentPrice()
                + addOnResponse.getTotalPrice();
        return new TotalPriceDto(variant.getCurrentPrice(), addOnResponse, finalTotal);
    }


    @Transactional
    public BulkUpdateResponseDto updatePartPrices(BulkUpdatePartRequestDto requestDto){
        List<Long> partIds=new ArrayList<>(requestDto.getPartPrices().keySet());
        List<Part> parts = partRepository.findByIdIn(partIds);

        List<Long> successIds = new ArrayList<>();
        List<FailureDto> failures = new ArrayList<>();

        for(Part part: parts){
            try {
                part.setNewPrice(requestDto.getPartPrices().get(part.getId()));
                part.setEffectiveFrom(requestDto.getEffectiveFrom());
                successIds.add(part.getId());

            }catch (InvalidDataEnteredException ex){
                failures.add(new FailureDto(part.getId(),ex.getMessage()));
            }
        }
        return new BulkUpdateResponseDto(successIds,failures);
    }

    @Transactional
    public BulkUpdateResponseDto updateAddOnPrices(BulkUpdateAddOnRequestDto requestDto){
        List<Long> addOnIds=new ArrayList<>(requestDto.getAddOnPrices().keySet());
        List<AddOn> addOns = addOnRepository.findByIdIn(addOnIds);

        List<Long> successIds = new ArrayList<>();
        List<FailureDto> failures = new ArrayList<>();

        for(AddOn addOn: addOns){
            try {
                addOn.setNewPrice(requestDto.getAddOnPrices().get(addOn.getId()));
                addOn.setEffectiveFrom(requestDto.getEffectiveFrom());
                successIds.add(addOn.getId());

            }catch (InvalidDataEnteredException ex){
                failures.add(new FailureDto(addOn.getId(),ex.getMessage()));
            }
        }
        return new BulkUpdateResponseDto(successIds,failures);
    }

    public List<PartResponseDto> getAllParts() {

        List<Part> parts = partRepository.findAll();
        List<PartResponseDto> response = new ArrayList<>();

        for (Part part : parts) {
            PartResponseDto dto = new PartResponseDto();
            dto.setId(part.getId());
            dto.setPartName(part.getName());
            response.add(dto);
        }
        return response;
    }

    public List<AddOnDropdownResponseDto> getAllAddOns() {

        List<AddOn> addOns = addOnRepository.findAll();

        List<AddOnDropdownResponseDto> response = new ArrayList<>();

        for (AddOn addOn : addOns) {
            AddOnDropdownResponseDto dto = new AddOnDropdownResponseDto();
            dto.setId(addOn.getId());
            dto.setName(addOn.getName());
            response.add(dto);
        }
        return response;
    }

    @Transactional
    public void updateEffectivePrices() {
        List<Part> parts=partRepository
                .findByNewPriceIsNotNullAndEffectiveFromLessThanEqual(LocalDate.now());
        List<Long> partIds=new ArrayList<>();
        for(Part part:parts){
            part.setCurrentPrice(part.getNewPrice());
            part.setNewPrice(null);
            part.setEffectiveFrom(null);
            partIds.add(part.getId());
        }
        List<Long> variantIds = variantPartRepository
                .findDistinctVariantIdsByPartIdIn(partIds);

        for(Long variantId:variantIds){
            Variant variant = variantRepository.findById(variantId)
                    .orElseThrow(() -> new RuntimeException("Variant not found"));

            variant.setCurrentPrice(calculateVariantPrice(variantId));

        }


    }

    @Transactional
    public void updateEffectiveAddOnPrices() {
        List<AddOn> addOns = addOnRepository
                .findByNewPriceIsNotNullAndEffectiveFromLessThanEqual(LocalDate.now());
        for (AddOn addOn : addOns) {
            addOn.setCurrentPrice(addOn.getNewPrice());
            addOn.setNewPrice(null);
            addOn.setEffectiveFrom(null);
        }
    }
}
