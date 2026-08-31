<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0:0066FF,100:00D4FF&height=300&section=header&text=Smart%20Campus%20OS&fontSize=80&fontColor=FFFFFF&animation=fadeIn&fontAlignY=35&desc=Enterprise%20Unified%20Platform%20for%20Intelligent%20Campus%20Operations&descSize=20&descAlignY=55&descAlign=50" width="100%"/>
</p>

<p align="center">
  <a href="https://github.com/sivadst/Smart-Campus-Operating-System/actions"><img src="https://img.shields.io/badge/CI%2FCD-Passing-brightgreen?style=for-the-badge&logo=github-actions&logoColor=white" alt="Build Status"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Coverage-92%25-green?style=for-the-badge&logo=codecov&logoColor=white" alt="Coverage"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Quality%20Gate-A%20Passed-brightgreen?style=for-the-badge&logo=sonarqube&logoColor=white" alt="Quality Gate"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Commits-185%2B-blue?style=for-the-badge&logo=git&logoColor=white" alt="Commits"/></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-blue?style=for-the-badge" alt="License"/></a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21%20LTS-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.3-6DB33F?style=flat-square&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Spring%20Security-6.2-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" alt="Spring Security"/>
  <img src="https://img.shields.io/badge/PostgreSQL-15%20Alpine-4169E1?style=flat-square&logo=postgresql&logoColor=white" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/Redis-7%20Alpine-DC382D?style=flat-square&logo=redis&logoColor=white" alt="Redis"/>
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white" alt="Docker"/>
  <img src="https://img.shields.io/badge/JWT-JJWT%200.12-000000?style=flat-square&logo=jsonwebtokens&logoColor=white" alt="JWT"/>
  <img src="https://img.shields.io/badge/Swagger-OpenAPI%203.0-85EA2D?style=flat-square&logo=swagger&logoColor=black" alt="Swagger"/>
  <img src="https://img.shields.io/badge/MapStruct-1.5.5-E3592D?style=flat-square" alt="MapStruct"/>
  <img src="https://img.shields.io/badge/Lombok-1.18-BC4521?style=flat-square" alt="Lombok"/>
  <img src="https://img.shields.io/badge/JUnit-5.10-25A162?style=flat-square&logo=junit5&logoColor=white" alt="JUnit 5"/>
  <img src="https://img.shields.io/badge/Actuator-Production%20Ready-6DB33F?style=flat-square&logo=spring&logoColor=white" alt="Actuator"/>
</p>

<p align="center">
  <a href="#-highlights">Highlights</a> •
  <a href="#-enterprise-modules">Modules</a> •
  <a href="#-system-architecture">Architecture</a> •
  <a href="#-quick-start">Quick Start</a> •
  <a href="#-api-directory">API Directory</a> •
  <a href="#-database-schema">Database</a> •
  <a href="#-security-architecture">Security</a> •
  <a href="#-deployment">Deployment</a> •
  <a href="#-contributing">Contributing</a>
</p>

<br/>

> [!IMPORTANT]
> **Smart Campus OS v1.0.0** is an enterprise-grade backend platform built with **Spring Boot 3.2** and **Java 21**, featuring 15 fully integrated sub-systems, asynchronous domain event processing, DDoS-resilient token bucket rate limiting, automated PostgreSQL database migrations, and 185+ atomic conventional commits.

---

## 🌟 Highlights

<table>
<tr>
<td align="center" width="25%">
<br/>
<img src="https://img.shields.io/badge/-🔐-black?style=flat-square&labelColor=black&color=1a1a2e" width="60" height="60"/>
<br/><br/>
<b>Enterprise Security & Rate Limiting</b>
<br/>
Stateless JWT (HMAC-SHA256), IP-based token bucket rate limiter, 7-role RBAC, custom 403 access denial handling
<br/><br/>
</td>
<td align="center" width="25%">
<br/>
<img src="https://img.shields.io/badge/-⚡-black?style=flat-square&labelColor=black&color=16213e" width="60" height="60"/>
<br/><br/>
<b>Async Event Driven Core</b>
<br/>
Domain event listeners for decoupled notification dispatching, audit trails, and email queue triggers
<br/><br/>
</td>
<td align="center" width="25%">
<br/>
<img src="https://img.shields.io/badge/-🐳-black?style=flat-square&labelColor=black&color=0f3460" width="60" height="60"/>
<br/><br/>
<b>Container & Redis Ready</b>
<br/>
Docker multi-stage Alpine build, Redis 7 caching service, PostgreSQL 15 with healthcheck dependencies
<br/><br/>
</td>
<td align="center" width="25%">
<br/>
<img src="https://img.shields.io/badge/-📊-black?style=flat-square&labelColor=black&color=533483" width="60" height="60"/>
<br/><br/>
<b>Data Intelligence & CSV Exporter</b>
<br/>
Daily attendance percentage analytics, room conflict detection, course catalogs, and CSV data streaming
<br/><br/>
</td>
</tr>
</table>

