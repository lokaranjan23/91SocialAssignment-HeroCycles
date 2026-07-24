package com.finalProject.requestDto;

import com.finalProject.enums.VariantStatus;

public class UpdateVariantStatusRequestDto {

    private VariantStatus status;

    public VariantStatus getStatus() {
        return status;
    }

    public void setStatus(VariantStatus status) {
        this.status = status;
    }
}