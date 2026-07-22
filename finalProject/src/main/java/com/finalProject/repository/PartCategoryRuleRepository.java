package com.finalProject.repository;

import com.finalProject.entity.PartCategoryRule;
import com.finalProject.enums.PartCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PartCategoryRuleRepository extends JpaRepository<PartCategoryRule, Long> {
    Optional<PartCategoryRule> findByCategory(PartCategory category);
}