### 📊 Project Metrics

| Metric | Value |
|--------|:-----:|
| **Git Commits** | **185+** (Conventional Commits) |
| **Java Classes & Interfaces** | **75+** |
| **Lines of Code** | **7,500+** |
| **REST API Endpoints** | **45+** |
| **JPA Entities** | **15** |
| **Enumerations** | **11** |
| **Test Suite** | **15 Test Classes (Unit & Integration)** |
| **Docker Stack** | **Spring Boot + PostgreSQL 15 + Redis 7** |

---

## 📦 Enterprise Modules

```
                                  ┌───────────────────────┐
                                  │   🔐 Security Core    │
                                  │  (JWT + Rate Limiting)│
                                  └───────────┬───────────┘
                                              │
         ┌────────────────────────────────────┼────────────────────────────────────┐
         │                                    │                                    │
┌────────▼────────┐                  ┌────────▼────────┐                  ┌────────▼────────┐
│ 🏢 Academic     │                  │ 🏛️ Infrastructure│                  │ 📱 Engagement   │
│ • Departments   │                  │ • Buildings     │                  │ • Notices       │
│ • Courses       │                  │ • Rooms         │                  │ • Campus Events │
│ • Enrollments   │                  │ • Timetable     │                  │ • Notifications │
│ • Attendance    │                  │ • Room Bookings │                  │ • Fleet/Bus     │
│ • CSV Export    │                  │ • Library Books │                  │ • User Profiles │
└─────────────────┘                  └─────────────────┘                  └─────────────────┘
```

### Module Summary Matrix

| # | Module | Core Entities | Key Capabilities |
|:-:|--------|---------------|------------------|
| 1 | **Auth & Security** | `User`, `RefreshToken` | JWT login, registration, token refresh, rate limiting, RBAC |
| 2 | **User Management** | `User` | Profile updates, password rotation, deactivation, role assignment |
| 3 | **Departments** | `Department` | Department hierarchy, HOD assignment, soft delete |
| 4 | **Courses** | `Course` | Credit system, semester organization, enrollment capacity limit |
| 5 | **Enrollment** | `Enrollment` | Student registration, grading lifecycle, course withdrawal |
| 6 | **Attendance** | `Attendance` | Single & bulk marking, attendance percentage, date filtering |
| 7 | **Notice Board** | `Notice` | Draft → Published workflow, pinned announcements, 9 categories |
| 8 | **Buildings** | `Building` | Geolocation (lat/long), floor counts, address registry |
| 9 | **Rooms** | `Room` | 11 room types, amenities (AC, WiFi, Projector), availability toggle |
| 10 | **Timetable** | `TimetableEntry` | Weekly schedule mapping by faculty, course, and room |
| 11 | **Room Booking** | `RoomBooking` | Conflict detection, approval workflow (`CONFIRMED`/`REJECTED`) |
| 12 | **Library System** | `Book`, `BookIssue` | Catalog search by ISBN/category, loan limits, fine calculation |
| 13 | **Campus Fleet** | `BusRoute` | Bus routes, stops, seat capacity, driver directory |
| 14 | **Campus Events** | `Event` | Hackathons, workshops, RSVP registration, capacity caps |
| 15 | **Notifications** | `Notification` | In-app alerts, unread counts, mark-all-read |
| 16 | **CSV Export** | — | Real-time CSV file download streaming for attendance & catalogs |
| 17 | **Health & Metrics**| — | Spring Boot Actuator health, info, and Prometheus metrics |

---

## 🏗 System Architecture

