package com.finalProject.requestDto;

import java.util.List;

public class CalculateTotalPriceRequestDto {
    private Long variantId;
    private List<Long> addonIds;

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public List<Long> getAddonIds() {
        return addonIds;
    }

    public void setAddonIds(List<Long> addonIds) {
        this.addonIds = addonIds;
    }
}
