# Streaming Manager

A platform for managing live broadcasts and recorded videos across multiple platforms (e.g., YouTube, Twitch, 치지직).  
This project is built using **Java** and **Spring Boot**, and uses **PostgreSQL** for database management.

## Features
- Manage videos and live stream records from multiple platforms.
- Track and analyze follower statistics and other data.
- Dockerized environment for development and deployment.

## Tech Stack
- **Language**: Java
- **Framework**: Spring Boot
- **Database**: PostgreSQL
- **Infrastructure**: Docker, Kubernetes (optional for scaling)
- **Build Tool**: Gradle

## Prerequisites
- Java 17 or higher
- Docker and Docker Compose
- PostgreSQL
- IntelliJ IDEA (optional, recommended)

## Getting Started

### Clone the Repository
```bash
git clone https://github.com/your-repo/streaming-manager.git
cd streaming-manager
```

### Start the Environment
1. Build and start the containers using Docker Compose:
```bash
docker-compose up
```
2. The application will be available at `http://localhost:8080`.

## License
This project is licensed under the MIT License.