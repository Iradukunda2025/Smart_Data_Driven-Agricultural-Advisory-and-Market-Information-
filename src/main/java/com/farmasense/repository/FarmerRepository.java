package com.farmasense.repository;

import com.farmasense.model.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    java.util.Optional<Farmer> findByContact(String contact);
}
