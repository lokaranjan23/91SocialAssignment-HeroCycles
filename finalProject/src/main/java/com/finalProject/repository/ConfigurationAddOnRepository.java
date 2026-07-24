package com.finalProject.repository;

import com.finalProject.entity.AddOn;
import com.finalProject.entity.ConfigurationAddOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationAddOnRepository extends JpaRepository<ConfigurationAddOn, Long> {
    @Query("SELECT ca.addOnId FROM ConfigurationAddOn ca WHERE ca.configurationId = :configurationId")
    List<Long> findByConfigurationId(@Param("configurationId") Long bikeConfigId) ;


    boolean existsByConfigurationIdAndAddOnId(Long configId, Long addOnId);
}