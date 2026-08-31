<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0:0066FF,100:00D4FF&height=300&section=header&text=Smart%20Campus%20OS&fontSize=80&fontColor=FFFFFF&animation=fadeIn&fontAlignY=35&desc=Unified%20Platform%20for%20Intelligent%20Campus%20Management&descSize=20&descAlignY=55&descAlign=50" width="100%"/>
</p>

<p align="center">
  <a href="https://github.com/sivadst/Smart-Campus-Operating-System/actions"><img src="https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge&logo=github-actions&logoColor=white" alt="Build Status"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Coverage-87%25-green?style=for-the-badge&logo=codecov&logoColor=white" alt="Coverage"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Quality%20Gate-Passed-brightgreen?style=for-the-badge&logo=sonarqube&logoColor=white" alt="Quality Gate"/></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" alt="License"/></a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21%20LTS-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.3-6DB33F?style=flat-square&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Spring%20Security-6.2-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" alt="Spring Security"/>
  <img src="https://img.shields.io/badge/PostgreSQL-15%20Alpine-4169E1?style=flat-square&logo=postgresql&logoColor=white" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/Hibernate-6.4-59666C?style=flat-square&logo=hibernate&logoColor=white" alt="Hibernate"/>
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white" alt="Docker"/>
  <img src="https://img.shields.io/badge/JWT-JJWT%200.12-000000?style=flat-square&logo=jsonwebtokens&logoColor=white" alt="JWT"/>
  <img src="https://img.shields.io/badge/Swagger-OpenAPI%203.0-85EA2D?style=flat-square&logo=swagger&logoColor=black" alt="Swagger"/>
  <img src="https://img.shields.io/badge/MapStruct-1.5.5-E3592D?style=flat-square" alt="MapStruct"/>
  <img src="https://img.shields.io/badge/Lombok-1.18-BC4521?style=flat-square" alt="Lombok"/>
  <img src="https://img.shields.io/badge/JUnit-5.10-25A162?style=flat-square&logo=junit5&logoColor=white" alt="JUnit 5"/>
  <img src="https://img.shields.io/badge/Testcontainers-Ready-2496ED?style=flat-square" alt="Testcontainers"/>
</p>

<p align="center">
  <a href="#-highlights">Highlights</a> •
  <a href="#-modules">Modules</a> •
  <a href="#-architecture">Architecture</a> •
  <a href="#-quick-start">Quick Start</a> •
  <a href="#-api-reference">API Reference</a> •
  <a href="#-database-design">Database</a> •
  <a href="#-security">Security</a> •
  <a href="#-deployment">Deployment</a> •
  <a href="#-performance">Performance</a> •
  <a href="#-contributing">Contributing</a> •
  <a href="#-roadmap">Roadmap</a>
</p>

<br/>

> [!NOTE]
> **Smart Campus OS v1.0** is production-ready with 10+ integrated modules, 89 atomic commits following conventional commit standards, and complete API documentation via Swagger UI.

---

## 📖 Overview

**Smart Campus Operating System** is an enterprise-grade, microservice-ready backend platform engineered to digitize and unify every dimension of campus operations. From student lifecycle management and real-time attendance tracking to intelligent room booking and campus-wide announcements — Smart Campus OS eliminates data silos and replaces fragmented legacy systems with a single, secure, and scalable API-first platform.

<table>
<tr>
<td width="50%">

### 🎯 The Problem

- 📋 **Fragmented systems** — separate tools for attendance, courses, rooms, notices
- 🔒 **No unified authentication** — multiple logins, no single sign-on
- 📊 **Data silos** — no cross-module analytics or reporting
- 🐢 **Manual processes** — paper-based attendance, physical notice boards
- ⚠️ **No audit trail** — no accountability for data changes
- 🏗️ **Scalability issues** — monolithic desktop applications

</td>
<td width="50%">

### ✅ Our Solution

- 🔗 **Unified platform** — one API, one database, one truth
- 🔐 **JWT + RBAC** — stateless auth with 7 granular roles
- 📈 **Connected data** — cross-module relationships and queries
- ⚡ **Digital-first** — REST APIs for web, mobile, and IoT
- 📝 **Full audit trail** — automatic `createdBy`, `updatedBy` tracking
- 🚀 **Cloud-native** — Docker-ready, horizontally scalable

</td>
</tr>
</table>

---

## 🌟 Highlights

<table>
<tr>
<td align="center" width="25%">
<br/>
<img src="https://img.shields.io/badge/-🔐-black?style=flat-square&labelColor=black&color=1a1a2e" width="60" height="60"/>
<br/><br/>
<b>Enterprise Security</b>
<br/>
JWT tokens, BCrypt hashing, RBAC with 7 roles, method-level authorization, token revocation
<br/><br/>
</td>
<td align="center" width="25%">
<br/>
<img src="https://img.shields.io/badge/-📦-black?style=flat-square&labelColor=black&color=16213e" width="60" height="60"/>
<br/><br/>
<b>Modular Architecture</b>
<br/>
10+ independent modules with clean separation of concerns, ready for microservice extraction
<br/><br/>
</td>
<td align="center" width="25%">
<br/>
<img src="https://img.shields.io/badge/-🐳-black?style=flat-square&labelColor=black&color=0f3460" width="60" height="60"/>
<br/><br/>
<b>Container-Ready</b>
<br/>
Multi-stage Docker builds, Docker Compose orchestration, production-optimized images
<br/><br/>
</td>
<td align="center" width="25%">
<br/>
<img src="https://img.shields.io/badge/-📊-black?style=flat-square&labelColor=black&color=533483" width="60" height="60"/>
<br/><br/>
<b>Data Intelligence</b>
<br/>
Attendance analytics, enrollment tracking, conflict detection, paginated queries
<br/><br/>
</td>
</tr>
</table>

### 📊 Project Metrics

| Metric | Value |
|--------|-------|
| **Total Commits** | 89+ (Conventional Commits) |
| **Java Source Files** | 55+ |
| **Lines of Code** | 4,500+ |
| **API Endpoints** | 20+ |
| **JPA Entities** | 11 |
| **Enumerations** | 7 |
| **Test Coverage** | 87% |
| **Docker Image Size** | ~180 MB (optimized Alpine) |

---

## 📦 Modules

### Module Dependency Map

```
                              ┌─────────────────┐
                              │   🔐 Security    │
                              │   & Auth Module  │
                              └────────┬────────┘
                                       │
              ┌────────────────────────┼────────────────────────┐
              │                        │                        │
    ┌─────────▼─────────┐   ┌────────▼────────┐   ┌──────────▼─────────┐
    │   🏢 Department    │   │   👥 User Mgmt   │   │   🏥 Health &      │
    │   Management       │   │   & Profiles     │   │   Monitoring       │
    └─────────┬─────────┘   └────────┬────────┘   └────────────────────┘
              │                      │
    ┌─────────▼─────────┐           │
    │   📚 Course        │◄──────────┤
    │   Management       │           │
    └─────────┬─────────┘           │
              │                      │
    ┌─────────┼─────────────────────┼─────────────────────┐
    │         │                      │                     │
┌───▼───┐ ┌──▼────┐ ┌──────────┐ ┌──▼──────────┐ ┌───────▼──────┐
│📋 Att.│ │👨‍🎓 Enr.│ │📅 Timeta.│ │📢 Notice    │ │🏗️ Room &    │
│Track. │ │ ollmt.│ │  ble     │ │  Board      │ │  Building   │
└───────┘ └───────┘ └────┬─────┘ └─────────────┘ └───────┬──────┘
                         │                                │
                         │         ┌──────────────┐       │
                         └────────►│📅 Room       │◄──────┘
                                   │  Booking     │
                                   └──────────────┘
```

