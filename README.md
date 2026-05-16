# Art Auction Platform (.NET Version)

This branch (`main`) contains the .NET-based implementation of the Art Auction platform.

## Branches
- `main`: .NET version (this branch)
- `spring-boot`: Spring Boot microservices version

## Architecture At A Glance
- The backend is organized in layered projects for API, application logic, and infrastructure.
- The frontend is a React + Vite application that consumes backend APIs.
- The codebase separates transport concerns (HTTP), business rules, and persistence/integration concerns.

## Repository Structure
- `Project/ArtAuction.slnx`: Solution entry point.
- `Project/Backend/src/ArtAuction.API`: ASP.NET Core API (controllers, startup, app configuration).
- `Project/Backend/src/ArtAuction.Application`: Application layer (DTOs, service contracts/implementations, mappings, entities).
- `Project/Backend/src/ArtAuction.Infrastructure`: Infrastructure layer (EF Core context, repositories, migrations, integrations, SignalR).
- `Project/Backend/tests/ArtAuction.Tests`: Backend automated tests.
- `Frontend`: React + Vite frontend.

## Backend Code Structure Details
### `ArtAuction.API`
- `Program.cs`: app startup and dependency registration.
- `Controllers/`: HTTP endpoints grouped by feature.
- `appsettings*.json`: environment-specific runtime settings.

### `ArtAuction.Application`
- `DTOs/`: request/response contracts.
- `Interfaces/Services`: business service contracts.
- `Interfaces/Repositories`: data access contracts.
- `Services/`: core application workflows.
- `Mappings/`: AutoMapper profiles.
- `Entities/`: core domain entities.

### `ArtAuction.Infrastructure`
- `Persistence/`: EF Core `DbContext` and entity configuration.
- `Repositories/`: repository implementations.
- `Migrations/`: schema + seed migration history.
- `SignalR/`: real-time auction communication components.
- `Services/`: infrastructure-facing services.

## Frontend Structure
- `Frontend/src/pages`: route-level screens.
- `Frontend/src/components`: reusable UI/feature components.
- `Frontend/src/api` and `Frontend/src/services`: backend communication layer.
- `Frontend/src/store`: app/auth state.
- `Frontend/src/utils`: helper and validation utilities.

## Prerequisites
- .NET SDK 10.0 (preview)
- SQL Server
- Node.js 20+
- pnpm

## Backend Setup
1. Configure database/JWT settings in:
- `Project/Backend/src/ArtAuction.API/appsettings.json`
2. Restore and run the API:

```bash
cd Project/Backend/src/ArtAuction.API
dotnet restore
dotnet run
```

## Run Backend Tests
```bash
cd Project/Backend/tests
dotnet test
```

## Run Frontend
```bash
cd Frontend
pnpm install
pnpm dev
```

## Notes
- This branch is independent from `spring-boot`; both represent the same domain with different backend stacks.
- Keep secrets and credentials out of source code before deployment.
- `node_modules` and build output are ignored by git.
