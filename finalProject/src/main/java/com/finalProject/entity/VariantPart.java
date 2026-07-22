package com.finalProject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "variant_part",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_variant_part",
                        columnNames = {"variant_id", "part_id"})
        })
public class VariantPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false)
    private Long id;

    @Column(name="part_id",nullable=false)
    private Long partId;

    @Column(name="variant_id", nullable=false)
    private Long variantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

}
