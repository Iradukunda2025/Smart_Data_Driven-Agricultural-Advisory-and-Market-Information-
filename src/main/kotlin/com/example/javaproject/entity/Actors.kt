package com.example.javaproject.entity

import jakarta.persistence.*

@Entity
@Table(name = "farmers")
data class Farmer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var contact: String
)

@Entity
@Table(name = "vendors")
data class Vendor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var contact: String
)

@Entity
@Table(name = "admins")
data class Admin(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var contact: String
)
