package com.example.javaproject.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "advisories")
data class Advisory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var description: String,

    @Column(nullable = false)
    var date: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    var associatedAdmin: Admin? = null
)

@Entity
@Table(name = "market_prices")
data class MarketPrice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var crop: String,

    @Column(nullable = false)
    var price: Double,

    @Column(nullable = false)
    var date: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    var associatedVendor: Vendor? = null
)

@Entity
@Table(name = "weather_info")
data class WeatherInfo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var region: String,

    @Column(nullable = false)
    var forecast: String,

    @Column(nullable = false)
    var date: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    var associatedAdmin: Admin? = null
)
