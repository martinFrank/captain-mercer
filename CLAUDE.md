# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Captain Mercer is a full-stack space trading/captain-themed web game with a Spring Boot backend and React TypeScript frontend, deployed via Docker.

## Build and Run Commands

### Backend (from /backend directory)
```bash
./mvnw clean test          # Run tests
./mvnw clean package       # Build JAR
./mvnw spring-boot:run     # Run locally
```

### Frontend (from /frontend directory)
```bash
npm install                # Install dependencies
npm run dev                # Development server
npm run build              # Production build
npm run lint               # Run ESLint
```

### Docker (from root directory)
```bash
docker compose -f docker-compose.yml build    # Build containers
docker compose -f docker-compose.yml up -d    # Start detached
docker compose -f docker-compose.yml down     # Stop containers
```

### Access Points
- Frontend: http://localhost:8480/captain-mercer/login
- API: http://localhost:8482/captain-mercer-api
- Test credentials: username=martin, password=geheim123

## Architecture

### Backend (Spring Boot 3.5.6, Java 25)
Layered architecture in `backend/src/main/java/com/github/martinfrank/elitegames/backend/`:

- **Controllers**: REST endpoints (`GameController`, `AuthController`, `UserController`, `AdminController`)
- **Services**: Business logic (`GameService`, `SectorService`, `UserService`)
- **Mappers**: DTO conversion layer decoupling API from JPA entities
- **Entities**: JPA entities (`GameEntity`, `ShipEntity`, `SectorEntity`, `StarEntity`, `EquipmentEntity`, `UserEntity`)
- **Repositories**: Spring Data JPA repositories
- **Security**: JWT authentication (`JwtService`, `JwtAuthenticationFilter`, `SecurityConfig`)
- **Exception**: Global exception handling via `GlobalExceptionHandler`

Database: PostgreSQL (production), H2 (testing)

### Frontend (React 19, TypeScript 5.8, Vite 7)
Component-based architecture in `frontend/src/`:

- **auth/**: Authentication context and login page
- **components/common/**: Reusable UI components (`ErrorMessage`, `LoadingSpinner`, `ViewToggle`, `InfoItem`)
- **components/game/**: Game-specific components (`GameView`, `SectorView`, `CaptainHUD`, `ShipStatusView`, `SystemsPanel`, `ShipMarker`, `StarMarker`)
- **pages/**: Page components (`HomePage`, `ProfilePage`)
- **api.ts**: Axios client with JWT token injection

### API Contract
- Base path: `/captain-mercer-api`
- Auth: JWT Bearer tokens via `Authorization` header
- Endpoints: `/api/auth/*`, `/api/game/*`, `/api/user/*`, `/api/admin/*`, `/metrics/*`

## Key Patterns

- DTOs and mappers separate API contracts from JPA entities
- React Context for authentication state
- Docker multi-stage builds for both backend and frontend
- Nginx serves frontend static files in production
