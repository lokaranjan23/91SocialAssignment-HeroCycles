package com.finalProject.requestDto;

public class PartRequestDto {
    private String partName;
    private Long partCategoryId;
    private Float currentPrice;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Long getPartCategoryId() {
        return partCategoryId;
    }

    public void setPartCategoryId(Long partCategoryId) {
        this.partCategoryId = partCategoryId;
    }

    public Float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Float currentPrice) {
        this.currentPrice = currentPrice;
    }
}
