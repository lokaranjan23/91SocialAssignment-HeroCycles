package com.finalProject.requestDto;

import java.time.LocalDate;
import java.util.HashMap;

public class BulkUpdateAddOnRequestDto {
    private HashMap<Long,Float> addOnPrices;
    private LocalDate effectiveFrom;

    public HashMap<Long, Float> getAddOnPrices() {
        return addOnPrices;
    }

    public void setAddOnPrices(HashMap<Long, Float> addOnPrices) {
        this.addOnPrices = addOnPrices;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
}
