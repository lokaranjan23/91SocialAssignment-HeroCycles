package com.finalProject.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "configuration_add_on",
        uniqueConstraints = {@UniqueConstraint(
                        name = "uk_configuration_addon",
                        columnNames = {"configuration_id", "addon_id"}
                )
        }
)
public class ConfigurationAddOn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long addOnId;

    @Column(nullable = false)
    private Long configurationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAddOnId() {
        return addOnId;
    }

    public void setAddOnId(Long addOnId) {
        this.addOnId = addOnId;
    }

    public Long getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Long configurationId) {
        this.configurationId = configurationId;
    }
}

