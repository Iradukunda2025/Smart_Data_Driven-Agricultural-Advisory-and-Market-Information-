package com.example.javaproject.repository

import com.example.javaproject.entity.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FarmerRepository : JpaRepository<Farmer, Long>

@Repository
interface VendorRepository : JpaRepository<Vendor, Long>

@Repository
interface AdminRepository : JpaRepository<Admin, Long>

@Repository
interface AdvisoryRepository : JpaRepository<Advisory, Long>

@Repository
interface MarketPriceRepository : JpaRepository<MarketPrice, Long>

@Repository
interface WeatherInfoRepository : JpaRepository<WeatherInfo, Long>
