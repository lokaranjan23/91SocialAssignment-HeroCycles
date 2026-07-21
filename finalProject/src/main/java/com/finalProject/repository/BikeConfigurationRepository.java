package com.finalProject.repository;

import com.finalProject.entity.BikeConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeConfigurationRepository extends JpaRepository<BikeConfiguration, Long> {
}