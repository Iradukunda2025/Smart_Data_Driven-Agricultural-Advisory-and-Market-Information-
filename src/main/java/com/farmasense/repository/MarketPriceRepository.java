package com.farmasense.repository;

import com.farmasense.model.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {

    /** Find all prices for a specific crop (case-insensitive). */
    List<MarketPrice> findByCropIgnoreCase(String crop);

    /** Find prices recorded within a date range. */
    List<MarketPrice> findByDateBetween(LocalDate from, LocalDate to);

    /** Find prices for a crop within a date range. */
    List<MarketPrice> findByCropIgnoreCaseAndDateBetween(String crop, LocalDate from, LocalDate to);

    /** Find prices posted by a specific vendor. */
    List<MarketPrice> findByAssociatedVendor(com.farmasense.model.Vendor vendor);
}