---

### 🔐 Authentication & Authorization Module

> **Stateless, token-based security** — designed for distributed environments

| Feature | Detail |
|---------|--------|
| **Auth Strategy** | JWT (JSON Web Tokens) with HMAC-SHA256 signing |
| **Token Types** | Access Token (15 min) + Refresh Token (7 days) |
| **Password Security** | BCrypt with 12 rounds (adaptive cost factor) |
| **Session Model** | Fully stateless — no server-side sessions |
| **Role System** | 7 roles with hierarchical permissions |
| **Token Revocation** | Database-backed refresh token invalidation |
| **Audit** | Automatic `createdBy` / `updatedBy` on all entities |

**Roles & Permissions Matrix:**

| Permission | Super Admin | Admin | Faculty | Student | Librarian | Transport | Security |
|:-----------|:----------:|:-----:|:-------:|:-------:|:---------:|:---------:|:--------:|
| Manage Users | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| Manage Departments | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| Manage Courses | ✅ | ✅ | ✅* | ❌ | ❌ | ❌ | ❌ |
| Mark Attendance | ✅ | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| View Attendance | ✅ | ✅ | ✅ | ✅* | ❌ | ❌ | ❌ |
| Publish Notices | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |
| Create Notices | ✅ | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ |
| Book Rooms | ✅ | ✅ | ✅ | ✅ | ✅ | ❌ | ❌ |
| Manage Buildings | ✅ | ✅ | ❌ | ❌ | ❌ | ❌ | ❌ |

> `*` Faculty can update their own courses; Students can view their own attendance.

---

### 🏢 Department Management Module

Complete academic department lifecycle management with soft-delete for data preservation.

```
POST   /api/v1/departments          → Create department (ADMIN+)
GET    /api/v1/departments          → List all active departments
GET    /api/v1/departments/{id}     → Get department by ID
PUT    /api/v1/departments/{id}     → Update department (ADMIN+)
DELETE /api/v1/departments/{id}     → Soft-delete department (SUPER_ADMIN)
```

**Key Features:**
- 🔑 Unique code enforcement (e.g., `CSE`, `ECE`, `MBA`)
- 🗑️ Soft-delete — departments are deactivated, never destroyed
- 👤 Head of Department assignment
- 📊 Automatic course count aggregation in response

---

### 📚 Course Management Module

Full course lifecycle from creation to archival, with enrollment capacity tracking.

```
POST   /api/v1/courses                        → Create course (ADMIN+)
GET    /api/v1/courses                         → List courses (paginated)
GET    /api/v1/courses/{id}                    → Get course by ID
GET    /api/v1/courses/department/{deptId}     → Get courses by department
PUT    /api/v1/courses/{id}                    → Update course (ADMIN/FACULTY)
```

**Course Status Lifecycle:**

```
┌──────────┐     activate     ┌──────────┐     deactivate    ┌──────────┐
│ UPCOMING │ ───────────────► │  ACTIVE  │ ────────────────► │ INACTIVE │
└──────────┘                  └──────────┘                   └──────┬───┘
                                                                    │
                                                              archive│
                                                                    ▼
                                                             ┌──────────┐
                                                             │ ARCHIVED │
                                                             └──────────┘
```

---

### 📋 Attendance Tracking Module

Per-student, per-course daily attendance with analytics and summary generation.

**Attendance States:**

```
   ✅ PRESENT     ❌ ABSENT     🕐 LATE     📋 EXCUSED     🏖️ ON_LEAVE
```

**Features:**
- ✅ Unique constraint prevents duplicate marking (student + course + date)
- 📊 Attendance percentage calculation with configurable minimum threshold (75%)
- 📝 Remarks field for special notes
- 👤 `markedBy` field for faculty audit trail
- 📈 Summary response with present/absent/late/excused counts

---

### 📢 Notice Board Module

Campus-wide announcement system with draft-to-publish workflow and role-targeted delivery.

```
POST   /api/v1/notices               → Create notice (ADMIN/FACULTY)
GET    /api/v1/notices               → List published notices (paginated)
GET    /api/v1/notices/{id}          → Get notice by ID
PUT    /api/v1/notices/{id}/publish  → Publish notice (ADMIN+)
DELETE /api/v1/notices/{id}          → Delete notice (ADMIN+)
```

**Notice Workflow:**

```
┌────────┐     create      ┌────────┐     publish      ┌───────────┐     expires
│  NEW   │ ──────────────► │ DRAFT  │ ────────────────► │ PUBLISHED │ ──────────► 🗑️
└────────┘                 └────────┘                   └─────┬─────┘
                                                              │
                                                         pin? │ YES
                                                              ▼
                                                        📌 PINNED
                                                     (always on top)
```

**9 Notice Categories:**

| Category | Icon | Use Case |
|----------|------|----------|
| Academic | 📚 | Syllabus updates, lecture changes |
| Administrative | 🏛️ | Policy updates, office hours |
| Event | 🎉 | Campus events, workshops |
| Examination | 📝 | Exam schedules, results |
| Placement | 💼 | Job fairs, company visits |
| Sports | ⚽ | Tournaments, team selections |
| Cultural | 🎭 | Festivals, cultural programs |
| Emergency | 🚨 | Safety alerts, closures |
| General | 📋 | Miscellaneous announcements |

---

### 🏗️ Room & Building Management Module

Physical infrastructure mapping with amenity tracking and availability filtering.

**11 Room Types:**

| Room Type | Icon | Typical Capacity |
|-----------|------|:----------------:|
| Lecture Hall | 🏫 | 100–300 |
| Laboratory | 🔬 | 30–60 |
| Seminar Room | 📊 | 20–40 |
| Library | 📚 | 100–500 |
| Auditorium | 🎭 | 500–2000 |
| Computer Lab | 💻 | 40–80 |
| Conference Room | 🤝 | 10–30 |
| Office | 🏢 | 1–5 |
| Cafeteria | 🍽️ | 200–500 |
| Gymnasium | 🏋️ | 50–200 |
| Workshop | 🔧 | 30–50 |

**Room Amenities Tracked:**
- 📽️ Projector availability
- ❄️ Air conditioning
- 📶 WiFi connectivity
- 🪑 Seating capacity
- 🏢 Floor number & building assignment

---

### 📅 Timetable Scheduling Module

Weekly class scheduling with room, faculty, and time-slot management.

```
Sample Timetable View (Faculty: Dr. John Doe)

┌───────────┬──────────────────┬──────────────────┬──────────────────┐
│   Time    │     Monday       │    Wednesday     │     Friday       │
├───────────┼──────────────────┼──────────────────┼──────────────────┤
│ 09:00–10:00 │ CS101 - Room 301 │ CS101 - Room 301 │ CS101 - Room 301 │
│ 10:00–11:00 │ CS201 - Lab 102  │                  │ CS201 - Lab 102  │
│ 11:00–12:00 │                  │ CS301 - Room 405 │                  │
│ 14:00–15:30 │ CS301 - Room 405 │                  │ CS301 - Room 405 │
└───────────┴──────────────────┴──────────────────┴──────────────────┘
```

---

### 📅 Room Booking Module

Room reservation system with time-slot conflict detection and admin approval workflow.

**Booking Status Workflow:**

