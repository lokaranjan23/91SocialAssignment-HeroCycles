package com.finalProject.requestDto;

import java.util.List;

public class CreateVariantRequestDto {
    private String name;

    private Long bikeConfigId;

    private List<VariantPartRequestDto> partDtos;

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

    public List<VariantPartRequestDto> getPartDtos() {
        return partDtos;
    }

    public void setPartDtos(List<VariantPartRequestDto> partDtos) {
        this.partDtos = partDtos;
    }
}
