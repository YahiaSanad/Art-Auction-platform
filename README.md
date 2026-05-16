# Art Auction Platform (Spring Boot Version)

This branch (`spring-boot`) contains the Spring Boot microservices implementation of the Art Auction platform.

## Branches
- `main`: .NET version
- `spring-boot`: Spring Boot microservices version (this branch)

## Architecture At A Glance
- Microservices are organized by business domain (`Authentication`, `Artist`, `ArtworkPost`, `Auction`, `Notification`).
- Service discovery is handled by `DiscoveryServer` (Eureka).
- Centralized configuration is handled by `ConfigServer`.
- External traffic enters through `ApiGateway`.
- Services communicate with each other using event-driven Kafka messaging (Spring Cloud Stream).
- A React frontend (`Frontend`) consumes the backend APIs through the gateway.

## Repository Structure
- `ApiGateway`: Gateway routing and security filters.
- `ConfigServer`: Central config service for all Spring services.
- `DiscoveryServer`: Eureka registry for service discovery.
- `AuthenticationServices`: Auth, buyer/admin validation, buyer profile data for other services.
- `ArtistServices`: Artist profile and admin-driven artist approval.
- `ArtworkPostServices`: Artwork listing lifecycle, approvals, watchlist, post metadata.
- `AuctionServices`: Bidding, sold posts, auction scheduling/business flow.
- `NotificationServices`: Email delivery service.
- `Frontend`: React + Vite web client.
- `docker-compose.yaml`: Local orchestration of infrastructure and services.

## Core Service Responsibilities
| Service | Main Responsibility |
| --- | --- |
| `AuthenticationServices` | User auth and identity checks used by other services |
| `ArtistServices` | Artist records and artist-related admin workflows |
| `ArtworkPostServices` | Artwork post creation/update/approval and post queries |
| `AuctionServices` | Bids, auction ending logic, and sold-post workflows |
| `NotificationServices` | Email notifications (consumes notification events) |

## Event-Driven Communication (Kafka)
This codebase uses Kafka in a request/reply style for cross-service data and validation checks, plus fire-and-forget events for notifications.

### Pattern Used In Code
- Request publisher classes: `Kafka/Request/KafkaRequestReplyService.java`
- Reply listener classes: `Kafka/Request/KafkaReplyListener.java`
- Reply handler classes: `Kafka/Reply/*KafkaHandler.java` (or `AuthenticationKafkaHandler`)
- Notification consumer: `NotificationServices/.../Kafka/EmailKafkaHandler.java`

Each request message includes a `correlationId` header. The receiving service replies with the same `correlationId`, and the caller matches it to a waiting `CompletableFuture`.

### Main Kafka Request/Reply Flows
| From -> To | Request Topic | Reply Topic | Purpose |
| --- | --- | --- | --- |
| `AuctionServices` -> `AuthenticationServices` | `auction.check.buyer.request` | `auction.check.buyer.reply` | Validate buyer existence |
| `AuctionServices` -> `AuthenticationServices` | `auction.buyer.data.request` | `auction.buyer.data.reply` | Fetch buyer info |
| `AuctionServices` -> `ArtworkPostServices` | `auction.check.post.request` | `auction.check.post.reply` | Validate post availability |
| `AuctionServices` -> `ArtworkPostServices` | `auction.post.data.request` | `auction.post.data.reply` | Fetch post details |
| `AuctionServices` -> `ArtworkPostServices` | `auction.ended.posts.request` | `auction.ended.posts.reply` | Fetch ended post IDs |
| `AuctionServices` -> `ArtworkPostServices` | `auction.update.buynowprice.request` | `auction.update.buynowprice.reply` | Update buy-now price |
| `AuctionServices` -> `ArtworkPostServices` | `auction.ended.post.data.request` | `auction.ended.post.data.reply` | Fetch ended post details |
| `ArtworkPostServices` -> `AuthenticationServices` | `artworkpost.check.admin.request` | `artworkpost.check.admin.reply` | Validate admin |
| `ArtworkPostServices` -> `AuthenticationServices` | `artworkpost.check.buyer.request` | `artworkpost.check.buyer.reply` | Validate buyer |
| `ArtworkPostServices` -> `ArtistServices` | `artworkpost.check.artist.request` | `artworkpost.check.artist.reply` | Validate artist |
| `ArtworkPostServices` -> `ArtistServices` | `artworkpost.artist.data.request` | `artworkpost.artist.data.reply` | Fetch artist profile |
| `ArtworkPostServices` -> `AuctionServices` | `artworkpost.bids.data.request` | `artworkpost.bids.data.reply` | Fetch bids for post details |
| `ArtistServices` -> `AuthenticationServices` | `artist.check.admin.request` | `artist.check.admin.reply` | Validate admin for artist approval |

### Fire-And-Forget Flow
| From -> To | Topic | Purpose |
| --- | --- | --- |
| `AuctionServices` -> `NotificationServices` | `auction.send.email` | Send auction email notifications |

## Runtime Ports (Docker Compose)
- Gateway: `8000`
- Authentication: `8084`
- Artist: `8081`
- ArtworkPost: `8082`
- Auction: `8083`
- Notification: `8085`
- Config Server: `8888`
- Discovery Server: `8761`
- Kafka: `9092`
- SQL Server: `1433`

## Prerequisites
- Java 17
- Maven 3.9+
- Docker + Docker Compose
- Node.js 20+
- pnpm

## Run With Docker Compose
```bash
docker compose up --build
```

## Frontend (Optional Local Run)
```bash
cd Frontend
pnpm install
pnpm dev
```

## Environment Variables To Set
Set these before running services that need external credentials:
- `CONFIG_REPO_GIT_PASSWORD`
- `SMTP_USERNAME`
- `SMTP_APP_PASSWORD`
- `SPRING_MAIL_HOST`
- `SPRING_MAIL_PORT`

## Notes
- `node_modules` and build artifacts are excluded from git.
- Keep all secrets out of source code and use environment variables.
- The current Kafka request/reply timeout in request services is very short (`50ms` in code) and should usually be increased for non-local deployments.