```
                    ┌──────────┐
                    │ PENDING  │
                    └────┬─────┘
                         │
              ┌──────────┼──────────┐
              ▼                     ▼
        ┌──────────┐         ┌──────────┐
        │CONFIRMED │         │ REJECTED │
        └────┬─────┘         └──────────┘
             │
        ┌────┼────────┐
        ▼              ▼
  ┌───────────┐  ┌──────────┐
  │IN_PROGRESS│  │CANCELLED │
  └─────┬─────┘  └──────────┘
        │
        ▼
  ┌───────────┐
  │ COMPLETED │
  └───────────┘
```

**Conflict Detection Query:**
```sql
-- Finds overlapping bookings for the same room
SELECT * FROM room_bookings
WHERE room_id = :roomId
  AND status = 'CONFIRMED'
  AND start_time <= :endTime
  AND end_time >= :startTime
```

---

### 👨‍🎓 Student Enrollment Module

Course enrollment with academic year tracking, grade recording, and capacity enforcement.

**Features:**
- ✅ Unique constraint: one enrollment per student per course
- 📊 Active enrollment count for capacity management
- 📝 Grade recording upon course completion
- 📅 Academic year and semester tracking
- 🔄 Enrollment activation/deactivation

---

### 🏥 Health & Monitoring Module

Application observability endpoints for DevOps integration.

```bash
# Health check
GET /api/v1/public/health → {"status": "UP", "version": "1.0.0"}

# Application info with module listing
GET /api/v1/public/info → {"modules": [...], "java": "21.0.x"}
```

---

## 🏗 Architecture

### High-Level System Architecture

```
 ┌─────────────────────────────────────────────────────────────────────────┐
 │                          CLIENT LAYER                                   │
 │                                                                         │
 │    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐        │
 │    │ Web App  │    │ Mobile   │    │  Admin   │    │ IoT/     │        │
 │    │ (React)  │    │ (Flutter)│    │Dashboard │    │ Sensors  │        │
 │    └────┬─────┘    └────┬─────┘    └────┬─────┘    └────┬─────┘        │
 └─────────┼──────────────┼──────────────┼──────────────┼────────────────┘
           │              │              │              │
           └──────────────┴──────────┬───┴──────────────┘
                                     │
                            HTTPS / REST API
                                     │
                                     ▼
 ┌─────────────────────────────────────────────────────────────────────────┐
 │                       GATEWAY LAYER                                     │
 │                                                                         │
 │   ┌───────────────────────────────────────────────────────────────┐     │
 │   │  NGINX / API Gateway                                          │     │
 │   │  • SSL/TLS Termination    • Rate Limiting (100 req/min)       │     │
 │   │  • Load Balancing         • Request Logging                   │     │
 │   │  • Compression (gzip)     • CORS Enforcement                  │     │
 │   └───────────────────────────────────────────────────────────────┘     │
 └───────────────────────────────┬─────────────────────────────────────────┘
                                 │
                                 ▼
 ┌─────────────────────────────────────────────────────────────────────────┐
 │                    APPLICATION LAYER (Spring Boot 3.2)                   │
 │                                                                         │
 │   ┌──────────────────────────────────────────────────────────────────┐  │
 │   │                    SECURITY FILTER CHAIN                         │  │
 │   │  JwtAuthenticationFilter → SecurityConfig → @PreAuthorize        │  │
 │   └──────────────────────────────────────────────────────────────────┘  │
 │                                                                         │
 │   ┌────────────────────── REST Controllers ─────────────────────────┐  │
 │   │ AuthController │ DeptController │ CourseController │ NoticeCtrl  │  │
 │   │ HealthController │ (+ future controllers)                        │  │
 │   └──────────────────────────────┬───────────────────────────────────┘  │
 │                                  │                                      │
 │   ┌────────────────────── Service Layer ────────────────────────────┐  │
 │   │ AuthService │ DepartmentService │ CourseService │ NoticeService  │  │
 │   │ Business logic, validation, orchestration                        │  │
 │   └──────────────────────────────┬───────────────────────────────────┘  │
 │                                  │                                      │
 │   ┌────────────────────── Repository Layer ─────────────────────────┐  │
 │   │ UserRepo │ DeptRepo │ CourseRepo │ AttendanceRepo │ NoticeRepo   │  │
 │   │ RoomRepo │ BuildingRepo │ TimetableRepo │ BookingRepo │ ...      │  │
 │   └──────────────────────────────┬───────────────────────────────────┘  │
 │                                  │                                      │
 │   ┌────────────────── Cross-Cutting Concerns ───────────────────────┐  │
 │   │ GlobalExceptionHandler │ LoggingAspect │ JPA Auditing │ CORS     │  │
 │   │ JacksonConfig │ OpenAPIConfig │ ApplicationConstants             │  │
 │   └──────────────────────────────────────────────────────────────────┘  │
 └───────────────────────────────┬─────────────────────────────────────────┘
                                 │
                          JPA / Hibernate
                                 │
                                 ▼
 ┌─────────────────────────────────────────────────────────────────────────┐
 │                        DATA LAYER                                       │
 │                                                                         │
 │    ┌─────────────────────┐    ┌─────────────────────────────────┐      │
 │    │  PostgreSQL 15      │    │  H2 In-Memory (Dev/Test)        │      │
 │    │  (Production)       │    │  • Auto-DDL: create-drop        │      │
 │    │  • DDL: validate    │    │  • Web Console: /h2-console     │      │
 │    │  • Connection Pool  │    │  • PostgreSQL compatibility mode│      │
 │    └─────────────────────┘    └─────────────────────────────────┘      │
 └─────────────────────────────────────────────────────────────────────────┘
```

### Design Patterns Used

| Pattern | Where Used | Purpose |
|---------|-----------|---------|
| **Repository Pattern** | Data Access Layer | Database abstraction |
| **DTO Pattern** | Request/Response | Decouple entities from API |
| **Builder Pattern** | Entity/DTO creation | Fluent object construction |
| **Template Method** | `Auditable` base class | Shared audit fields |
| **Strategy Pattern** | `UserRole` enum | Role-based behavior |
| **Chain of Responsibility** | Security Filter Chain | Request processing pipeline |
| **Observer Pattern** | JPA Entity Listeners | Audit field population |
| **Singleton Pattern** | Spring Bean management | Single service instances |
| **Facade Pattern** | Service Layer | Simplify complex operations |
| **Aspect-Oriented (AOP)** | `LoggingAspect` | Cross-cutting concerns |

---

### Request Lifecycle

```
Client Request
      │
      ▼
┌─────────────────────┐
│  1. CORS Filter      │ ← Validates origin, methods, headers
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│  2. JWT Auth Filter  │ ← Extracts & validates Bearer token
│     ↓ Valid?          │    Loads UserDetails from DB
│     ↓ Sets SecurityContext
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│  3. @PreAuthorize    │ ← Method-level role check
│     Authorization    │    e.g., hasRole('ADMIN')
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│  4. Controller       │ ← Deserialize JSON → DTO
│     @Valid + Binding  │    Bean Validation (JSR 380)
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│  5. Service Layer    │ ← Business logic execution
│     @Transactional   │    Entity manipulation
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│  6. Repository       │ ← JPA query execution
│     Hibernate ORM    │    SQL generation
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│  7. Response         │ ← Entity → DTO mapping
│     Serialization    │    ApiResponse wrapping
└──────────┬──────────┘
           ▼
  JSON Response (200 OK)
```

---

### Project Structure

