package com.finalProject.responseDto;

public class PartBreakdownDto {

    private String category;

    private String partName;

    private Float price;

    public PartBreakdownDto() {
    }

    public PartBreakdownDto(String category,
                            String partName,
                            Float price) {
        this.category = category;
        this.partName = partName;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
