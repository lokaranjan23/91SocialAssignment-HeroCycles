package com.finalProject.requestDto;

import com.finalProject.enums.PartCategory;

public class PartRequestDto {
    private String partName;
    private PartCategory category;
    private Float currentPrice;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public PartCategory getCategory() {
        return category;
    }

    public void setCategory(PartCategory category) {
        this.category = category;
    }

    public Float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Float currentPrice) {
        this.currentPrice = currentPrice;
    }
}