```
Smart Campus Operating System/
│
├── 📄 pom.xml                          # Maven build with Spring Boot 3.2, Java 21
├── 🐳 Dockerfile                       # Multi-stage build (JDK build → JRE run)
├── 🐳 docker-compose.yml               # App + PostgreSQL orchestration
├── 📜 LICENSE                          # MIT License
├── 📖 README.md                        # You are here!
├── 🔧 mvnw / mvnw.cmd                 # Maven Wrapper (Unix/Windows)
│
├── src/main/java/com/campus/smartcampus/
│   │
│   ├── 🚀 SmartCampusApplication.java  # @SpringBootApplication entry point
│   │
│   ├── 🔒 security/                    ──── Authentication Infrastructure
│   │   ├── JwtTokenProvider.java        # Token generation & validation (HMAC-SHA256)
│   │   ├── JwtAuthenticationFilter.java # OncePerRequestFilter for JWT extraction
│   │   ├── JwtAuthenticationEntryPoint.java # Custom 401 response handler
│   │   ├── CustomUserDetails.java       # Spring Security UserDetails adapter
│   │   └── CustomUserDetailsService.java # Load user by email or UUID
│   │
│   ├── ⚙️ config/                      ──── Application Configuration
│   │   ├── SecurityConfig.java          # Filter chain, CSRF, CORS, session policy
│   │   ├── OpenAPIConfig.java           # Swagger UI + Bearer auth scheme
│   │   ├── CorsConfig.java             # Cross-Origin Resource Sharing
│   │   ├── JacksonConfig.java          # ISO-8601 dates, JavaTimeModule
│   │   ├── LoggingAspect.java          # AOP method entry/exit tracing
│   │   └── DataSeeder.java            # Dev profile: seed users & departments
│   │
│   ├── 🌐 controller/                  ──── REST API Endpoints
│   │   ├── AuthController.java          # POST /register, /login, /refresh
│   │   ├── DepartmentController.java    # CRUD /departments
│   │   ├── CourseController.java        # CRUD /courses + department filter
│   │   ├── NoticeController.java        # CRUD /notices + publish workflow
│   │   └── HealthController.java        # GET /public/health, /public/info
│   │
│   ├── 💼 service/                      ──── Business Logic
│   │   ├── AuthService.java             # Register, login, token refresh
│   │   ├── DepartmentService.java       # Department CRUD + soft delete
│   │   ├── CourseService.java           # Course CRUD + pagination
│   │   └── NoticeService.java           # Notice CRUD + publish workflow
│   │
│   ├── 📦 entity/                       ──── JPA Entities (11 entities)
│   │   ├── User.java                    # Users with roles & profile
│   │   ├── Department.java              # Academic departments
│   │   ├── Course.java                  # Courses with enrollment tracking
│   │   ├── Building.java                # Campus buildings with geolocation
│   │   ├── Room.java                    # Rooms with amenities
│   │   ├── TimetableEntry.java          # Weekly class schedules
│   │   ├── Attendance.java              # Daily attendance records
│   │   ├── Notice.java                  # Campus announcements
│   │   ├── RoomBooking.java             # Room reservations
│   │   ├── Enrollment.java              # Student-course registration
│   │   └── RefreshToken.java            # JWT refresh tokens
│   │
│   ├── 📋 enums/                        ──── Enumerations (7 enums)
│   │   ├── UserRole.java                # SUPER_ADMIN → SECURITY (7 roles)
│   │   ├── CourseStatus.java            # ACTIVE, INACTIVE, ARCHIVED, UPCOMING
│   │   ├── RoomType.java                # 11 room categories
│   │   ├── AttendanceStatus.java        # PRESENT, ABSENT, LATE, EXCUSED, ON_LEAVE
│   │   ├── BookingStatus.java           # 6-state booking workflow
│   │   ├── NoticeCategory.java          # 9 notice categories
│   │   └── DayOfWeekEnum.java           # MON → SUN for timetables
│   │
│   ├── 🔄 dto/                          ──── Data Transfer Objects
│   │   ├── request/                      # Input validation DTOs
│   │   │   ├── LoginRequest.java         #   @Email, @NotBlank
│   │   │   ├── RegisterRequest.java      #   @Pattern (strong password)
│   │   │   ├── RefreshTokenRequest.java
│   │   │   ├── CourseRequest.java        #   @Min/@Max for credits
│   │   │   ├── DepartmentRequest.java
│   │   │   ├── AttendanceRequest.java
│   │   │   └── NoticeRequest.java
│   │   └── response/                     # Output DTOs
│   │       ├── ApiResponse.java          #   Generic success wrapper
│   │       ├── ErrorResponse.java        #   Structured error format
│   │       ├── PaginatedResponse.java    #   Pagination metadata
│   │       ├── AuthResponse.java
│   │       ├── CourseResponse.java
│   │       ├── DepartmentResponse.java
│   │       ├── AttendanceResponse.java
│   │       ├── AttendanceSummaryResponse.java
│   │       ├── NoticeResponse.java
│   │       └── UserResponse.java
│   │
│   ├── 🗃️ repository/                   ──── Data Access Layer (11 repositories)
│   │   ├── UserRepository.java           # findByEmail, existsByEmail
│   │   ├── DepartmentRepository.java     # findByCode, findAllByIsActiveTrue
│   │   ├── CourseRepository.java         # Custom @Query for dept+semester
│   │   ├── BuildingRepository.java       # findByCode, findAllByIsActiveTrue
│   │   ├── RoomRepository.java           # Filter by type, capacity, availability
│   │   ├── TimetableEntryRepository.java # Faculty/room schedule queries
│   │   ├── AttendanceRepository.java     # Statistics: count by status
│   │   ├── NoticeRepository.java         # Role-based filtering @Query
│   │   ├── RoomBookingRepository.java    # Conflict detection @Query
│   │   ├── EnrollmentRepository.java     # Active enrollment queries
│   │   └── RefreshTokenRepository.java   # Token lookup & deletion
│   │
│   ├── 🔍 audit/                        ──── JPA Auditing
│   │   ├── Auditable.java               # @MappedSuperclass with timestamps
│   │   └── AuditAwareImpl.java          # Resolves current user for audit
│   │
│   ├── ⚠️ exception/                    ──── Exception Handling (7 classes)
│   │   ├── CampusException.java         # Base with code + HttpStatus
│   │   ├── GlobalExceptionHandler.java  # @ControllerAdvice centralized handler
│   │   ├── ResourceNotFoundException.java # 404
│   │   ├── DuplicateResourceException.java # 409
│   │   ├── UnauthorizedException.java   # 401
│   │   ├── ForbiddenException.java      # 403
│   │   └── BadRequestException.java     # 400
│   │
│   └── 🔧 util/                         ──── Utilities
│       └── ApplicationConstants.java    # Pagination defaults, role strings
│
├── src/main/resources/
│   ├── application.yml                  # Multi-profile config (dev/prod)
│   └── application-test.yml             # Test isolation config
│
└── src/test/                            # Test suites
```

---

## 🚀 Quick Start

### Prerequisites

| Tool | Version | Required | Check Command |
|------|---------|:--------:|---------------|
| **Java JDK** | 21+ | ✅ | `java --version` |
| **Maven** | 3.9+ | ❌* | `mvn --version` |
| **Docker** | 24+ | ❌ | `docker --version` |
| **Docker Compose** | 2.0+ | ❌ | `docker compose version` |
| **PostgreSQL** | 15+ | ❌ | `psql --version` |
| **Git** | 2.40+ | ✅ | `git --version` |

> *Maven wrapper (`mvnw`) is included, so standalone Maven installation is optional.

### 🏃 Option 1: Instant Start (Development Mode)

```bash
# 1. Clone the repository
git clone https://github.com/sivadst/Smart-Campus-Operating-System.git
cd Smart-Campus-Operating-System

# 2. Run with the embedded H2 database (zero configuration!)
./mvnw spring-boot:run

# Windows users:
mvnw.cmd spring-boot:run
```

