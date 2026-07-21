package com.finalProject.requestDto;

import java.time.LocalDate;
import java.util.HashMap;

public class BulkUpdatePartRequestDto {
    private HashMap<Long,Float> partPrices;
    private LocalDate effectiveFrom;

    public HashMap<Long, Float> getPartPrices() {
        return partPrices;
    }

    public void setPartPrices(HashMap<Long, Float> partPrices) {
        this.partPrices = partPrices;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
}
