package com.finalProject.repository;

import com.finalProject.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {
    List<Variant> findByIdIn(List<Long> variantIds);

    List<Variant> findByBikeConfigurationId(Long bikeConfigId);
}