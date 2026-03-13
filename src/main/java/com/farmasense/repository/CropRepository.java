package com.farmasense.repository;

import com.farmasense.model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CropRepository extends JpaRepository<Crop, Long> {
    Optional<Crop> findByName(String name);
}