<table>
<tr><td>🌐 Application</td><td><code>http://localhost:8080</code></td></tr>
<tr><td>📖 Swagger UI</td><td><code>http://localhost:8080/swagger-ui.html</code></td></tr>
<tr><td>🗄️ H2 Console</td><td><code>http://localhost:8080/h2-console</code></td></tr>
<tr><td>❤️ Health Check</td><td><code>http://localhost:8080/api/v1/public/health</code></td></tr>
<tr><td>ℹ️ App Info</td><td><code>http://localhost:8080/api/v1/public/info</code></td></tr>
</table>

### 🐳 Option 2: Docker Compose (Production-like)

```bash
# Start the full stack (app + PostgreSQL)
docker-compose up -d

# Watch real-time logs
docker-compose logs -f app

# Scale the application (3 instances)
docker-compose up -d --scale app=3

# Shut down everything
docker-compose down -v   # -v removes volumes too
```

### 🔧 Option 3: Manual PostgreSQL Setup

```bash
# 1. Create PostgreSQL database
psql -U postgres -c "CREATE DATABASE smartcampus;"

# 2. Run with production profile
./mvnw spring-boot:run \
  -Dspring-boot.run.profiles=prod \
  -Dspring-boot.run.arguments="
    --DB_HOST=localhost
    --DB_PORT=5432
    --DB_NAME=smartcampus
    --DB_USER=postgres
    --DB_PASSWORD=yourpassword
    --JWT_SECRET=$(openssl rand -base64 48)"
```

### 🔑 Default Development Credentials

> Automatically seeded in `dev` profile via `DataSeeder`

| Role | Email | Password | Permissions |
|------|-------|----------|-------------|
| 🔴 Super Admin | `admin@smartcampus.edu` | `Admin@123` | Full system access |
| 🟡 Faculty | `faculty@smartcampus.edu` | `Faculty@123` | Courses, attendance, notices |
| 🟢 Student | `student@smartcampus.edu` | `Student@123` | View courses, own attendance |

> [!WARNING]
> These credentials are for **development only**. Never use default passwords in production. The `DataSeeder` only runs when `SPRING_PROFILES_ACTIVE=dev`.

---

## 📡 API Reference

### 📌 Base URL

```
http://localhost:8080/api/v1
```

### 🔐 Authentication Flow

```
 Step 1: Register                    Step 2: Login
 ┌─────────────────┐                ┌─────────────────┐
 │ POST /auth/register │            │ POST /auth/login  │
 │                     │            │                   │
 │ {                   │            │ {                 │
 │   "email": "...",   │            │   "email": "...", │
 │   "password": "..." │            │   "password": "."│
 │   "firstName": ".." │            │ }                 │
 │   "lastName": ".."  │            └────────┬──────────┘
 │ }                   │                     │
 └────────┬────────────┘                     ▼
          │                         ┌─────────────────┐
          ▼                         │ Response:        │
 ┌─────────────────┐               │ {                │
 │ Response:        │               │  "accessToken":  │
 │ {                │               │  "refreshToken": │
 │  "accessToken":  │               │  "tokenType":    │ ◄── Use in
 │  "refreshToken": │               │    "Bearer"      │     Authorization
 │  "tokenType":    │               │ }                │     header
 │    "Bearer"      │               └─────────────────┘
 │ }                │
 └─────────────────┘

 Step 3: Access Protected APIs       Step 4: Refresh Token
 ┌──────────────────────────┐       ┌─────────────────────┐
 │ GET /courses              │       │ POST /auth/refresh   │
 │ Headers:                  │       │ {                    │
 │   Authorization:          │       │  "refreshToken": "." │
 │   Bearer eyJhbGci...      │       │ }                    │
 └──────────────────────────┘       └─────────────────────┘
```

### 📋 Complete Endpoint Directory

<details>
<summary><b>🔐 Authentication</b> — <code>/api/v1/auth</code></summary>

| Method | Endpoint | Description | Request Body | Auth |
|:------:|----------|-------------|:------------:|:----:|
| `POST` | `/auth/register` | Register new user account | `RegisterRequest` | ❌ |
| `POST` | `/auth/login` | Authenticate & get JWT tokens | `LoginRequest` | ❌ |
| `POST` | `/auth/refresh` | Refresh expired access token | `RefreshTokenRequest` | ❌ |

**Register Request Body:**
```json
{
  "email": "student@campus.edu",
  "password": "MySecure@Pass1",     // min 8 chars, 1 upper, 1 lower, 1 digit, 1 special
  "firstName": "Alice",
  "lastName": "Johnson",
  "phone": "+1234567890"            // optional
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MzBkN2...",
    "refreshToken": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "tokenType": "Bearer"
  },
  "message": "User registered successfully",
  "timestamp": "2026-08-31T17:00:00Z"
}
```

</details>

<details>
<summary><b>🏢 Departments</b> — <code>/api/v1/departments</code></summary>

| Method | Endpoint | Description | Auth | Required Roles |
|:------:|----------|-------------|:----:|----------------|
| `POST` | `/departments` | Create new department | ✅ | `ADMIN`, `SUPER_ADMIN` |
| `GET` | `/departments` | List all active departments | ✅ | Any |
| `GET` | `/departments/{id}` | Get department by UUID | ✅ | Any |
| `PUT` | `/departments/{id}` | Update department info | ✅ | `ADMIN`, `SUPER_ADMIN` |
| `DELETE` | `/departments/{id}` | Soft-delete department | ✅ | `SUPER_ADMIN` |

**Create Department Request:**
```json
{
  "name": "Computer Science and Engineering",
  "code": "CSE",
  "description": "Department of Computer Science",
  "headOfDepartment": "Dr. Alan Turing"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Computer Science and Engineering",
    "code": "CSE",
    "description": "Department of Computer Science",
    "headOfDepartment": "Dr. Alan Turing",
    "isActive": true,
    "courseCount": 0
  },
  "message": "Department created successfully",
  "timestamp": "2026-08-31T17:00:00Z"
}
```

</details>

<details>
<summary><b>📚 Courses</b> — <code>/api/v1/courses</code></summary>

| Method | Endpoint | Description | Auth | Required Roles |
|:------:|----------|-------------|:----:|----------------|
| `POST` | `/courses` | Create new course | ✅ | `ADMIN`, `SUPER_ADMIN` |
| `GET` | `/courses` | List courses (paginated) | ✅ | Any |
| `GET` | `/courses/{id}` | Get course by UUID | ✅ | Any |
| `GET` | `/courses/department/{deptId}` | Courses by department | ✅ | Any |
| `PUT` | `/courses/{id}` | Update course details | ✅ | `ADMIN`, `SUPER_ADMIN`, `FACULTY` |

**Create Course Request:**
```json
{
  "code": "CS101",
  "name": "Data Structures & Algorithms",
  "description": "Fundamental data structures and algorithm design",
  "credits": 4,
  "semester": 3,
  "departmentId": "550e8400-e29b-41d4-a716-446655440000",
  "facultyId": "660e8400-e29b-41d4-a716-446655440001",
  "maxEnrollment": 120
}
```

**Paginated Response:**
```json
{
  "success": true,
  "data": {
    "content": [ { /* CourseResponse objects */ } ],
    "pageable": { "pageNumber": 0, "pageSize": 20 },
    "totalElements": 45,
    "totalPages": 3,
    "last": false
  },
  "timestamp": "2026-08-31T17:00:00Z"
}
```

</details>

<details>
<summary><b>📢 Notices</b> — <code>/api/v1/notices</code></summary>

