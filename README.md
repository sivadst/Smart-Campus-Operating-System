<p align="center">
  <img src="https://img.shields.io/badge/Smart%20Campus-Operating%20System-0066FF?style=for-the-badge&labelColor=000000&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0IiBmaWxsPSJub25lIiBzdHJva2U9IiMwMDY2RkYiIHN0cm9rZS13aWR0aD0iMiIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIj48cGF0aCBkPSJNNCAxMGgxNmEyIDIgMCAwIDEgMiAydjhhMiAyIDAgMCAxLTIgMkg0YTIgMiAwIDAgMS0yLTJ2LThhMiAyIDAgMCAxIDItMnoiLz48cGF0aCBkPSJNMiAxMFY2YTIgMiAwIDAgMSAyLTJoNGwyIDRoNmEyIDIgMCAwIDEgMiAydjIiLz48L3N2Zz4=" alt="Smart Campus OS"/>
</p>

<h1 align="center">🏛️ Smart Campus Operating System</h1>

<p align="center">
  <strong>A unified, enterprise-grade platform for intelligent campus management</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.3-6DB33F?style=flat-square&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/PostgreSQL-15-4169E1?style=flat-square&logo=postgresql&logoColor=white" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker&logoColor=white" alt="Docker"/>
  <img src="https://img.shields.io/badge/JWT-Auth-000000?style=flat-square&logo=jsonwebtokens&logoColor=white" alt="JWT"/>
  <img src="https://img.shields.io/badge/Swagger-API%20Docs-85EA2D?style=flat-square&logo=swagger&logoColor=black" alt="Swagger"/>
  <img src="https://img.shields.io/badge/License-MIT-blue?style=flat-square" alt="License"/>
</p>

<p align="center">
  <a href="#-features">Features</a> •
  <a href="#-architecture">Architecture</a> •
  <a href="#-quick-start">Quick Start</a> •
  <a href="#-api-reference">API Reference</a> •
  <a href="#-database-schema">Database</a> •
  <a href="#-deployment">Deployment</a> •
  <a href="#-contributing">Contributing</a>
</p>

---

## 📖 Overview

**Smart Campus OS** is a comprehensive backend platform that digitizes and streamlines every aspect of campus operations — from student enrollment and attendance tracking to room booking and campus-wide announcements. Built with **Spring Boot 3.2** and **Java 21**, it provides a secure, scalable, and modular REST API that serves as the backbone for any smart campus ecosystem.

> **Why Smart Campus OS?**  
> Traditional campus management involves fragmented systems, manual processes, and data silos. Smart Campus OS unifies everything into a single, intelligent platform with role-based access, real-time data, and enterprise-grade security.

---

## ✨ Features

### 🔐 Authentication & Authorization
- **JWT-based stateless authentication** with access & refresh tokens
- **Role-Based Access Control (RBAC)** with 7 distinct roles: `SUPER_ADMIN`, `ADMIN`, `FACULTY`, `STUDENT`, `LIBRARIAN`, `TRANSPORT_MANAGER`, `SECURITY`
- **Method-level security** using `@PreAuthorize` annotations
- **BCrypt password hashing** with configurable strength (12 rounds)
- Automatic token refresh workflow with revocation support

### 🏢 Department Management
- Full CRUD operations for academic departments
- Department code uniqueness enforcement
- Soft-delete mechanism for data preservation
- Head of Department assignment
- Active/inactive filtering

### 📚 Course Management
- Course creation with department and faculty associations
- Credit and semester-based organization
- Enrollment capacity tracking (`maxEnrollment` / `currentEnrollment`)
- Course status lifecycle: `ACTIVE` → `INACTIVE` → `ARCHIVED`
- Paginated listing with department filtering

### 📋 Attendance Tracking
- Per-student, per-course daily attendance records
- Multiple statuses: `PRESENT`, `ABSENT`, `LATE`, `EXCUSED`, `ON_LEAVE`
- Attendance summary with percentage calculation
- Faculty-marked attendance with audit trail
- Duplicate prevention (unique constraint on student + course + date)

