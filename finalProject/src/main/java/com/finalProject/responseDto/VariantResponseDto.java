package com.finalProject.responseDto;

import com.finalProject.enums.VariantStatus;

public class VariantResponseDto {
    private Long id;
    private String name;
    private Float currentPrice;
    private VariantStatus status;
    private String bikeConfigurationName;
    public VariantResponseDto() {
    }

    public VariantResponseDto(Long id, String name,
                              Float currentPrice,VariantStatus status
                                ,String bikeConfigurationName) {
        this.bikeConfigurationName=bikeConfigurationName;
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
        this.status=status;
    }

    public String getBikeConfigurationName() {
        return bikeConfigurationName;
    }

    public void setBikeConfigurationName(String bikeConfigurationName) {
        this.bikeConfigurationName = bikeConfigurationName;
    }

    public VariantStatus getStatus() {
        return status;
    }

    public void setStatus(VariantStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Float currentPrice) {
        this.currentPrice = currentPrice;
    }
}
