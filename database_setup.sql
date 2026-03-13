-- Database Creation Script for FarmaSense
-- Database Name: farmasense_db

CREATE DATABASE IF NOT EXISTS farmasense_db;
USE farmasense_db;

-- 1. Roles Table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- 2. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE
);

-- 3. User-Roles Mapping
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- 4. Admin Table
CREATE TABLE IF NOT EXISTS admin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(20)
);

-- 5. Vendor Table
CREATE TABLE IF NOT EXISTS vendor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(20)
);

-- 6. Crops Table
CREATE TABLE IF NOT EXISTS crop (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    season VARCHAR(50)
);

-- 7. Farmers Table
CREATE TABLE IF NOT EXISTS farmer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    contact VARCHAR(20)
);

-- 8. Weather Info Table (Aligned with WeatherInfo entity)
CREATE TABLE IF NOT EXISTS weather_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    region VARCHAR(255) NOT NULL,
    forecast VARCHAR(255),
    date DATE,
    admin_id BIGINT,
    FOREIGN KEY (admin_id) REFERENCES admin(id) ON DELETE SET NULL
);

-- 9. Advisories Table (Aligned with Advisory entity)
CREATE TABLE IF NOT EXISTS advisory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    date DATE,
    admin_id BIGINT,
    FOREIGN KEY (admin_id) REFERENCES admin(id) ON DELETE SET NULL
);

-- 10. Market Prices Table (Aligned with MarketPrice entity)
CREATE TABLE IF NOT EXISTS market_price (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    crop VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    date DATE,
    vendor_id BIGINT,
    FOREIGN KEY (vendor_id) REFERENCES vendor(id) ON DELETE SET NULL
);

-- 11. Notifications Table
CREATE TABLE IF NOT EXISTS notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    message TEXT,
    sender_role VARCHAR(50),
    type VARCHAR(50),
    timestamp DATETIME,
    is_read BOOLEAN DEFAULT FALSE
);

-- Initial Roles Seeding
INSERT IGNORE INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_FARMER'), ('ROLE_VENDOR');