### 📢 Notice Board & Announcements
- Create, publish, and manage campus-wide announcements
- **9 notice categories**: Academic, Administrative, Event, Examination, Placement, Sports, Cultural, Emergency, General
- Pin important notices to the top
- Role-targeted notices (e.g., notices only for students)
- Auto-expiry support with timestamp-based filtering
- Draft → Published workflow

### 🏗️ Room & Building Management
- Building registry with geolocation (latitude/longitude)
- Multi-floor room mapping with capacity tracking
- **11 room types**: Lecture Hall, Laboratory, Seminar Room, Library, Auditorium, Computer Lab, Conference Room, Office, Cafeteria, Gymnasium, Workshop
- Room amenities tracking (projector, AC, WiFi)
- Availability-based filtering

### 📅 Room Booking System
- Room reservation with time-slot conflict detection
- Booking status workflow: `PENDING` → `CONFIRMED` / `REJECTED` → `COMPLETED`
- Purpose and attendee count tracking
- Admin approval workflow

### 📆 Timetable Scheduling
- Course schedule entries with day, time, and room assignments
- Faculty-wise and room-wise schedule views
- Academic year and semester organization
- Section-based timetable support

### 👨‍🎓 Student Enrollment
- Course enrollment with academic year tracking
- Active enrollment management with grade recording
- Enrollment capacity enforcement
- Student-course relationship tracking

### 🏥 Health & Monitoring
- `/health` endpoint for service status
- `/info` endpoint with module listing
- Centralized logging with AOP-based method tracing
- Performance monitoring (execution time tracking)

---

