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

import java.time.LocalDate;
import java.util.*;

@Service
public class PricingService {

    private final VariantPartRepository variantPartRepository;
    private final PartRepository partRepository;
    private final VariantRepository variantRepository;
    private final AddOnRepository addOnRepository;
    private final PartCategoryRuleRepository partCategoryRuleRepository;
    public PricingService(VariantPartRepository variantPartRepository,
                          PartRepository partRepository,
                          VariantRepository variantRepository,
                          AddOnRepository addOnRepository,
                          PartCategoryRuleRepository partCategoryRuleRepository) {
        this.variantPartRepository = variantPartRepository;
        this.partRepository = partRepository;
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
        part.setName(requestDto.getPartName().toLowerCase());
        part.setCurrentPrice(requestDto.getCurrentPrice());
        PartCategoryRule category=partCategoryRuleRepository.findByCategory(requestDto.getCategory()).orElseThrow(
                ()->new PartCategoryNotFoundException("Category not found")
        );
        part.setStatus(Status.ACTIVE);
        part.setPartCategoryRule(category);
        partRepository.save(part);
        return "Part added successful";
    }

    @Transactional
    public String addAddOns(AddOnRequestDto requestDto){
        if(addOnRepository.existsByNameIgnoreCase(requestDto.getAddOnName())){
            throw new DuplicateAddOnException("AddOn with name:"+requestDto.getAddOnName()+" " +
                    "already exists");
        }
        AddOn addOn=new AddOn();
        addOn.setName(requestDto.getAddOnName().toLowerCase());
        addOn.setStatus(Status.ACTIVE);
        addOn.setCurrentPrice(requestDto.getCurrentPrice());
        addOnRepository.save(addOn);
        return "AddOn added successful";
    }


    public Float calculateVariantPrice(Long variantId){

        List<VariantPart> variantParts= variantPartRepository.findByVariantId(variantId);

        float total = 0f;
        for(VariantPart varPart : variantParts){

            Part part = partRepository.findById(varPart.getPartId())
                    .orElseThrow(() -> new PartNotFoundException("Part not found"));

            total += part.getCurrentPrice() * part.getPartCategoryRule().getDefaultQuantity();
        }
        return total;}

    public AddOnResponseDto calculateAddonsPrices(List<Long> addonIds) {
        Set<Long> uniqueIds = new HashSet<>();
        for (Long id : addonIds) {
            if (!uniqueIds.add(id)) {
                throw new DuplicateAddOnException("Duplicate add-on selected.");}
        }
        List<AddOn> addons = addOnRepository.findByIdIn(addonIds);
        if (addons.size() != addonIds.size()) {
            throw new AddOnNotFoundException("One or more add-ons were not found.");}

        float total = 0f;
        List<SelectedAddOnResponseDto> selectedAddOns = new ArrayList<>();
        for (AddOn addOn : addons) {
            SelectedAddOnResponseDto dto = new SelectedAddOnResponseDto();
            dto.setId(addOn.getId());
            dto.setName(addOn.getName());
            dto.setPrice(addOn.getCurrentPrice());
            selectedAddOns.add(dto);
            if (addOn.getStatus() == Status.INACTIVE) {
                throw new InvalidDataEnteredException(
                        "Selected add-on is inactive.");
            }

            total += addOn.getCurrentPrice();
        }
        AddOnResponseDto response = new AddOnResponseDto();
        response.setAddons(selectedAddOns);
        response.setTotalPrice(total);
        return response;
    }

    private List<PartBreakdownDto> getPartBreakDown(Long variantId){
        List<Long> partIds = variantPartRepository.findPartIdsByVariantId(variantId);
        List<PartBreakdownDto> responseDto=new ArrayList<>();

        for(Long id:partIds){
            Part part = partRepository.findByIdAndStatus(id, Status.ACTIVE).orElseThrow(
                    ()-> new PartNotFoundException("Active Part not found")
            );
            PartBreakdownDto dto=new PartBreakdownDto();
            dto.setCategory(part.getPartCategoryRule().getCategory().toString());
            dto.setPartName(part.getName());
            dto.setPrice(part.getCurrentPrice());
            responseDto.add(dto);
        }
        return responseDto;
    }



