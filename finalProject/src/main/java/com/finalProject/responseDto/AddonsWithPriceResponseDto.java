package com.finalProject.responseDto;

public class AddonsWithPriceResponseDto {
        Long addonId;
        String addonName;
        Float addonPrice;

    public Long getAddonId() {
        return addonId;
    }

    public void setAddonId(Long addonId) {
        this.addonId = addonId;
    }

    public String getAddonName() {
        return addonName;
    }

    public void setAddonName(String addonName) {
        this.addonName = addonName;
    }

    public Float getAddonPrice() {
        return addonPrice;
    }

    public void setAddonPrice(Float addonPrice) {
        this.addonPrice = addonPrice;
    }
}
