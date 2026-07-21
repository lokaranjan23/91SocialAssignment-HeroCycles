package com.finalProject.entity;


import com.finalProject.enums.VariantStatus;
import jakarta.persistence.*;

@Entity
@Table(name="variants")
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "bike_configuration_id")
    private BikeConfiguration bikeConfiguration;

    @Column(name = "current_price", nullable = false)
    private Float currentPrice;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false)
    private VariantStatus status;

    public Float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Float currentPrice) {
        this.currentPrice = currentPrice;
    }

    public VariantStatus getStatus() {
        return status;
    }

    public void setStatus(VariantStatus status) {
        this.status = status;
    }



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

    public BikeConfiguration getBikeConfiguration() {
        return bikeConfiguration;
    }

    public void setBikeConfiguration(BikeConfiguration bikeConfiguration) {
        this.bikeConfiguration = bikeConfiguration;
    }
}