    public TotalPriceDto calculateTotalPrice(CalculateTotalPriceRequestDto requestDto) {

        Variant variant = variantRepository.findByIdAndStatus
                (requestDto.getVariantId(), VariantStatus.ACTIVE).orElseThrow(
                                () -> new VariantNotFoundException("Active variant not found"));
        List<PartBreakdownDto> partBreakDown = getPartBreakDown(variant.getId());
        AddOnResponseDto addOnResponse = calculateAddonsPrices(requestDto.getAddonIds());

        float finalTotal = variant.getCurrentPrice() + addOnResponse.getTotalPrice();
        return new TotalPriceDto(variant.getName(), partBreakDown ,addOnResponse
                ,variant.getCurrentPrice()
                ,finalTotal);
    }


    @Transactional
    public BulkUpdateResponseDto updatePartPrices(BulkUpdatePartRequestDto requestDto) {

        List<Long> partIds = new ArrayList<>(requestDto.getPartPrices().keySet());
        List<Part> parts = partRepository.findByIdIn(partIds);

        LocalDate effectiveFrom = requestDto.getEffectiveFrom();

        if (effectiveFrom == null || effectiveFrom.isBefore(LocalDate.now())) {
            throw new InvalidDataEnteredException(
                    "Effective date cannot be null or a past date");
        }

        Set<Long> foundIds = new HashSet<>();
        for (Part part : parts) {
            foundIds.add(part.getId());
        }

        for (Long id : partIds) {
            if (!foundIds.contains(id)) {
                throw new PartNotFoundException("Part not found");
            }
        }

        List<Long> successIds = new ArrayList<>();

        for (Part part : parts) {

            if (part.getStatus() == Status.INACTIVE) {
                throw new InvalidDataEnteredException(
                        "Cannot update price of an inactive part.");
            }

            Float newPrice = requestDto.getPartPrices().get(part.getId());

            if (newPrice == null) {
                throw new InvalidDataEnteredException("Price cannot be null.");
            }

            if (newPrice <= 0) {
                throw new InvalidDataEnteredException(
                        "Part price must be greater than 0.");
            }

            part.setNewPrice(newPrice);
            part.setEffectiveFrom(effectiveFrom);

            successIds.add(part.getId());
        }

        BulkUpdateResponseDto response = new BulkUpdateResponseDto();
        response.setSuccessIds(successIds);

        return response;
    }

    @Transactional
    public BulkUpdateResponseDto updateAddOnPrices(BulkUpdateAddOnRequestDto requestDto) {

        List<Long> addOnIds = new ArrayList<>(requestDto.getAddOnPrices().keySet());
        List<AddOn> addOns = addOnRepository.findByIdIn(addOnIds);

        LocalDate effectiveFrom = requestDto.getEffectiveFrom();

        if (effectiveFrom == null || effectiveFrom.isBefore(LocalDate.now())) {
            throw new InvalidDataEnteredException(
                    "Effective date cannot be null or a past date");
        }

        Set<Long> foundIds = new HashSet<>();
        for (AddOn addOn : addOns) {
            foundIds.add(addOn.getId());
        }

        for (Long id : addOnIds) {
            if (!foundIds.contains(id)) {
                throw new AddOnNotFoundException("Add-on not found");
            }
        }

        List<Long> successIds = new ArrayList<>();

        for (AddOn addOn : addOns) {

            if (addOn.getStatus() == Status.INACTIVE) {
                throw new InvalidDataEnteredException(
                        "Cannot update price of an inactive add-on.");
            }

            Float newPrice = requestDto.getAddOnPrices().get(addOn.getId());

            if (newPrice == null) {
                throw new InvalidDataEnteredException("Price cannot be null.");
            }

            if (newPrice <= 0) {
                throw new InvalidDataEnteredException(
                        "Add-on price must be greater than 0.");
            }

            addOn.setNewPrice(newPrice);
            addOn.setEffectiveFrom(effectiveFrom);

            successIds.add(addOn.getId());
        }

        BulkUpdateResponseDto response = new BulkUpdateResponseDto();
        response.setSuccessIds(successIds);

        return response;
    }

