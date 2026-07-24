package com.finalProject.repository;

import com.finalProject.entity.Variant;
import com.finalProject.enums.VariantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {

    List<Variant> findByBikeConfigurationIdAndStatus(Long bikeConfigId, 
                                                     VariantStatus status);

    List<Variant> findByBikeConfigurationId(Long bikeConfigId);

    Optional<Variant> findByIdAndStatus(Long variantId, VariantStatus variantStatus);

    @Modifying
    @Query("UPDATE Variant v SET v.status = :status WHERE v.id IN :variantIds")
    void updateVariantStatus(@Param("variantIds") List<Long> variantIds,
            @Param("status") VariantStatus status);

    List<Variant> findByNameContainingIgnoreCase(String keyword);
}