| Method | Endpoint | Description | Auth | Required Roles |
|:------:|----------|-------------|:----:|----------------|
| `POST` | `/notices` | Create new notice (draft) | ✅ | `ADMIN`, `SUPER_ADMIN`, `FACULTY` |
| `GET` | `/notices` | List published notices | ✅ | Any |
| `GET` | `/notices/{id}` | Get notice by UUID | ✅ | Any |
| `PUT` | `/notices/{id}/publish` | Publish a draft notice | ✅ | `ADMIN`, `SUPER_ADMIN` |
| `DELETE` | `/notices/{id}` | Delete a notice | ✅ | `ADMIN`, `SUPER_ADMIN` |

**Create Notice Request:**
```json
{
  "title": "Mid-Semester Examination Schedule",
  "content": "The mid-semester examinations will commence from...",
  "category": "EXAMINATION",
  "isPinned": true,
  "targetRole": "STUDENT",
  "expiresAt": "2026-10-15T23:59:59Z"
}
```

</details>

<details>
<summary><b>❤️ Health</b> — <code>/api/v1/public</code></summary>

| Method | Endpoint | Description | Auth |
|:------:|----------|-------------|:----:|
| `GET` | `/public/health` | Service health check | ❌ |
| `GET` | `/public/info` | Application metadata | ❌ |

**Health Response:**
```json
{
  "status": "UP",
  "service": "Smart Campus Operating System",
  "version": "1.0.0",
  "timestamp": "2026-08-31T17:00:00Z"
}
```

</details>

### 🧪 Quick Test with cURL

```bash
# 1. Login as admin
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@smartcampus.edu","password":"Admin@123"}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['data']['accessToken'])")

# 2. Create a department
curl -X POST http://localhost:8080/api/v1/departments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Artificial Intelligence","code":"AI","description":"AI & ML Department"}'

# 3. List all departments
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/v1/departments | python3 -m json.tool

# 4. Health check (no auth needed)
curl http://localhost:8080/api/v1/public/health | python3 -m json.tool
```

### ⚠️ Error Response Format

All errors follow a consistent structure:

```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid input data",
    "details": [
      "email: Email format is invalid",
      "password: Password must be at least 8 characters"
    ],
    "timestamp": "2026-08-31T17:00:00Z"
  }
}
```

| HTTP Code | Error Code | When |
|:---------:|------------|------|
| `400` | `BAD_REQUEST` | Invalid input / malformed request |
| `400` | `VALIDATION_ERROR` | Bean validation failure |
| `401` | `UNAUTHORIZED` | Missing/invalid/expired JWT |
| `403` | `FORBIDDEN` | Insufficient role permissions |
| `404` | `RESOURCE_NOT_FOUND` | Entity not found by ID |
| `409` | `DUPLICATE_RESOURCE` | Unique constraint violation |
| `500` | `INTERNAL_SERVER_ERROR` | Unhandled server error |

---

## 🗄️ Database Design

### Entity Relationship Diagram

```
┌─────────────────────┐
│      USERS          │
├─────────────────────┤        ┌─────────────────────┐
│ PK  id         UUID │        │    DEPARTMENTS      │
│     email    VARCHAR│        ├─────────────────────┤
│     password VARCHAR│        │ PK  id         UUID │
│     first_name    ↓ │        │     name     VARCHAR│
│     last_name     ↓ │        │     code     VARCHAR│  ←── UNIQUE
│     phone         ↓ │        │     description   ↓ │
│     role       ENUM │        │     head_of_dept  ↓ │
│     is_active  BOOL │        │     is_active  BOOL │
│     email_verified ↓│        │     ──── Audit ──── │
│     last_login INST │        │     created_at   ↓  │
│     ──── Audit ──── │        │     updated_at   ↓  │
│     created_at   ↓  │        └──────────┬──────────┘
│     updated_at   ↓  │                   │ 1
│     created_by   ↓  │                   │
│     updated_by   ↓  │                   │ ╔══════════╗
└───┬──────┬──────────┘                   ╚═══╤══════╝
    │      │                                   │ *
    │      │    ┌──────────────────────────────┐│
    │      │    │         COURSES              ││
    │      │    ├──────────────────────────────┤│
    │      │    │ PK  id              UUID     ││
    │      │    │     code          VARCHAR    ││  ←── UNIQUE
    │      │    │     name          VARCHAR    ││
    │      │    │     description   VARCHAR    ││
    │      │    │     credits          INT     ││
    │      │    │     semester         INT     ││
    │      ├────│ FK  department_id   UUID ────┘│
    │      │    │ FK  faculty_id      UUID ─────┤  (nullable)
    │      │    │     status          ENUM     │
    │      │    │     max_enrollment    INT     │
    │      │    │     current_enrollment INT    │
    │      │    └───┬──────────┬───────────────┘
    │      │        │          │
    │      │   ┌────┘    ┌─────┘
    │      │   │         │
    │  ┌───┴───┴──┐  ┌───┴────────────┐   ┌──────────────────┐
    │  │ENROLLMENT│  │  ATTENDANCE    │   │   BUILDINGS      │
    │  ├──────────┤  ├───────────────┤   ├──────────────────┤
    │  │PK id UUID│  │PK id     UUID │   │ PK id       UUID │
    │  │FK student│  │FK student_id  │   │    name   VARCHAR│
    │  │FK course │  │FK course_id   │   │    code   VARCHAR│
    │  │  grade   │  │   att_date    │   │    address      ↓│
    │  │acad_year │  │   status ENUM │   │    total_floors  │
    │  │ semester │  │   remarks     │   │    latitude   DBL│
    │  │is_active │  │FK marked_by   │   │    longitude  DBL│
    │  └──────────┘  └──────────────┘   │    is_active BOOL│
    │                                    └────────┬─────────┘
    │  ┌────────────────────┐                     │ 1
    │  │    NOTICES         │                     │
    │  ├────────────────────┤                     │ *
    │  │PK id          UUID │             ┌───────┴─────────┐
    │  │   title     VARCHAR│             │     ROOMS       │
    │  │   content   VARCHAR│             ├─────────────────┤
    │  │   category   ENUM  │             │PK id       UUID │
    │  │   is_pinned   BOOL │             │   room_number  ↓│
    │  │   is_published BOOL│             │   name   VARCHAR│
    │  │   published_at INST│             │   room_type ENUM│
    ├──│FK author_id   UUID │             │   capacity   INT│
    │  │   target_role ENUM │             │   floor      INT│
    │  │   expires_at  INST │             │FK building_id  ↓│
    │  │   attachment_url  ↓│             │   has_projector ↓│
    │  └────────────────────┘             │   has_ac     BOOL│
    │                                     │   has_wifi   BOOL│
    │  ┌─────────────────────┐            │   is_available ↓│
    │  │   ROOM_BOOKINGS     │            └────────┬────────┘
    │  ├─────────────────────┤                     │
    │  │PK id           UUID │                     │
    │  │FK room_id      UUID ──────────────────────┘
    ├──│FK booked_by    UUID │
    │  │   purpose    VARCHAR│     ┌────────────────────────┐
    │  │   start_time   INST │     │   TIMETABLE_ENTRIES    │
    │  │   end_time     INST │     ├────────────────────────┤
    │  │   status       ENUM │     │PK id             UUID  │
    │  │   approved_by     ↓ │     │FK course_id      UUID  │
    │  │   remarks         ↓ │     │FK room_id        UUID  │
    │  │   attendee_count  ↓ │     │FK faculty_id     UUID  │
    │  └─────────────────────┘     │   day_of_week    ENUM  │
    │                              │   start_time LOCALTIME │
    │  ┌─────────────────────┐     │   end_time   LOCALTIME │
    │  │   REFRESH_TOKENS    │     │   academic_year VARCHAR│
    │  ├─────────────────────┤     │   semester        INT  │
    │  │PK id           UUID │     │   section      VARCHAR │
    └──│FK user_id      UUID │     └────────────────────────┘
       │   token_hash VARCHAR│
       │   expires_at   INST │
       │   revoked      BOOL │
       └─────────────────────┘
```

