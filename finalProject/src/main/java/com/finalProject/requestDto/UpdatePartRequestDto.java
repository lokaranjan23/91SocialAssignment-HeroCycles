package com.finalProject.requestDto;

import jakarta.validation.constraints.NotBlank;

public class UpdatePartRequestDto {

    @NotBlank(message = "Part name is required")
    private String partName;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }
}
