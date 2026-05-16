# Art Auction Platform (.NET Version)

This branch (`main`) contains the .NET-based version of the Art Auction platform.

## Branches
- `main`: .NET version (this branch)
- `spring-boot`: Spring Boot microservices version

## Project Structure
- `Project/Backend/src/ArtAuction.API`: ASP.NET Core API
- `Project/Backend/src/ArtAuction.Application`: Application layer
- `Project/Backend/src/ArtAuction.Infrastructure`: Infrastructure and persistence
- `Project/Backend/tests/ArtAuction.Tests`: Test project
- `Frontend`: React + Vite frontend

## Prerequisites
- .NET SDK 10.0 (preview)
- SQL Server
- Node.js 20+
- pnpm

## Run Backend
```bash
cd Project/Backend/src/ArtAuction.API
dotnet restore
dotnet run
```

## Run Frontend
```bash
cd Frontend
pnpm install
pnpm dev
```

## Notes
- Keep secrets and credentials out of source code before deployment.
- `node_modules` and build output are ignored by git.
