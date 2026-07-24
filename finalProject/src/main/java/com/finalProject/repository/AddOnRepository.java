package com.finalProject.repository;

import com.finalProject.entity.AddOn;
import com.finalProject.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AddOnRepository extends JpaRepository<AddOn, Long> {
    List<AddOn> findByIdIn(List<Long> addOnIds);

    List<AddOn> findAllByStatus(Status status);

    List<AddOn> findByStatusAndIdIn(Status status, List<Long> addOnIds);

    List<AddOn> findByStatusAndNewPriceIsNotNullAndEffectiveFromLessThanEqual(Status status, LocalDate now);

    Optional<AddOn> findByIdAndStatus(Long addOnId, Status status);

    List<AddOn> findByNameContainingIgnoreCase(String keyword);

    boolean existsByNameIgnoreCase(String name);
}