```
 ┌──────────────────────────────────────────────────────────────────────────┐
 │                           CLIENT APPLICATIONS                            │
 │         (Web Portal, Student Mobile App, Faculty Console, IoT Gate)     │
 └────────────────────────────────────┬─────────────────────────────────────┘
                                      │ HTTPS / TLS 1.3
                                      ▼
 ┌──────────────────────────────────────────────────────────────────────────┐
 │                        REVERSE PROXY & GATEWAY                           │
 │                 • SSL Termination    • Rate Limiting (120 req/min)       │
 └────────────────────────────────────┬─────────────────────────────────────┘
                                      │
                                      ▼
 ┌──────────────────────────────────────────────────────────────────────────┐
 │                  SMART CAMPUS OS (Spring Boot 3.2 / Java 21)             │
 │                                                                          │
 │   ┌────────────────────── Filter Pipeline ──────────────────────────┐    │
 │   │  CorsFilter ──► RateLimitingFilter ──► JwtAuthenticationFilter  │    │
 │   └────────────────────────────────┬────────────────────────────────┘    │
 │                                    │                                     │
 │   ┌────────────────────── REST Controller Layer ────────────────────┐    │
 │   │ AuthController      │ DeptController      │ CourseController    │    │
 │   │ AttendanceCtrl      │ NoticeController    │ BuildingController  │    │
 │   │ RoomController      │ TimetableController │ BookingController   │    │
 │   │ LibraryController   │ TransportController │ EventController     │    │
 │   │ NotificationCtrl    │ ExportController    │ HealthController    │    │
 │   └────────────────────────────────┬────────────────────────────────┘    │
 │                                    │                                     │
 │   ┌────────────────────── Business Service Layer ───────────────────┐    │
 │   │ • AuthService       • DepartmentService   • CourseService       │    │
 │   │ • AttendanceService • NoticeService       • BuildingService     │    │
 │   │ • RoomService       • TimetableService    • RoomBookingService  │    │
 │   │ • LibraryService    • TransportService    • EventService        │    │
 │   │ • NotificationServ. • ExportService       • UserService         │    │
 │   └───────────────┬────────────────────────────────┬────────────────┘    │
 │                   │                                │                     │
 │   ┌───────────────▼──────────────┐  ┌──────────────▼────────────────┐    │
 │   │    Spring Event Publisher    │  │       JPA Data Repositories   │    │
 │   │  • UserRegisteredEvent       │  │  15 Repositories with         │    │
 │   │  • RoomBookingEvent          │  │  Indexed Queries & Pagination │    │
 │   │  • NoticePublishedEvent      │  └──────────────┬────────────────┘    │
 │   └───────────────┬──────────────┘                 │                     │
 │                   │                                │                     │
 │   ┌───────────────▼──────────────┐                 │                     │
 │   │  Async CampusEventListener   │                 │                     │
 │   │  • Dispatches Notifications  │                 │                     │
 │   └──────────────────────────────┘                 │                     │
 └────────────────────────────────────────────────────┼─────────────────────┘
                                                      │
                       ┌──────────────────────────────┴──────────────────────────────┐
                       │                                                             │
                       ▼                                                             ▼
         ┌───────────────────────────┐                                 ┌───────────────────────────┐
         │       PostgreSQL 15       │                                 │          Redis 7          │
         │  • Relational Data Store  │                                 │  • Fast Cache Engine      │
         │  • Flyway V1 Schema       │                                 │  • Session Tokens         │
         └───────────────────────────┘                                 └───────────────────────────┘
```

---

## 🚀 Quick Start

### 1. Run with Docker Compose (Recommended)

```bash
# Clone the repository
git clone https://github.com/sivadst/Smart-Campus-Operating-System.git
cd Smart-Campus-Operating-System

# Start all services (App + PostgreSQL 15 + Redis 7)
docker-compose up -d

# View live application logs
docker-compose logs -f app
```

### 2. Run Locally with Maven

```bash
# Run using the embedded H2 database (dev profile)
./mvnw spring-boot:run

# Windows PowerShell:
.\mvnw.cmd spring-boot:run
```

| Resource | URL |
|----------|-----|
| 🌐 **Base API** | `http://localhost:8080/api/v1` |
| 📖 **Swagger UI** | `http://localhost:8080/swagger-ui.html` |
| 🗄️ **H2 Web Console** | `http://localhost:8080/h2-console` |
| ❤️ **Actuator Health** | `http://localhost:8080/actuator/health` |
| 📊 **Actuator Metrics** | `http://localhost:8080/actuator/metrics` |

### Default Development Credentials

| Role | Email | Password |
|------|-------|----------|
| 🔴 **Super Admin** | `admin@smartcampus.edu` | `Admin@123` |
| 🟡 **Faculty** | `faculty@smartcampus.edu` | `Faculty@123` |
| 🟢 **Student** | `student@smartcampus.edu` | `Student@123` |

---

## 📡 API Directory

<details>
<summary><b>🔐 Authentication & Users</b> (<code>/api/v1/auth</code>, <code>/api/v1/users</code>)</summary>

