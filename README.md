# LegalMetrix — SIH 2026 prototype

**Verify. Certify. Comply.** This repository starts a modular monolith for digital legal metrology services. It is an SIH prototype and is not an official government service. Legal rules and approval authority must be configured and validated by the competent authority before operational use.

## Phase 1 architecture

```mermaid
flowchart LR
  Browser[React + Vite + TypeScript] -->|JSON API, bearer access token| API[Spring Boot modular monolith]
  API --> Sec[Spring Security + JWT]
  API --> Domain[Identity, access, audit modules]
  Domain --> DB[(MySQL 8 / Flyway)]
  API -. future phases .-> Files[Private object storage abstraction]
  API -. future phases .-> Mail[Email provider]
```

The browser uses one authentication flow and routes users according to server-issued roles. The API owns authorization and is the source of truth. Phase 1 implements identity, role assignment, authentication, basic account administration, audit events, and shared dashboard shell. Business workflows, refresh-token rotation, email verification, password recovery, lockout/rate-limit persistence, and deployment hardening are subsequent work; optional integrations are disabled until configured. JWT signing material is supplied through environment variables. Passwords are stored using BCrypt.

### Role-permission matrix

| Capability | Owner | LMO | GATC | State admin | Central admin | Public | Helpdesk | System admin |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| Manage own profile | ✓ | ✓ | ✓ | ✓ | ✓ | — | ✓ | ✓ |
| Instruments/applications | Own | Assigned | Centre queue | State scope | Aggregate/config | Public verify | Assigned grievance | — |
| Statutory inspection/decision | — | Assigned authority | Authorized tests | Allocate/monitor | — | — | — | — |
| State user/office administration | — | — | — | State scope | — | — | — | — |
| Cross-state aggregate/config | — | — | — | — | ✓ | — | — | — |
| Grievances | Own | Assigned | Assigned | State scope | Aggregate | Submit/report | Assigned tickets | — |
| Security/account administration | — | — | — | Access review | — | — | — | ✓ |
| Audit visibility | Own events | Own/assigned | Own/centre | State scope | Aggregate | — | Assigned scope | Security events |

Roles are not interchangeable: system administration grants no statutory approval authority, and central administration cannot bypass workflow approvals. Public certificate verification remains anonymous. These are intended capability boundaries; workflow endpoints arrive in later phases.

### Core data relationships

```mermaid
erDiagram
  USER ||--o{ USER_ROLE : assigned
  ROLE ||--o{ USER_ROLE : grants
  USER ||--o| ORGANIZATION : represents
  ORGANIZATION ||--o{ INSTRUMENT : owns
  INSTRUMENT ||--o{ APPLICATION : subject
  APPLICATION ||--o{ APPOINTMENT : schedules
  APPLICATION ||--o{ INSPECTION : records
  INSPECTION ||--o| CERTIFICATE : may_issue
  CERTIFICATE ||--o{ CERTIFICATE_EVENT : lifecycle
  USER ||--o{ AUDIT_EVENT : performs
  USER ||--o{ GRIEVANCE : submits
```

Phase 1 persists users, roles, role assignments, and audit events. The remaining entities are planned relationships, not yet implemented. State/district/office scope will be represented explicitly before workflow data is added.

## Project structure

```text
backend/                 Spring Boot API, Maven, Flyway migrations
  src/main/java/...      config, identity, security, audit, common
  src/main/resources/    application profiles and migrations
  src/test/java/...      focused API/security tests
frontend/                Vite, React, TypeScript, Tailwind
  src/                    routes, auth, layouts, shared UI
docs/                     architecture, permissions, API notes
```

## Stack and dependency choices

Java 21, Spring Boot 3.5.x, Maven, Spring Web, Security, Data JPA, Validation, MySQL Connector/J, Flyway, JJWT, springdoc-openapi, JUnit 5/Mockito. Frontend: React, Vite, TypeScript, Tailwind, React Router, Axios, React Hook Form, Zod, Recharts, Lucide, and React Hot Toast. Versions are pinned in the build manifests; dependency compatibility should be checked when upgrading.

## Local setup (PowerShell)

Requirements: JDK 21, Node.js 20+, Docker Desktop with Compose. From the repository root:

```powershell
Copy-Item .env.example .env
docker compose up -d mysql
Set-Location backend
./mvnw spring-boot:run
```

In a second terminal:

```powershell
Set-Location frontend
npm install
npm run dev
```

Open `http://localhost:5173`. Swagger UI is at `http://localhost:8080/swagger-ui.html`. The backend reads environment variables from the process environment; export values from `.env` in your shell or use a local environment loader. Never use the development JWT secret in a deployed environment. Set `BOOTSTRAP_ADMIN_EMAIL` and `BOOTSTRAP_ADMIN_PASSWORD` before first startup to create a local system administrator; there are no committed demo credentials.

For the backend wrapper if it has not been generated locally, run `mvn -N wrapper:wrapper` with Maven installed, then use `./mvnw`. Alternatively run `mvn spring-boot:run` from `backend`.

## Phase 1 scope and status

This initial increment provides the architecture baseline, schema, shared authentication, role-aware shell, and account/profile basics. It is intentionally the first phase of the larger system; later phases add instrument registry, applications, inspections, certificates, grievances, reporting, and hardened deployment integrations. Do not put personal or production data into this prototype.

## Security and operations

Use TLS, a production-grade secret manager, restricted CORS, private object storage, backups with restore drills, monitoring, dependency scanning, and configured rate limiting before deployment. LegalMetrix does not claim formal authorization or compliance certification. External email/SMS, malware scanning, object storage, and payment services require separately configured providers.
