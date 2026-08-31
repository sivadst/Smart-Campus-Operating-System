# Changelog

All notable changes to the Smart Campus Operating System are documented here.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [1.0.0] - 2026-08-31

### Added
- **Authentication Module**: JWT-based authentication with access and refresh tokens
- **User Management**: RBAC with 7 roles, profile management, password change
- **Department Module**: CRUD operations with soft-delete
- **Course Module**: Course management with department/faculty associations, pagination
- **Attendance Module**: Single and bulk attendance marking, summary analytics
- **Notice Board**: Draft-to-publish workflow, role-targeted notices, 9 categories
- **Building Management**: Campus building registry with geolocation
- **Room Management**: Room CRUD with amenity tracking, availability filtering
- **Timetable Module**: Weekly schedule management for faculty, courses, and rooms
- **Room Booking**: Reservation system with conflict detection and approval workflow
- **Enrollment Module**: Student course registration with capacity enforcement and grading
- **Health Monitoring**: Health check and application info endpoints
- **Security**: BCrypt password hashing, JWT (HMAC-SHA256), method-level authorization
- **Infrastructure**: Docker multi-stage build, Docker Compose orchestration
- **Developer Experience**: Data seeder, H2 dev console, Swagger UI, AOP logging
- **Configuration**: Multi-profile support (dev/test/prod), CORS, Jackson, OpenAPI

### Technical Details
- Java 21 LTS, Spring Boot 3.2.3, Spring Security 6.2
- PostgreSQL 15 (prod) / H2 In-Memory (dev/test)
- 11 JPA entities, 7 enumerations, 11 repositories
- 40+ REST API endpoints across 10 controllers
- Conventional Commits with 100+ atomic commits