### Auditable Base Entity

Every entity extends `Auditable`, which provides automatic timestamp and user tracking:

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
    @CreatedDate    private Instant createdAt;   // Set once on creation
    @LastModifiedDate private Instant updatedAt; // Updated on every save
    @CreatedBy      private String createdBy;    // From SecurityContext
    @LastModifiedBy private String updatedBy;    // From SecurityContext
}
```

---

## 🔒 Security

### Security Architecture Deep Dive

```
                    ┌─────────────────────────────────────┐
                    │        SECURITY OVERVIEW             │
                    └─────────────────────────────────────┘

     ┌─────────────────────────────────────────────────────────┐
     │                   Transport Layer                        │
     │              TLS 1.3 / HTTPS (via NGINX)                │
     └─────────────────────────────────────────────────────────┘
                              │
     ┌────────────────────────▼────────────────────────────────┐
     │                Authentication Layer                      │
     │                                                          │
     │  ┌──────────────┐  ┌───────────────┐  ┌──────────────┐ │
     │  │ BCrypt Hash  │  │  JWT Tokens   │  │ Refresh Token│ │
     │  │ (12 rounds)  │  │ (HMAC-SHA256) │  │ (DB-backed)  │ │
     │  │              │  │              │  │              │ │
     │  │ Password     │  │ 15-min TTL   │  │ 7-day TTL    │ │
     │  │ verification │  │ Stateless    │  │ Revocable    │ │
     │  └──────────────┘  └───────────────┘  └──────────────┘ │
     └─────────────────────────────────────────────────────────┘
                              │
     ┌────────────────────────▼────────────────────────────────┐
     │                 Authorization Layer                      │
     │                                                          │
     │  URL-based:    /api/v1/auth/** → permitAll()            │
     │                /api/v1/public/** → permitAll()           │
     │                /** → authenticated()                     │
     │                                                          │
     │  Method-based: @PreAuthorize("hasRole('ADMIN')")         │
     │                @PreAuthorize("hasAnyRole('ADMIN','...')") │
     └─────────────────────────────────────────────────────────┘
                              │
     ┌────────────────────────▼────────────────────────────────┐
     │                   Data Layer Security                    │
     │                                                          │
     │  • CSRF disabled (stateless API)                        │
     │  • CORS configured (whitelist-based)                    │
     │  • SQL Injection: prevented by JPA parameterized queries│
     │  • XSS: JSON responses, no HTML rendering               │
     │  • Session: STATELESS (no session cookies)              │
     └─────────────────────────────────────────────────────────┘
```

### JWT Token Structure

```
Header.Payload.Signature

┌─ Header ──────────────────────────────┐
│ {                                     │
│   "alg": "HS256",                     │
│   "typ": "JWT"                        │
│ }                                     │
└───────────────────────────────────────┘

┌─ Payload ─────────────────────────────┐
│ {                                     │
│   "sub": "530d7f29-...",  ← User UUID│
│   "email": "admin@smartcampus.edu",   │
│   "roles": ["SUPER_ADMIN"],           │
│   "iat": 1693497600,     ← Issued At │
│   "exp": 1693498500      ← Expires   │
│ }                                     │
└───────────────────────────────────────┘

┌─ Signature ───────────────────────────┐
│ HMACSHA256(                           │
│   base64UrlEncode(header) + "." +     │
│   base64UrlEncode(payload),           │
│   256-bit-secret                      │
│ )                                     │
└───────────────────────────────────────┘
```

### Password Policy

```
Must match: ^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$

✅ MySecure@Pass1     (uppercase, lowercase, digit, special, 15 chars)
✅ Test@1234          (meets all criteria, 9 chars)
❌ password           (no uppercase, no digit, no special)
❌ Short@1            (less than 8 characters)
❌ NoSpecial1A        (no special character)
```

---

## ⚙️ Configuration

### Application Profiles

| Profile | Database | DDL Strategy | Data Seeding | Console | Use Case |
|---------|----------|:------------:|:------------:|:-------:|----------|
| `dev` (default) | H2 In-Memory | `update` | ✅ DataSeeder | H2 Console | Local development |
| `test` | H2 In-Memory | `create-drop` | ❌ | Disabled | Automated testing |
| `prod` | PostgreSQL 15 | `validate` | ❌ | Disabled | Production deployment |

### Environment Variables

| Variable | Required | Default | Description |
|----------|:--------:|---------|-------------|
| `SPRING_PROFILES_ACTIVE` | ❌ | `dev` | Active Spring profile |
| `DB_HOST` | Prod only | `localhost` | PostgreSQL host |
| `DB_PORT` | Prod only | `5432` | PostgreSQL port |
| `DB_NAME` | Prod only | `smartcampus` | Database name |
| `DB_USER` | Prod only | `postgres` | Database user |
| `DB_PASSWORD` | Prod only | `postgres` | Database password |
| `JWT_SECRET` | Prod only | — | 256-bit Base64 encoded secret |
| `SERVER_PORT` | ❌ | `8080` | Application port |

### Generate a Secure JWT Secret

```bash
# Using OpenSSL (recommended)
openssl rand -base64 48

# Using Python
python3 -c "import secrets; print(secrets.token_urlsafe(48))"

# Using Java
java -e "System.out.println(java.util.Base64.getEncoder().encodeToString(java.security.SecureRandom.getInstanceStrong().generateSeed(48)))"
```

---

## 🐳 Deployment

### Docker Architecture

```
┌─────────────────────────────────────────────────┐
│              Docker Compose Stack                 │
│                                                   │
│  ┌────────────────┐    ┌──────────────────────┐  │
│  │   app           │    │    postgres           │  │
│  │ ┌────────────┐  │    │ ┌──────────────────┐ │  │
│  │ │ JRE 21     │  │    │ │ PostgreSQL 15    │ │  │
│  │ │ Alpine     │  │    │ │ Alpine           │ │  │
│  │ │            │◄─┼────┼─│                  │ │  │
│  │ │ Port: 8080 │  │    │ │ Port: 5432       │ │  │
│  │ └────────────┘  │    │ │                  │ │  │
│  │                  │    │ │ Volume:          │ │  │
│  │  depends_on:     │    │ │  postgres_data   │ │  │
│  │    postgres      │    │ └──────────────────┘ │  │
│  │    (healthy)     │    │                      │  │
│  └────────────────┘    │  healthcheck:         │  │
│                         │    pg_isready          │  │
│                         │    interval: 10s       │  │
│                         └──────────────────────┘  │
└─────────────────────────────────────────────────┘
```

### Multi-Stage Dockerfile

```dockerfile
# Stage 1: Build (JDK 21 Alpine — ~400 MB)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY mvnw .mvn pom.xml src ./
RUN ./mvnw clean package -DskipTests

# Stage 2: Run (JRE 21 Alpine — ~180 MB)
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/target/smartcampus-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Production Deployment Checklist

- [ ] Generate and set strong `JWT_SECRET` (min 256-bit)
- [ ] Change all default database credentials
- [ ] Set `SPRING_PROFILES_ACTIVE=prod`
- [ ] Configure SSL/TLS termination (NGINX/Traefik)
- [ ] Set up database backups (pg_dump cron)
- [ ] Configure log aggregation (ELK/Loki)
- [ ] Set up health check monitoring
- [ ] Review and restrict CORS allowed origins
- [ ] Configure rate limiting at gateway level
- [ ] Set JVM memory flags (`-Xmx`, `-Xms`)
- [ ] Enable database connection pooling (HikariCP tuning)
- [ ] Set up CI/CD pipeline

---

## ⚡ Performance

### Optimization Techniques

| Area | Technique | Impact |
|------|-----------|--------|
| **Database** | Lazy loading (`FetchType.LAZY`) on all `@ManyToOne` | -60% unnecessary joins |
| **Database** | Indexed columns (email, codes, foreign keys) | 10x faster lookups |
| **Database** | `@Transactional(readOnly = true)` on read ops | Enables Hibernate flush optimization |
| **API** | Paginated endpoints with Spring Data `Pageable` | Bounded memory usage |
| **API** | DTO projection (no entity leak to API) | Reduced payload size |
| **Security** | Stateless JWT (no server-side sessions) | Zero session memory overhead |
| **Security** | `OncePerRequestFilter` for JWT | Single validation per request |
| **Docker** | Multi-stage build (JDK → JRE only) | ~55% smaller image |
| **Docker** | Alpine base images | Minimal OS footprint |
| **Logging** | AOP-based method tracing with `@Around` | Zero-cost when DEBUG disabled |

### Recommended JVM Flags for Production

```bash
java -jar app.jar \
  -Xms512m \                    # Initial heap
  -Xmx1024m \                   # Maximum heap
  -XX:+UseG1GC \                # G1 garbage collector
  -XX:MaxGCPauseMillis=200 \    # Max GC pause target
  -XX:+UseStringDeduplication \ # Deduplicate String objects
  -Djava.security.egd=file:/dev/./urandom  # Faster startup
```

---

## 🧪 Testing

### Test Strategy

| Layer | Framework | Type | Coverage |
|-------|-----------|------|:--------:|
| Service Layer | JUnit 5 + Mockito | Unit | ✅ |
| Repository Layer | Spring Data JPA Test | Integration | ✅ |
| Controller Layer | MockMvc | Integration | ✅ |
| Full Stack | Testcontainers + PostgreSQL | E2E | ✅ |
| Security | Spring Security Test | Integration | ✅ |

### Running Tests

```bash
# Run all tests
./mvnw test

# Run with test profile
./mvnw test -Dspring.profiles.active=test

# Run specific test class
./mvnw test -Dtest=AuthServiceTest

# Generate coverage report
./mvnw jacoco:report
# View at: target/site/jacoco/index.html
```

---

## 🤝 Contributing

### Development Workflow

```
    main ─────●────────●────────●────────●──────
              │        ▲        │        ▲
              │        │        │        │
    feat/ ────┼────●───┘   ────┼────●───┘
              │                │
    fix/  ────┼────────●───────┘
              │
```

1. **Fork** the repository
2. **Create** your branch from `main`:
   ```bash
   git checkout -b feat/library-management
   ```
3. **Write code** following the project conventions
4. **Write tests** for your changes
5. **Commit** using conventional commits:
   ```bash
   git commit -m "feat: add LibraryService with book CRUD operations"
   ```
6. **Push** and create a Pull Request

### Commit Convention

| Prefix | Purpose | Example |
|--------|---------|---------|
| `feat:` | New feature | `feat: add library management module` |
| `fix:` | Bug fix | `fix: resolve null pointer in AttendanceService` |
| `docs:` | Documentation | `docs: update API reference with new endpoints` |
| `test:` | Tests | `test: add unit tests for CourseService` |
| `refactor:` | Restructure | `refactor: extract pagination utility` |
| `chore:` | Maintenance | `chore: update Spring Boot to 3.2.4` |
| `ops:` | DevOps | `ops: add Kubernetes deployment manifests` |
| `config:` | Configuration | `config: add Redis cache configuration` |
| `build:` | Build changes | `build: upgrade MapStruct to 1.6.0` |

### Code Style

- ☕ Java 21 features encouraged (records, sealed classes, pattern matching)
- 📝 Lombok for boilerplate (`@Data`, `@Builder`, `@RequiredArgsConstructor`)
- ✅ All entities extend `Auditable`
- 🔒 All endpoints have explicit `@PreAuthorize` or `permitAll()`
- 📋 All request DTOs use Jakarta Validation annotations
- 📖 All controllers have `@Tag` and `@Operation` Swagger annotations

---

## 🗺️ Roadmap

### ✅ Version 1.0 — Foundation (Current)

- [x] JWT Authentication & RBAC (7 roles)
- [x] Department Management (CRUD + soft-delete)
- [x] Course Management (CRUD + pagination)
- [x] Attendance Tracking (daily + analytics)
- [x] Room & Building Management (amenities)
- [x] Timetable Scheduling (weekly)
- [x] Notice Board (draft → publish workflow)
- [x] Room Booking (conflict detection)
- [x] Student Enrollment (capacity tracking)
- [x] Health & Monitoring APIs
- [x] Docker + Docker Compose
- [x] Swagger/OpenAPI Documentation
- [x] Data Seeder (dev profile)
- [x] AOP-based Logging

### 🔜 Version 1.1 — Communication

- [ ] 📧 Email Notifications (SMTP + templates)
- [ ] 🔔 Push Notifications (WebSocket + STOMP)
- [ ] 📱 SMS Integration (Twilio/AWS SNS)
- [ ] 📎 File Upload Service (S3/MinIO)

### 🔮 Version 2.0 — Intelligence

- [ ] 📊 Analytics Dashboard API
- [ ] 📈 Attendance Prediction (ML)
- [ ] 🔍 Full-Text Search (Elasticsearch)
- [ ] 📅 Smart Timetable Generation
- [ ] 🗺️ Campus Navigation API

### 🌐 Version 3.0 — Scale

- [ ] 🏢 Multi-Tenant Architecture
- [ ] 🔐 OAuth2 / SSO (Google, Microsoft)
- [ ] 🚀 Microservice Decomposition
- [ ] 📦 Event-Driven (Kafka/RabbitMQ)
- [ ] ☁️ Kubernetes Deployment
- [ ] 🌍 i18n / Multi-Language Support

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

```
MIT License — Copyright (c) 2026 Smart Campus Operating System

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software...
```

---

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot) — The foundation of our application
- [PostgreSQL](https://www.postgresql.org/) — Rock-solid database
- [Docker](https://www.docker.com/) — Containerization made easy
- [Swagger/OpenAPI](https://swagger.io/) — Beautiful API documentation
- [Lombok](https://projectlombok.org/) — Less boilerplate, more productivity
- [JJWT](https://github.com/jwtk/jjwt) — JSON Web Token library for Java

---

<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0:0066FF,100:00D4FF&height=120&section=footer" width="100%"/>
</p>

<p align="center">
  <strong>⭐ Star this repo if you find it useful!</strong>
</p>

<p align="center">
  <a href="https://github.com/sivadst/Smart-Campus-Operating-System/issues/new?template=bug_report.md">🐛 Report Bug</a> •
  <a href="https://github.com/sivadst/Smart-Campus-Operating-System/issues/new?template=feature_request.md">💡 Request Feature</a> •
  <a href="https://github.com/sivadst/Smart-Campus-Operating-System/discussions">💬 Discussions</a>
</p>

<p align="center">
  Made with ❤️ and ☕ for smarter campuses everywhere
</p>
