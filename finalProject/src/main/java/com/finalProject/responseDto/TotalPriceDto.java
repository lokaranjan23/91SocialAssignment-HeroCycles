package com.finalProject.responseDto;

public class TotalPriceDto {

        float variantPrice;
        AddOnResponseDto addonResponse;
        float finalTotal;

    public TotalPriceDto(float variantPrice, AddOnResponseDto addonResponse, float finalTotal) {
    }

    public float getVariantPrice() {
        return variantPrice;
    }

    public void setVariantPrice(float variantPrice) {
        this.variantPrice = variantPrice;
    }

    public AddOnResponseDto getAddonResponse() {
        return addonResponse;
    }

    public void setAddonResponse(AddOnResponseDto addonResponse) {
        this.addonResponse = addonResponse;
    }

    public float getFinalTotal() {
        return finalTotal;
    }

    public void setFinalTotal(float finalTotal) {
        this.finalTotal = finalTotal;
    }
}
