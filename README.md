# Art Auction Platform (Spring Boot Version)

This branch (`spring-boot`) contains the Spring Boot microservices version of the Art Auction platform.

## Branches
- `main`: .NET version
- `spring-boot`: Spring Boot version (this branch)

## Services
- `ConfigServer`
- `DiscoveryServer`
- `ApiGateway`
- `AuthenticationServices`
- `ArtistServices`
- `ArtworkPostServices`
- `AuctionServices`
- `NotificationServices`
- `Frontend` (React + Vite)

## Prerequisites
- Java 17
- Maven 3.9+
- Docker and Docker Compose
- Node.js 20+
- pnpm

## Run With Docker Compose
```bash
docker compose up --build
```

## Notes
- Set sensitive values (for example `CONFIG_REPO_GIT_PASSWORD`, `SMTP_USERNAME`, `SMTP_APP_PASSWORD`) in your environment before running.
- `node_modules` and build output are ignored by git.
