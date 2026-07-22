package com.finalProject.responseDto;

import com.finalProject.enums.PartCategory;

public class PartCategoryResponseDto {

    private Long id;
    private PartCategory category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PartCategory getCategory() {
        return category;
    }

    public void setCategory(PartCategory category) {
        this.category = category;
    }
}
