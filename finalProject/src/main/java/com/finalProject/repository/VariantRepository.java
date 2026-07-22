package com.finalProject.repository;

import com.finalProject.entity.Variant;
import com.finalProject.enums.VariantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {
    List<Variant> findByIdIn(List<Long> variantIds);

    List<Variant> findByBikeConfigurationIdAndStatus(Long bikeConfigId, 
                                                     VariantStatus status);

    List<Variant> findByBikeConfigurationId(Long bikeConfigId);
}