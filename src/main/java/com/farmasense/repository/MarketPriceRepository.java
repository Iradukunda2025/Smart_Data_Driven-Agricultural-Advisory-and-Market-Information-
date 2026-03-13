package com.farmasense.repository;

import com.farmasense.model.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {
}
