package com.finalProject.entity;




import com.finalProject.enums.PartCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "part_category_rule")
public class PartCategoryRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PartCategory category;

    @Column(name = "default_quantity", nullable = false)
    private Integer defaultQuantity;

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

    public Integer getDefaultQuantity() {
        return defaultQuantity;
    }

    public void setDefaultQuantity(Integer defaultQuantity) {
        this.defaultQuantity = defaultQuantity;
    }
}