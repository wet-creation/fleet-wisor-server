# FleetWisor Server

FleetWisor Server is the backend service of a multi-user fleet management information system. It enables centralized management of vehicles, drivers, trips, maintenance, and fuel costs for private fleets.

## ⚙️ Tech Stack

- **Kotlin + Ktor** – Backend API framework
- **MySQL** – Relational database
- **Flyway** – Database migration and version control
- **Ktorm** – ORM for Kotlin
- **HikariCP** – JDBC connection pool
- **JWT** – Authentication (access and refresh tokens)
- **Docker + Docker Compose** – Containerization and orchestration
- **MinIO** – Object storage for files (e.g., driver license scans)

## 📁 Project Structure

```
.
├── .env                      # Environment variables
├── docker-compose.yml        # Compose configuration (app, MySQL, MinIO)
└── README.md
```

## 🚀 Quick Start

### Prerequisites

- Docker
- Docker Compose

### Launch the App

```bash
git clone https://github.com/your-username/fleetwisor-server.git
cd fleetwisor-server
cp .env.example .env   # Fill in environment variables
docker-compose up --build
```

The server will be available at: `http://localhost:8080`

## 🔐 Authentication

- Implemented via JWT (access & refresh tokens)
- User roles:
  - `OWNER` – full control over the fleet
  - `DRIVER` – access to assigned vehicles and trips

## 📌 API Features

- User registration and login
- CRUD operations for:
  - Vehicles
  - Drivers
  - Maintenance records
  - Fuel fill-ups
  - Vehicle-driver assignments
- File uploads to MinIO
- Report generation

## 🧪 Testing

```bash
./gradlew test
```

## 📦 Production Deployment

> Use `Dockerfile` and `docker-compose.yml` for easy deployment on a Linux host (e.g., Ubuntu 22.04).
