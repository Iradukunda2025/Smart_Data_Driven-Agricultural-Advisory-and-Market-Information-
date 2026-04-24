# FarmaSense: Smart Crop Price & Weather Advisory System

FarmaSense is a comprehensive digital solution designed to empower farmers and agricultural stakeholders with real-time, data-driven insights. By integrating weather forecasting and market price intelligence, the system helps users make informed decisions to optimize crop yields and maximize profits.

## 🚀 Key Features

### 1. Farmer Dashboard
- **Weather Advisory**: Real-time weather updates and agricultural advice based on current conditions.
- **Market Insights**: View current market prices for various crops to choose the best time to sell.
- **Personalized Profile**: Manage farming location and preferences.

### 2. Vendor Dashboard
- **Price Management**: Vendors can update market prices for different crops in real-time.
- **Market Impact**: Reach more farmers by providing transparent pricing data.

### 3. Admin Dashboard
- **User Management**: Oversee farmers and vendors registered on the platform.
- **Weather Sync**: Synchronize weather data directly from **Meteo Rwanda** or other APIs.
- **System Monitoring**: Manage global settings and system notifications.

### 4. Security & Authentication
- **Role-Based Access Control (RBAC)**: Distinct interfaces for Farmers, Vendors, and Admins.
- **OTP Verification**: Secure registration via email-based One-Time Passwords.

## 🛠️ Technology Stack
- **Backend**: Java 17, Spring Boot 2.7.x
- **Frontend**: Thymeleaf, HTML5, CSS3 (Modern Responsive Design)
- **Database**: MySQL (Remote/Local)
- **Security**: Spring Security
- **Email Service**: Spring Mail (SMTP Integration)
- **DevOps**: Docker, Maven, Render

## 📦 Local Setup

1. **Prerequisites**:
   - JDK 17+
   - MySQL 8.0+
   - Maven

2. **Database Setup**:
   - Create a database named `farmasense_db`.
   - Execute the `database_setup.sql` script.

3. **Configuration**:
   - Update `src/main/resources/application.properties` with your MySQL and SMTP credentials.

4. **Run**:
   ```bash
   mvn spring-boot:run
   ```

## 🌐 Deployment
The project is live on **Render**. For deployment details, refer to [deployment_report.md](./deployment_report.md).

---
*Empowering Agriculture through Technology.*
