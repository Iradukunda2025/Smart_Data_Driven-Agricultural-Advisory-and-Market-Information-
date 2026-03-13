package com.farmasense.repository;

import com.farmasense.model.Advisory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdvisoryRepository extends JpaRepository<Advisory, Long> {
}
