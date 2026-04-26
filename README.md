# Backend Intern Assignment — REST API with JWT Auth

A Spring Boot REST API with JWT authentication, role-based access control, and Task CRUD operations.

---

## Tech Stack
- Java 17 + Spring Boot 3
- Spring Security + JWT (jjwt 0.11.5)
- PostgreSQL
- Swagger (springdoc-openapi)

---

## Setup & Run

### 1. Clone the project
git clone https://github.com/your-username/BackendIntern.git
cd BackendIntern

### 2. Configure environment variables
Create or edit src/main/resources/application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

### 3. Run the project
./mvnw spring-boot:run

App starts at: http://localhost:8080
Swagger UI at: http://localhost:8080/swagger-ui/index.html

---

## API Endpoints

### Auth (Public)
| Method | URL                        | Description         |
|--------|----------------------------|---------------------|
| POST   | /api/v1/auth/register      | Register new user   |
| POST   | /api/v1/auth/login         | Login & get token   |

### Tasks (Protected — Bearer Token required)
| Method | URL                        | Description         |
|--------|----------------------------|---------------------|
| POST   | /api/v1/tasks              | Create a task       |
| GET    | /api/v1/tasks              | Get all tasks       |
| GET    | /api/v1/tasks/{id}         | Get task by ID      |
| PUT    | /api/v1/tasks/{id}         | Update a task       |
| DELETE | /api/v1/tasks/{id}         | Delete a task       |

---

## Sample Requests & Responses

### Register
POST /api/v1/auth/register
Content-Type: application/json

Request:
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}

Response:
"User registered successfully"

---

### Login
POST /api/v1/auth/login
Content-Type: application/json

Request:
{
  "email": "john@example.com",
  "password": "password123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}

---

### Create Task (Auth required)
POST /api/v1/tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

Request:
{
  "title": "Complete assignment",
  "description": "Finish the backend intern task",
  "status": "PENDING"
}

Response:
{
  "id": 1,
  "title": "Complete assignment",
  "description": "Finish the backend intern task",
  "status": "PENDING"
}

---

### Get All Tasks (Auth required)
GET /api/v1/tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

Response:
[
  {
    "id": 1,
    "title": "Complete assignment",
    "description": "Finish the backend intern task",
    "status": "PENDING"
  }
]

---

## Security
- Passwords hashed with BCrypt
- JWT tokens expire in 10 hours
- Role-based access: USER and ADMIN roles supported
- Input validation on all request bodies