## 🏗 Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENT APPLICATIONS                       │
│         (Web App, Mobile App, Admin Dashboard)               │
└────────────────────────┬────────────────────────────────────┘
                         │ HTTPS / REST API
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                   API GATEWAY / NGINX                        │
│              (Rate Limiting, SSL Termination)                 │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│              SMART CAMPUS OS (Spring Boot)                    │
│                                                              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐    │
│  │   Auth   │  │  Course  │  │ Attend.  │  │  Notice  │    │
│  │ Module   │  │  Module  │  │  Module  │  │  Module  │    │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘    │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐    │
│  │   Room   │  │ Timetable│  │  Enroll  │  │  Health  │    │
│  │ Booking  │  │  Module  │  │  Module  │  │ Monitor  │    │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘    │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              Security Layer (JWT + RBAC)                │  │
│  └────────────────────────────────────────────────────────┘  │
│  ┌────────────────────────────────────────────────────────┐  │
│  │           Cross-Cutting (AOP, Audit, Exception)        │  │
│  └────────────────────────────────────────────────────────┘  │
└────────────────────────┬────────────────────────────────────┘
                         │ JPA / Hibernate
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                   PostgreSQL Database                         │
│        (Production: PostgreSQL 15 / Dev: H2 In-Memory)       │
└─────────────────────────────────────────────────────────────┘
```

### Project Structure

```
Smart Campus Operating System/
├── src/
│   ├── main/
│   │   ├── java/com/campus/smartcampus/
│   │   │   ├── SmartCampusApplication.java          # Application entry point
│   │   │   ├── audit/                                # JPA auditing
│   │   │   │   ├── Auditable.java                    # Base entity with timestamps
│   │   │   │   └── AuditAwareImpl.java               # Current user resolver
│   │   │   ├── config/                               # Configuration classes
│   │   │   │   ├── SecurityConfig.java               # Spring Security setup
│   │   │   │   ├── OpenAPIConfig.java                # Swagger/OpenAPI config
│   │   │   │   ├── CorsConfig.java                   # CORS policy
│   │   │   │   ├── JacksonConfig.java                # JSON serialization
│   │   │   │   ├── LoggingAspect.java                # AOP method tracing
│   │   │   │   └── DataSeeder.java                   # Dev environment seeder
│   │   │   ├── controller/                           # REST API endpoints
│   │   │   │   ├── AuthController.java               # Authentication APIs
│   │   │   │   ├── DepartmentController.java         # Department CRUD
│   │   │   │   ├── CourseController.java             # Course management
│   │   │   │   ├── NoticeController.java             # Notice board
│   │   │   │   └── HealthController.java             # Health & info
│   │   │   ├── dto/                                  # Data Transfer Objects
│   │   │   │   ├── request/                          # Input DTOs
│   │   │   │   └── response/                         # Output DTOs
│   │   │   ├── entity/                               # JPA Entities
│   │   │   │   ├── User.java                         # User entity
│   │   │   │   ├── Department.java                   # Department entity
│   │   │   │   ├── Course.java                       # Course entity
│   │   │   │   ├── Building.java                     # Building entity
│   │   │   │   ├── Room.java                         # Room entity
│   │   │   │   ├── TimetableEntry.java               # Schedule entity
│   │   │   │   ├── Attendance.java                   # Attendance entity
│   │   │   │   ├── Notice.java                       # Notice entity
│   │   │   │   ├── RoomBooking.java                  # Booking entity
│   │   │   │   ├── Enrollment.java                   # Enrollment entity
│   │   │   │   └── RefreshToken.java                 # Auth token entity
│   │   │   ├── enums/                                # Enumerations
│   │   │   │   ├── UserRole.java                     # 7 user roles
│   │   │   │   ├── CourseStatus.java                 # Course lifecycle
│   │   │   │   ├── RoomType.java                     # 11 room types
│   │   │   │   ├── AttendanceStatus.java             # Attendance states
│   │   │   │   ├── BookingStatus.java                # Booking workflow
│   │   │   │   ├── NoticeCategory.java               # Notice categories
│   │   │   │   └── DayOfWeekEnum.java                # Scheduling days
│   │   │   ├── exception/                            # Exception handling
│   │   │   │   ├── CampusException.java              # Base exception
│   │   │   │   ├── GlobalExceptionHandler.java       # Centralized handler
│   │   │   │   ├── ResourceNotFoundException.java    # 404 errors
│   │   │   │   ├── DuplicateResourceException.java   # 409 conflicts
│   │   │   │   ├── UnauthorizedException.java        # 401 errors
│   │   │   │   ├── ForbiddenException.java           # 403 errors
│   │   │   │   └── BadRequestException.java          # 400 errors
│   │   │   ├── repository/                           # Data access layer
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── DepartmentRepository.java
│   │   │   │   ├── CourseRepository.java
│   │   │   │   ├── BuildingRepository.java
│   │   │   │   ├── RoomRepository.java
│   │   │   │   ├── TimetableEntryRepository.java
│   │   │   │   ├── AttendanceRepository.java
│   │   │   │   ├── NoticeRepository.java
│   │   │   │   ├── RoomBookingRepository.java
│   │   │   │   ├── EnrollmentRepository.java
│   │   │   │   └── RefreshTokenRepository.java
│   │   │   ├── security/                             # Security components
│   │   │   │   ├── JwtTokenProvider.java             # Token generation
│   │   │   │   ├── JwtAuthenticationFilter.java      # Request filter
│   │   │   │   ├── JwtAuthenticationEntryPoint.java  # 401 handler
│   │   │   │   ├── CustomUserDetails.java            # User principal
│   │   │   │   └── CustomUserDetailsService.java     # User loader
│   │   │   ├── service/                              # Business logic
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── DepartmentService.java
│   │   │   │   ├── CourseService.java
│   │   │   │   └── NoticeService.java
│   │   │   └── util/                                 # Utilities
│   │   │       └── ApplicationConstants.java
│   │   └── resources/
│   │       ├── application.yml                       # Main config
│   │       └── application-test.yml                  # Test config
│   └── test/                                         # Test suites
├── Dockerfile                                        # Multi-stage build
├── docker-compose.yml                                # Service orchestration
├── pom.xml                                           # Maven configuration
└── README.md                                         # This file
```

---

## 🚀 Quick Start

### Prerequisites

| Tool | Version | Purpose |
|------|---------|---------|
| **Java JDK** | 21+ | Runtime environment |
| **Maven** | 3.9+ | Build tool (or use included wrapper) |
| **Docker** | 24+ | Containerization (optional) |
| **PostgreSQL** | 15+ | Production database (optional, H2 used in dev) |

### Option 1: Run with Maven (Development)

```bash
# Clone the repository
git clone https://github.com/sivadst/Smart-Campus-Operating-System.git
cd Smart-Campus-Operating-System

