package com.finalProject.entity;
import com.finalProject.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="parts")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false)
    private Long id;

    @Column(name = "name",nullable = false, unique = true)
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_category_rule_id", nullable = false)
    private PartCategoryRule partCategoryRule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "current_price",nullable=false)
    private float currentPrice;

    @Column(name = "new_price")
    private Float newPrice;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PartCategoryRule getPartCategoryRule() {
        return partCategoryRule;
    }

    public void setPartCategoryRule(PartCategoryRule partCategoryRule) {
        this.partCategoryRule = partCategoryRule;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Float getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Float newPrice) {

        this.newPrice = newPrice;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {

        this.effectiveFrom = effectiveFrom;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
