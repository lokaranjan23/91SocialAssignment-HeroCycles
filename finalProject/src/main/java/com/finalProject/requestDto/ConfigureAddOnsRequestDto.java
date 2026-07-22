package com.finalProject.requestDto;

import java.util.List;

public class ConfigureAddOnsRequestDto {
    private Long bikeConfigurationId;

    private List<Long> addOnIds;

    public Long getBikeConfigurationId() {
        return bikeConfigurationId;
    }

    public void setBikeConfigurationId(Long bikeConfigurationId) {
        this.bikeConfigurationId = bikeConfigurationId;
    }

    public List<Long> getAddOnIds() {
        return addOnIds;
    }

    public void setAddOnIds(List<Long> addOnIds) {
        this.addOnIds = addOnIds;
    }
}