# Run with the embedded H2 database (dev profile)
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

The application will start on **`http://localhost:8080`** with:
- 🗄️ H2 Console: `http://localhost:8080/h2-console`
- 📖 Swagger UI: `http://localhost:8080/swagger-ui.html`
- ❤️ Health Check: `http://localhost:8080/api/v1/public/health`

### Option 2: Run with Docker Compose (Production-like)

```bash
# Start the full stack (app + PostgreSQL)
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down
```

### Default Development Credentials

| Role | Email | Password |
|------|-------|----------|
| Super Admin | `admin@smartcampus.edu` | `Admin@123` |
| Faculty | `faculty@smartcampus.edu` | `Faculty@123` |
| Student | `student@smartcampus.edu` | `Student@123` |

> **⚠️ Note:** Default credentials are only seeded in the `dev` profile. Always change these in production.

---

## 📡 API Reference

### Base URL
```
http://localhost:8080/api/v1
```

### Authentication Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `POST` | `/auth/register` | Register a new user | ❌ |
| `POST` | `/auth/login` | Authenticate and get tokens | ❌ |
| `POST` | `/auth/refresh` | Refresh access token | ❌ |

### Department Endpoints

| Method | Endpoint | Description | Auth | Roles |
|--------|----------|-------------|------|-------|
| `POST` | `/departments` | Create department | ✅ | ADMIN, SUPER_ADMIN |
| `GET` | `/departments` | List all departments | ✅ | ALL |
| `GET` | `/departments/{id}` | Get department by ID | ✅ | ALL |
| `PUT` | `/departments/{id}` | Update department | ✅ | ADMIN, SUPER_ADMIN |
| `DELETE` | `/departments/{id}` | Delete department | ✅ | SUPER_ADMIN |

### Course Endpoints

| Method | Endpoint | Description | Auth | Roles |
|--------|----------|-------------|------|-------|
| `POST` | `/courses` | Create course | ✅ | ADMIN, SUPER_ADMIN |
| `GET` | `/courses` | List all courses (paginated) | ✅ | ALL |
| `GET` | `/courses/{id}` | Get course by ID | ✅ | ALL |
| `GET` | `/courses/department/{id}` | Get courses by department | ✅ | ALL |
| `PUT` | `/courses/{id}` | Update course | ✅ | ADMIN, SUPER_ADMIN, FACULTY |

### Notice Endpoints

| Method | Endpoint | Description | Auth | Roles |
|--------|----------|-------------|------|-------|
| `POST` | `/notices` | Create notice | ✅ | ADMIN, SUPER_ADMIN, FACULTY |
| `GET` | `/notices` | List published notices | ✅ | ALL |
| `GET` | `/notices/{id}` | Get notice by ID | ✅ | ALL |
| `PUT` | `/notices/{id}/publish` | Publish a notice | ✅ | ADMIN, SUPER_ADMIN |
| `DELETE` | `/notices/{id}` | Delete notice | ✅ | ADMIN, SUPER_ADMIN |

### Health Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| `GET` | `/public/health` | Service health check | ❌ |
| `GET` | `/public/info` | Application information | ❌ |

### Example API Calls

<details>
<summary><b>📝 Register a New User</b></summary>

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newstudent@campus.edu",
    "password": "MySecure@Pass1",
    "firstName": "Alice",
    "lastName": "Johnson",
    "phone": "+1234567890"
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "tokenType": "Bearer"
  },
  "message": "User registered successfully",
  "timestamp": "2026-08-31T16:45:00Z"
}
```
</details>

<details>
<summary><b>🔑 Login</b></summary>

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@smartcampus.edu",
    "password": "Admin@123"
  }'
```
</details>

<details>
<summary><b>🏢 Create a Department</b></summary>

```bash
curl -X POST http://localhost:8080/api/v1/departments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <access_token>" \
  -d '{
    "name": "Artificial Intelligence",
    "code": "AI",
    "description": "Department of Artificial Intelligence and Machine Learning",
    "headOfDepartment": "Dr. Sarah Connor"
  }'
```
</details>

