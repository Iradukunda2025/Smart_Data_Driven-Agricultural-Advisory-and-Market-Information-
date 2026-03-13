# Phase 6: Core API & Technical Infrastructure Documentation

This document provides a detailed technical overview of the components developed and integrated during Phase 6 of the FarmaSense project.

## 1. Data Models (Entities)
The following JPA entities form the core of the agricultural data system:

| Entity | Description | Core Attributes |
| :--- | :--- | :--- |
| **User / Role** | Authentication foundation. | `username`, `password`, `enabled`, `roles` |
| **Farmer / Vendor / Admin**| Actor-specific extensions of User data. | `associatedUser`, `fullName`, `phoneNumber` |
| **Advisory** | Expert guidance for farmers. | `title`, `description`, `date`, `crop` |
| **WeatherInfo** | Regional forecast data. | `region`, `forecast`, `date`, `lastSync` |
| **MarketPrice** | Regional crop pricing data. | `crop`, `price`, `date`, `vendor` |
| **Crop** | System-wide crop registry. | `name`, `variety`, `season` |
| **Notification** | Real-time system alerts. | `message`, `type`, `createdAt`, `read` |

## 2. Persistence Layer (Repositories)
All repositories extend `JpaRepository` to provide standard CRUD operations:
- **Repositories**: `AdvisoryRepository`, `WeatherInfoRepository`, `MarketPriceRepository`, `UserRepository`, `FarmerRepository`, `VendorRepository`, `AdminRepository`, `RoleRepository`, `CropRepository`, `NotificationRepository`.
- **Design Decision**: Leveraged Spring Data JPA for automatic query derivation (e.g., `findByUsername`) and simplified database interaction.

## 3. Business Logic (Services)
Services encapsulate complex operations and external integrations:
- **Core Services**: `AdvisoryService`, `MarketPriceService`, `WeatherInfoService`.
- **Integration Services**: `MeteoRwandaService` (Handles external API consumption and data parsing).
- **Control Services**: `UserService` (Registration/Admin logic), `NotificationService` (Broadcast alerts).
- **Design Decision**: Isolated business logic from controllers to ensure testability and modularity.

## 4. Web & API Layer (Controllers)
The system uses a two-tier controller strategy:

### A. REST Controllers (API)
Located at `/api/v1/`, these provide JSON-based endpoints for system flexibility:
- `GET/POST/DELETE /api/v1/advisories`
- `GET/POST/DELETE /api/v1/weather`
- `GET/POST/DELETE /api/v1/market-prices`
- `GET /api/v1/notifications`

### B. View Controllers (Dashboards)
The `WebController` handles Thymeleaf view rendering and dashboard-specific mappings:
- `/dashboard/admin`: Admin metrics and system oversight.
- `/dashboard/vendor`: Inventory and sales preview.
- `/dashboard/farmer`: Personalized service view.

## 5. API Design Decisions
- **RESTful Principles**: Uses standard HTTP verbs (GET for retrieval, POST for creation, DELETE for removal).
- **Role-Based Routing**: Security is enforced at the controller level using Spring Security, ensuring (e.g.) Farmers can `GET` data but not `POST/DELETE`.
- **Content Type**: Standardized on `application/json` for all API communication.
- **DTOs**: Used for secure data transfer between layers, preventing exposure of sensitive entity fields (like passwords).

---
*Verified Final Phase 6 Deliverable*
