# Scalability Notes

## Current Architecture
Single Spring Boot monolith connected to PostgreSQL with JWT-based stateless authentication.

---

## 1. Microservices Split
The application can be split into independent services:

- Auth Service → handles /api/v1/auth/** (register, login, token validation)
- Task Service → handles /api/v1/tasks/** (CRUD operations)
- API Gateway  → single entry point, routes to each service (Spring Cloud Gateway)

Each service has its own database, scales independently, and can be deployed separately.

---

## 2. Redis Caching
- Cache JWT blacklist (logged-out tokens) in Redis with TTL equal to token expiry
- Cache frequently accessed task lists to reduce DB queries
- Session store for rate limiting per user

Add to pom.xml:
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

---

## 3. Load Balancing with Nginx
Run multiple instances of the app and balance traffic:

nginx.conf example:
upstream backend {
    server localhost:8080;
    server localhost:8081;
    server localhost:8082;
}

server {
    listen 80;
    location / {
        proxy_pass http://backend;
    }
}

JWT being stateless makes this seamless — any instance can verify any token
without shared session state.

---

## 4. Docker Deployment

Dockerfile:
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/BackendIntern-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

docker-compose.yml:
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/backenddb
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
    depends_on:
      - db

  db:
    image: postgres:15
    environment:
      POSTGRES_DB: backenddb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"

Build and run:
docker build -t backendintern .
docker-compose up

---

## 5. Additional Improvements
- Horizontal scaling on cloud (AWS ECS / GCP Cloud Run)
- Database connection pooling with HikariCP (already default in Spring Boot)
- Pagination on GET /tasks to handle large datasets
- Rate limiting per user using Bucket4j or Redis