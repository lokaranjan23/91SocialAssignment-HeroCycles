package com.finalProject.requestDto;

import java.util.List;

public class CreateVariantRequestDto {
    private String name;

    private Long bikeConfigId;

    private List<VariantPartRequestDto> partDto;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBikeConfigId() {
        return bikeConfigId;
    }

    public void setBikeConfigId(Long bikeConfigId) {
        this.bikeConfigId = bikeConfigId;
    }

    public List<VariantPartRequestDto> getPartDto() {
        return partDto;
    }

    public void setPartDto(List<VariantPartRequestDto> partDto) {
        this.partDto = partDto;
    }
}
