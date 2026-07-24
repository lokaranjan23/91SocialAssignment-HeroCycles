package com.finalProject.requestDto;

import jakarta.validation.constraints.NotBlank;

public class UpdateAddOnRequestDto {

    @NotBlank(message = "Add-on name is required.")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
