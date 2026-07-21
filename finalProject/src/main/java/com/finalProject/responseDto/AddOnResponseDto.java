package com.finalProject.responseDto;

import com.finalProject.entity.AddOn;

import java.util.List;

public class AddOnResponseDto {

        List<AddOn> addons;

        Float totalPrice;

    public List<AddOn> getAddons() {
        return addons;
    }

    public void setAddons(List<AddOn> addons) {
        this.addons = addons;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
