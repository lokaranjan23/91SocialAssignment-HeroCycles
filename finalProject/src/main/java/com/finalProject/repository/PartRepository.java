package com.finalProject.repository;

import com.finalProject.entity.Part;
import com.finalProject.enums.PartCategory;
import com.finalProject.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {


    List<Part> findByIdIn(List<Long> partIds);

    boolean existsByName(String partName);
    
    Optional<Part> findByIdAndStatus(Long partId, Status status);

    List<Part> findByStatusAndPartCategoryRuleCategory(Status status, PartCategory category);

    List<Part> findAllByStatus(Status status);

    List<Part> findByStatusAndNewPriceIsNotNullAndEffectiveFromLessThanEqual(Status status, LocalDate now);

    List<Part> findByNameContainingIgnoreCase(String keyword);

    boolean existsByNameIgnoreCase(String partName);
}