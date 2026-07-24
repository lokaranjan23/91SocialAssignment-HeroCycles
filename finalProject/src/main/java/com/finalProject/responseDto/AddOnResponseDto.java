package com.finalProject.responseDto;

import com.finalProject.entity.AddOn;

import java.util.List;

public class AddOnResponseDto {

        List<SelectedAddOnResponseDto> addons;

        Float totalPrice;

    public List<SelectedAddOnResponseDto> getAddons() {
        return addons;
    }

    public void setAddons(List<SelectedAddOnResponseDto> addons) {
        this.addons = addons;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
