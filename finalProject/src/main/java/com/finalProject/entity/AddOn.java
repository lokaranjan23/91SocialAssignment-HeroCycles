package com.finalProject.entity;

import com.finalProject.exception.InvalidDataEnteredException;
import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name="add_ons")
public class AddOn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "current_price",nullable = false)
    private Float currentPrice;

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


    public void setCurrentPrice(Float currentPrice) {
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

    public Float getCurrentPrice() {
        return currentPrice;
    }
}