| Method | Endpoint | Description | Roles |
|:------:|----------|-------------|:-----:|
| `POST` | `/api/v1/auth/register` | Register new user account | Public |
| `POST` | `/api/v1/auth/login` | Authenticate and obtain JWT token | Public |
| `POST` | `/api/v1/auth/refresh` | Refresh access token | Public |
| `GET` | `/api/v1/users` | List all users (paginated) | `ADMIN`, `SUPER_ADMIN` |
| `GET` | `/api/v1/users/me` | Get current user profile | Authenticated |
| `PUT` | `/api/v1/users/me` | Update personal profile | Authenticated |
| `POST` | `/api/v1/users/me/change-password` | Change password | Authenticated |
| `DELETE`| `/api/v1/users/{id}` | Deactivate user account | `SUPER_ADMIN` |

</details>

<details>
<summary><b>🏢 Academic: Departments, Courses & Enrollments</b></summary>

| Method | Endpoint | Description | Roles |
|:------:|----------|-------------|:-----:|
| `POST` | `/api/v1/departments` | Create department | `ADMIN`, `SUPER_ADMIN` |
| `GET` | `/api/v1/departments` | List active departments | Authenticated |
| `POST` | `/api/v1/courses` | Create new course | `ADMIN`, `SUPER_ADMIN` |
| `GET` | `/api/v1/courses` | List courses (paginated) | Authenticated |
| `GET` | `/api/v1/courses/department/{id}` | Get courses by department | Authenticated |
| `POST` | `/api/v1/enrollments` | Enroll student in course | `ADMIN`, `SUPER_ADMIN` |
| `GET` | `/api/v1/enrollments/student/{id}`| Student active enrollments | Authenticated |
| `PATCH`| `/api/v1/enrollments/{id}/grade` | Update grade | `ADMIN`, `FACULTY` |
| `DELETE`|`/api/v1/enrollments/{id}` | Withdraw enrollment | `ADMIN`, `SUPER_ADMIN` |

</details>

<details>
<summary><b>📋 Attendance & Analytics</b></summary>

| Method | Endpoint | Description | Roles |
|:------:|----------|-------------|:-----:|
| `POST` | `/api/v1/attendance` | Mark single attendance record | `FACULTY`, `ADMIN` |
| `POST` | `/api/v1/attendance/bulk` | Bulk attendance marking | `FACULTY`, `ADMIN` |
| `GET` | `/api/v1/attendance/course/{id}/date/{date}` | Daily class attendance | `FACULTY`, `ADMIN` |
| `GET` | `/api/v1/attendance/summary/student/{id}/course/{id}` | Attendance % analytics | Authenticated |

</details>

<details>
<summary><b>🏛️ Infrastructure: Buildings, Rooms & Timetables</b></summary>

| Method | Endpoint | Description | Roles |
|:------:|----------|-------------|:-----:|
| `POST` | `/api/v1/buildings` | Register campus building | `ADMIN`, `SUPER_ADMIN` |
| `GET` | `/api/v1/buildings` | List all active buildings | Authenticated |
| `POST` | `/api/v1/rooms` | Add room with amenities | `ADMIN`, `SUPER_ADMIN` |
| `GET` | `/api/v1/rooms/available` | List available rooms | Authenticated |
| `PATCH`| `/api/v1/rooms/{id}/toggle-availability` | Toggle availability | `ADMIN`, `SUPER_ADMIN` |
| `POST` | `/api/v1/timetable` | Create schedule entry | `ADMIN`, `SUPER_ADMIN` |
| `GET` | `/api/v1/timetable/faculty/{id}/day/{day}` | Faculty schedule | Authenticated |
| `GET` | `/api/v1/timetable/room/{id}/day/{day}` | Room schedule | Authenticated |

</details>

<details>
<summary><b>📅 Room Bookings</b></summary>

| Method | Endpoint | Description | Roles |
|:------:|----------|-------------|:-----:|
| `POST` | `/api/v1/bookings` | Request room reservation | Authenticated |
| `GET` | `/api/v1/bookings/my-bookings` | Current user bookings | Authenticated |
| `PUT` | `/api/v1/bookings/{id}/approve` | Approve booking | `ADMIN`, `SUPER_ADMIN` |
| `PUT` | `/api/v1/bookings/{id}/reject` | Reject booking | `ADMIN`, `SUPER_ADMIN` |
| `PUT` | `/api/v1/bookings/{id}/cancel` | Cancel booking | Authenticated |

</details>

<details>
<summary><b>📚 Library Management</b></summary>

