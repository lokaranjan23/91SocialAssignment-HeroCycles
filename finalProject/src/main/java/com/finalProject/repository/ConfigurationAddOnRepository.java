package com.finalProject.repository;

import com.finalProject.entity.AddOn;
import com.finalProject.entity.ConfigurationAddOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationAddOnRepository extends JpaRepository<ConfigurationAddOn, Long> {
    List<Long> findByConfigurationId(Long bikeConfigId) ;


    boolean existsByConfigurationIdAndAddOnId(Long configId, Long addOnId);
}