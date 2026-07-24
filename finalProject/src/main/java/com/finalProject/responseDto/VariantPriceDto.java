package com.finalProject.responseDto;

public class VariantPriceDto {
        Long variantId;
        String variantName;
        Float variantPrice;

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public Float getVariantPrice() {
        return variantPrice;
    }

    public void setVariantPrice(Float variantPrice) {
        this.variantPrice = variantPrice;
    }
}