    public List<PartPricingResponseDto> searchParts(String keyword) {

        List<Part> parts;

        if (keyword == null || keyword.isBlank()) {
            parts = partRepository.findAll();
        } else {
            parts = partRepository.findByNameContainingIgnoreCase(keyword);
        }

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

    @Transactional
    public String updatePart(Long partId,
                             UpdatePartRequestDto requestDto) {

        Part part = partRepository.findById(partId)
                .orElseThrow(() ->
                        new PartNotFoundException("Part not found."));

        if (!part.getName().equalsIgnoreCase(requestDto.getPartName())
                && partRepository.existsByNameIgnoreCase(requestDto.getPartName())) {

            throw new DuplicatePartException(
                    "Part with this name already exists.");
        }

        part.setName(requestDto.getPartName());

        return "Part updated successfully.";
    }

    @Transactional
    public String deactivatePart(Long partId) {

        Part part = partRepository.findById(partId)
                .orElseThrow(() ->
                        new PartNotFoundException("Part not found"));

        if (part.getStatus() == Status.INACTIVE) {
            throw new InvalidDataEnteredException(
                    "Part is already inactive.");
        }

        part.setStatus(Status.INACTIVE);

        List<Long> variantIds =
                variantPartRepository.findDistinctVariantIdsByPartId(partId);

        if (!variantIds.isEmpty()) {variantRepository.updateVariantStatus(
                    variantIds,
                    VariantStatus.INACTIVE
            );
        }

        return "Part deactivated successfully.";
    }

    @Transactional
    public String activatePart(Long partId) {

        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new PartNotFoundException("Part not found"));

        if (part.getStatus() == Status.ACTIVE) {
            throw new InvalidDataEnteredException("Part is already active.");
        }
        part.setStatus(Status.ACTIVE);
        return "Part activated successfully.";
    }

    public List<AddOnPricingResponseDto> searchAddOns(String keyword) {

        List<AddOn> addOns;

        if (keyword == null || keyword.isBlank()) {
            addOns = addOnRepository.findAll();
        } else {
            addOns = addOnRepository.findByNameContainingIgnoreCase(keyword);
        }

        List<AddOnPricingResponseDto> response = new ArrayList<>();

        for (AddOn addOn : addOns) {

            AddOnPricingResponseDto dto = new AddOnPricingResponseDto();

            dto.setId(addOn.getId());
            dto.setName(addOn.getName());
            dto.setStatus(addOn.getStatus());
            dto.setCurrentPrice(addOn.getCurrentPrice());
            dto.setNewPrice(addOn.getNewPrice());
            dto.setEffectiveFrom(addOn.getEffectiveFrom());

            response.add(dto);
        }

        return response;
    }

    @Transactional
    public String updateAddOn(Long addOnId,
                              UpdateAddOnRequestDto requestDto) {

        AddOn addOn = addOnRepository.findById(addOnId)
                .orElseThrow(() ->
                        new AddOnNotFoundException("Add-on not found."));

        if (!addOn.getName().equalsIgnoreCase(requestDto.getName())
                && addOnRepository.existsByNameIgnoreCase(requestDto.getName())) {

            throw new DuplicateAddOnException(
                    "Add-on with this name already exists.");
        }

        addOn.setName(requestDto.getName());

        return "Add-on updated successfully.";
    }

    @Transactional
    public String activateAddOn(Long addOnId) {

        AddOn addOn = addOnRepository.findById(addOnId)
                .orElseThrow(() ->
                        new AddOnNotFoundException("Add-on not found."));

        if (addOn.getStatus() == Status.ACTIVE) {
            throw new InvalidDataEnteredException(
                    "Add-on is already active.");
        }

        addOn.setStatus(Status.ACTIVE);

        return "Add-on activated successfully.";
    }

    @Transactional
    public String deactivateAddOn(Long addOnId) {

        AddOn addOn = addOnRepository.findById(addOnId)
                .orElseThrow(() ->
                        new AddOnNotFoundException("Add-on not found."));

        if (addOn.getStatus() == Status.INACTIVE) {
            throw new InvalidDataEnteredException(
                    "Add-on is already inactive.");
        }

        addOn.setStatus(Status.INACTIVE);

        return "Add-on deactivated successfully.";
    }

    @Transactional
    public void updateEffectivePrices() {
        List<Part> parts=partRepository
                .findByStatusAndNewPriceIsNotNullAndEffectiveFromLessThanEqual(Status.ACTIVE,
                        LocalDate.now());
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
            if (variant.getStatus() != VariantStatus.ACTIVE) {
                continue;
            }
            variant.setCurrentPrice(calculateVariantPrice(variantId));

        }


    }

    @Transactional
    public void updateEffectiveAddOnPrices() {
        List<AddOn> addOns = addOnRepository
                .findByStatusAndNewPriceIsNotNullAndEffectiveFromLessThanEqual(Status.ACTIVE,
                        LocalDate.now());
        for (AddOn addOn : addOns) {
            addOn.setCurrentPrice(addOn.getNewPrice());
            addOn.setNewPrice(null);
            addOn.setEffectiveFrom(null);
        }
    }
}