<details>
<summary><b>📚 Create a Course</b></summary>

```bash
curl -X POST http://localhost:8080/api/v1/courses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <access_token>" \
  -d '{
    "code": "AI101",
    "name": "Introduction to Artificial Intelligence",
    "description": "Fundamentals of AI, including search algorithms, knowledge representation, and machine learning basics",
    "credits": 4,
    "semester": 5,
    "departmentId": "<department-uuid>",
    "maxEnrollment": 120
  }'
```
</details>

---

## 🗄️ Database Schema

### Entity Relationship Diagram

```
┌─────────────┐       ┌──────────────┐       ┌─────────────┐
│   USERS     │       │ DEPARTMENTS  │       │  BUILDINGS  │
├─────────────┤       ├──────────────┤       ├─────────────┤
│ id (PK)     │       │ id (PK)      │       │ id (PK)     │
│ email       │       │ name         │       │ name        │
│ password    │       │ code         │       │ code        │
│ first_name  │       │ description  │       │ address     │
│ last_name   │       │ head_of_dept │       │ total_floors│
│ role        │       │ is_active    │       │ lat/lng     │
│ is_active   │       └──────┬───────┘       └──────┬──────┘
└──────┬──────┘              │                      │
       │               ┌────┴───────┐          ┌────┴──────┐
       │               │  COURSES   │          │   ROOMS   │
       │               ├────────────┤          ├───────────┤
       │               │ id (PK)    │          │ id (PK)   │
       ├───────FK──────│ faculty_id │          │ room_num  │
       │               │ dept_id(FK)│          │ building  │
       │               │ code       │          │ room_type │
       │               │ name       │          │ capacity  │
       │               │ credits    │          │ amenities │
       │               │ semester   │          └─────┬─────┘
       │               │ status     │                │
       │               └────┬───────┘                │
       │                    │                        │
  ┌────┴────────┐    ┌─────┴──────┐    ┌────────────┴──┐
  │ ENROLLMENTS │    │ ATTENDANCE │    │ TIMETABLE     │
  ├─────────────┤    ├────────────┤    │ ENTRIES       │
  │ student(FK) │    │ student(FK)│    ├───────────────┤
  │ course(FK)  │    │ course(FK) │    │ course(FK)    │
  │ grade       │    │ date       │    │ room(FK)      │
  │ acad_year   │    │ status     │    │ faculty(FK)   │
  └─────────────┘    │ remarks    │    │ day_of_week   │
                     └────────────┘    │ start/end_time│
  ┌─────────────┐                     └───────────────┘
  │   NOTICES   │    ┌──────────────┐
  ├─────────────┤    │ ROOM_BOOKINGS│
  │ title       │    ├──────────────┤
  │ content     │    │ room(FK)     │
  │ category    │    │ booked_by(FK)│
  │ is_pinned   │    │ purpose      │
  │ author(FK)  │    │ start/end    │
  │ target_role │    │ status       │
  │ expires_at  │    └──────────────┘
  └─────────────┘
```

### Key Relationships
- **User ↔ Course**: Many-to-Many through `Enrollment`
- **Department → Course**: One-to-Many
- **Building → Room**: One-to-Many
- **Course + Room + Faculty → TimetableEntry**: Schedule assignments
- **Student + Course + Date → Attendance**: Daily tracking
- **User → Notice**: Author relationship
- **Room + User → RoomBooking**: Reservation system

---

## ⚙️ Configuration

### Application Profiles

| Profile | Database | Purpose |
|---------|----------|---------|
| `dev` (default) | H2 In-Memory | Local development with data seeding |
| `test` | H2 In-Memory | Automated testing (create-drop) |
| `prod` | PostgreSQL 15 | Production deployment |

