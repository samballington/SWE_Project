# Online Bookstore System

A Spring Boot web application for browsing and purchasing books with user authentication, shopping cart functionality, and admin management.

## Technology Stack

- Spring Boot 3.5.3 (Java 17)
- Spring Security 6
- Thymeleaf + Bootstrap 4
- MySQL 8.0 with JPA/Hibernate
- Maven
- Docker & Docker Compose

## Quick Start with Docker

1. Ensure Docker Desktop is running
2. Clone the repository
3. Navigate to the project directory
4. Run the application:

```bash
docker-compose up -d
```

The application will be available at: http://localhost:8080

## Manual Setup

### Prerequisites
- Java 17
- Maven 3.6+
- MySQL 8.0

### Database Setup
1. Create MySQL database:
```sql
CREATE DATABASE obs_db;
```

2. Copy `application-template.properties` to `application.properties`
3. Update database credentials in `application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application
```bash
./mvnw spring-boot:run
```

## Default Admin Credentials
- Username: admin
- Password: password

## Project Structure

```
src/main/java/com/example/obs/
├── config/          # Security configuration
├── controller/      # Web controllers
├── model/          # Domain entities
├── repository/     # Data access layer
├── service/        # Business logic
└── ObsApplication.java
```

## Available URLs

### Public Routes
- `/` - Homepage
- `/book/{id}` - Book details
- `/search` - Search books
- `/login` - Login page
- `/register` - User registration

### User Routes (Authenticated)
- `/cart` - Shopping cart
- `/checkout` - Checkout process
- `/profile` - User profile

### Admin Routes
- `/admin` - Admin dashboard
- `/admin/books` - Manage books
- `/admin/users` - Manage users
- `/admin/orders` - View orders
