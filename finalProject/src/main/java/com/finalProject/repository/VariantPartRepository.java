package com.finalProject.repository;

import com.finalProject.entity.Variant;
import com.finalProject.entity.VariantPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantPartRepository extends JpaRepository<VariantPart, Long> {
    List<VariantPart> findByVariantId(Long variantId);

    @Query("SELECT DISTINCT vp.variantId FROM VariantPart vp WHERE vp.partId IN :partIds")
    List<Long> findDistinctVariantIdsByPartIdIn(@Param("partIds") List<Long> partIds);
}