package com.finalProject.repository;

import com.finalProject.entity.Part;
import com.finalProject.enums.PartCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {


    List<Part> findByIdIn(List<Long> partIds);


    List<Part> findByNewPriceIsNotNullAndEffectiveFromLessThanEqual(LocalDate now);

    boolean existsByName(String partName);

    List<Part> findByPartCategoryRuleCategory(PartCategory category);
}