package com.finalProject.responseDto;

import java.util.List;

public class TotalPriceDto {

    private String variantName;

    private List<PartBreakdownDto> partBreakdown;

    private AddOnResponseDto addOns;

    private Float variantPrice;

    private Float finalPrice;

    public TotalPriceDto(String variantName,
                         List<PartBreakdownDto> partBreakdown,
                         AddOnResponseDto addOns,
                         Float variantPrice,
                         Float finalPrice) {
        this.variantName = variantName;
        this.partBreakdown = partBreakdown;
        this.addOns = addOns;
        this.variantPrice = variantPrice;
        this.finalPrice = finalPrice;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public List<PartBreakdownDto> getPartBreakdown() {
        return partBreakdown;
    }

    public void setPartBreakdown(List<PartBreakdownDto> partBreakdown) {
        this.partBreakdown = partBreakdown;
    }

    public AddOnResponseDto getAddOns() {
        return addOns;
    }

    public void setAddOns(AddOnResponseDto addOns) {
        this.addOns = addOns;
    }

    public Float getVariantPrice() {
        return variantPrice;
    }

    public void setVariantPrice(Float variantPrice) {
        this.variantPrice = variantPrice;
    }


    public Float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Float finalPrice) {
        this.finalPrice = finalPrice;
    }
}