### Environment Variables (Production)

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_HOST` | PostgreSQL host | `localhost` |
| `DB_PORT` | PostgreSQL port | `5432` |
| `DB_NAME` | Database name | `smartcampus` |
| `DB_USER` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |
| `JWT_SECRET` | JWT signing secret (256-bit) | — |
| `SPRING_PROFILES_ACTIVE` | Active profile | `dev` |

### JWT Configuration

| Parameter | Value | Description |
|-----------|-------|-------------|
| Access Token Expiry | 15 minutes | Short-lived for security |
| Refresh Token Expiry | 7 days | Long-lived for convenience |
| Algorithm | HMAC-SHA256 | Industry standard |
| Password Encoder | BCrypt (12 rounds) | High-security hashing |

---

## 🐳 Deployment

### Docker Deployment

```bash
# Build the Docker image
docker build -t smart-campus-os .

# Run with Docker Compose
docker-compose up -d

# Scale the application
docker-compose up -d --scale app=3
```

### Docker Compose Services

| Service | Port | Description |
|---------|------|-------------|
| `app` | 8080 | Spring Boot application |
| `postgres` | 5432 | PostgreSQL database |

### Production Checklist

- [ ] Set strong `JWT_SECRET` (min 256-bit)
- [ ] Change default database credentials
- [ ] Set `SPRING_PROFILES_ACTIVE=prod`
- [ ] Configure SSL/TLS termination
- [ ] Set up database backups
- [ ] Configure monitoring and alerting
- [ ] Review CORS allowed origins
- [ ] Enable rate limiting

---

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run with specific profile
./mvnw test -Dspring.profiles.active=test

# Generate test coverage report
./mvnw jacoco:report
```

---

## 🔧 Tech Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| **Runtime** | Java 21 (LTS) | Modern language features, virtual threads |
| **Framework** | Spring Boot 3.2.3 | Application framework |
| **Security** | Spring Security 6 | Authentication & authorization |
| **ORM** | Hibernate / JPA | Object-relational mapping |
| **Database** | PostgreSQL 15 / H2 | Data persistence |
| **Auth Tokens** | JJWT 0.12.5 | JWT generation & validation |
| **API Docs** | SpringDoc OpenAPI 2.3 | Swagger UI & API docs |
| **Build** | Maven | Dependency management |
| **Container** | Docker + Compose | Containerized deployment |
| **Mapping** | MapStruct 1.5.5 | DTO ↔ Entity mapping |
| **Boilerplate** | Lombok | Code generation |
| **AOP** | Spring AOP | Cross-cutting concerns |
| **Validation** | Jakarta Validation | Input validation |
| **Testing** | JUnit 5 + Testcontainers | Integration testing |

---

## 🤝 Contributing

We welcome contributions! Here's how to get started:

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feat/amazing-feature`
3. **Commit** your changes: `git commit -m 'feat: add amazing feature'`
4. **Push** to the branch: `git push origin feat/amazing-feature`
5. **Open** a Pull Request

### Commit Convention

We follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:

| Prefix | Purpose |
|--------|---------|
| `feat:` | New feature |
| `fix:` | Bug fix |
| `docs:` | Documentation |
| `test:` | Adding tests |
| `refactor:` | Code refactoring |
| `chore:` | Maintenance tasks |
| `ops:` | DevOps / infrastructure |
| `config:` | Configuration changes |
| `build:` | Build system changes |

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 🗺️ Roadmap

- [x] JWT Authentication & RBAC
- [x] Department Management
- [x] Course Management
- [x] Attendance Tracking
- [x] Room & Building Management
- [x] Timetable Scheduling
- [x] Notice Board
- [x] Room Booking System
- [x] Student Enrollment
- [x] Health Monitoring
- [x] Data Seeding (Dev)
- [ ] Email Notifications (SMTP)
- [ ] File Upload / Attachment Service
- [ ] Library Management Module
- [ ] Transport & Bus Tracking
- [ ] Examination & Grade Management
- [ ] Student Fee & Payment System
- [ ] Campus Event Calendar
- [ ] Push Notifications (WebSocket)
- [ ] Analytics Dashboard API
- [ ] Multi-Tenant Support
- [ ] OAuth2 / SSO Integration

---

<p align="center">
  <strong>Built with ❤️ for smarter campuses everywhere</strong>
</p>

<p align="center">
  <a href="https://github.com/sivadst/Smart-Campus-Operating-System/issues">Report Bug</a> •
  <a href="https://github.com/sivadst/Smart-Campus-Operating-System/issues">Request Feature</a>
</p>