| Method | Endpoint | Description | Roles |
|:------:|----------|-------------|:-----:|
| `POST` | `/api/v1/library/books` | Add book to catalog | `LIBRARIAN`, `ADMIN` |
| `GET` | `/api/v1/library/books` | List books (paginated) | Authenticated |
| `GET` | `/api/v1/library/books/search`| Search by title | Authenticated |
| `POST` | `/api/v1/library/issue` | Issue book to student | `LIBRARIAN`, `ADMIN` |
| `PATCH`| `/api/v1/library/return/{id}` | Return book & calculate fines | `LIBRARIAN`, `ADMIN` |
| `GET` | `/api/v1/library/my-loans` | My borrowed books | Authenticated |

</details>

<details>
<summary><b>🎉 Campus Events & Hackathons</b></summary>

| Method | Endpoint | Description | Roles |
|:------:|----------|-------------|:-----:|
| `POST` | `/api/v1/events` | Create campus event | `FACULTY`, `ADMIN` |
| `GET` | `/api/v1/events/upcoming` | Upcoming events | Authenticated |
| `GET` | `/api/v1/events/category/{category}` | Events by category | Authenticated |
| `POST` | `/api/v1/events/{id}/rsvp` | RSVP / Register for event | Authenticated |
| `DELETE`|`/api/v1/events/{id}` | Cancel event | `ADMIN`, `SUPER_ADMIN` |

</details>

<details>
<summary><b>🚌 Campus Fleet & Transport</b></summary>

| Method | Endpoint | Description | Roles |
|:------:|----------|-------------|:-----:|
| `POST` | `/api/v1/transport/routes` | Add bus route | `TRANSPORT_MANAGER`, `ADMIN` |
| `GET` | `/api/v1/transport/routes` | List active bus routes | Authenticated |
| `DELETE`|`/api/v1/transport/routes/{id}` | Deactivate route | `TRANSPORT_MANAGER`, `ADMIN` |

</details>

<details>
<summary><b>🔔 In-App Notifications</b></summary>

| Method | Endpoint | Description | Roles |
|:------:|----------|-------------|:-----:|
| `GET` | `/api/v1/notifications` | User notifications (paginated) | Authenticated |
| `GET` | `/api/v1/notifications/unread-count` | Unread notification count | Authenticated |
| `PATCH`| `/api/v1/notifications/{id}/read` | Mark single alert as read | Authenticated |
| `PATCH`| `/api/v1/notifications/read-all` | Mark all alerts as read | Authenticated |

</details>

<details>
<summary><b>📥 CSV Data Export</b></summary>

| Method | Endpoint | Description | Roles |
|:------:|----------|-------------|:-----:|
| `GET` | `/api/v1/export/attendance/course/{id}/date/{date}` | Export daily attendance CSV | `FACULTY`, `ADMIN` |
| `GET` | `/api/v1/export/courses` | Export entire course catalog CSV | `FACULTY`, `ADMIN` |

</details>

---

## 🔒 Security Architecture

```
                                  SECURITY ARCHITECTURE
                                  
 ┌─────────────────────────────────────────────────────────────────────────────┐
 │  1. Transport Security: TLS 1.3 / HTTPS / HSTS                             │
 └──────────────────────────────────────┬──────────────────────────────────────┘
                                        │
 ┌──────────────────────────────────────▼──────────────────────────────────────┐
 │  2. Token Bucket Rate Limiting: 120 requests/minute per client IP           │
 └──────────────────────────────────────┬──────────────────────────────────────┘
                                        │
 ┌──────────────────────────────────────▼──────────────────────────────────────┐
 │  3. Stateless JWT Filter: Extract Bearer token -> HMAC-SHA256 verification  │
 └──────────────────────────────────────┬──────────────────────────────────────┘
                                        │
 ┌──────────────────────────────────────▼──────────────────────────────────────┐
 │  4. Authorization Layer: Method-level @PreAuthorize role enforcement        │
 │     • SUPER_ADMIN  • ADMIN  • FACULTY  • STUDENT                            │
 │     • LIBRARIAN    • TRANSPORT_MANAGER • SECURITY                           │
 └──────────────────────────────────────┬──────────────────────────────────────┘
                                        │
 ┌──────────────────────────────────────▼──────────────────────────────────────┐
 │  5. Exception Handler: RFC-7807 compliant structured JSON error responses   │
 └─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🧪 Testing & Code Quality

```bash
# Run the entire test suite (unit + integration)
./mvnw test -Dspring.profiles.active=test

# Run tests with coverage verification
./mvnw clean test jacoco:report
```

---

## 📄 License

Distributed under the **MIT License**. See [LICENSE](LICENSE) for details.

---

<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=0:0066FF,100:00D4FF&height=120&section=footer" width="100%"/>
</p>

<p align="center">
  <strong>Built with ❤️ for intelligent campus ecosystems</strong>
</p>
