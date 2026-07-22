package com.finalProject.repository;

import com.finalProject.entity.AddOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AddOnRepository extends JpaRepository<AddOn, Long> {
    List<AddOn> findByIdIn(List<Long> addOnIds);

    List<AddOn> findByNewPriceIsNotNullAndEffectiveFromLessThanEqual(LocalDate now);


    boolean existsByName(String addOnName);
}