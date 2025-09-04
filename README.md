# Online Bookstore System

A Spring Boot web application for browsing and purchasing books with user authentication, shopping cart functionality, admin management, email verification, password reset, promo codes, and promotions signup.
 ## 🚀 Live Deployment

  **Running Live with ECS and RDS at:**
  http://54.234.177.133:8080/

  ---

  ### 🌐 Production Environment
  - **Platform:** AWS ECS Fargate
  - **Database:** Amazon RDS MySQL
  - **Container Registry:** Amazon ECR
  - **Status:** ✅ Active
## Technology Stack

- Spring Boot 3.5.3 (Java 17)
- Spring Security 6 with Remember Me
- Thymeleaf + Bootstrap 4
- MySQL 8.0 with JPA/Hibernate
- Email Integration (Gmail SMTP)
- Maven
- Docker & Docker Compose

## Quick Start with Docker

1. Ensure Docker Desktop is running
2. Clone the repository
3. Navigate to the project directory: `cd obs`
4. Run the application:

```bash
docker-compose up -d --build
```

The application will be available at: http://localhost:8080

**Admin Login:**
- Username: `admin`
- Password: `password`

**Database Access:**
- Host: `localhost`
- Port: `3307`
- Database: `obs_db`
- Username: `obs_user`
- Password: `obs_password`

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

## Project Structure

```
src/main/java/com/example/obs/
├── config/          # Security and password configuration
├── controller/      # Web controllers (Auth, Cart, Checkout, Profile, etc.)
├── model/          # Domain entities (User, Book, Order, PromoCode)
├── repository/     # Data access layer
├── service/        # Business logic (Email, PromoCode, User services)
└── ObsApplication.java
```

## Available URLs

### Public Routes
- `/` - Homepage
- `/book/{id}` - Book details
- `/search` - Search books by title/author/genre
- `/login` - Login page (with Remember Me)
- `/register` - User registration (with promotions signup)
- `/forgot-password` - Password reset request
- `/reset-password` - Password reset form
- `/verify-email` - Email verification

### User Routes (Authenticated)
- `/cart` - Shopping cart
- `/checkout` - Checkout process (with promo codes)
- `/profile` - User profile (with promotions settings)

### Admin Routes
- `/admin/books` - Manage books
- `/admin/users` - Manage users
- `/admin/orders` - View orders
- `/admin/promo-codes` - Manage promo codes

## Features

- **User Authentication**: Registration, login, logout, email verification
- **Password Management**: Forgot/reset password via email
- **Shopping**: Browse books, search, cart, checkout with promo codes
- **Promotions**: User signup for promotional emails, admin-managed promo codes
- **Email Integration**: Verification, password reset, welcome emails
- **Admin Panel**: Manage books, users, orders, and promo codes
- **Security**: Remember me, CSRF protection, encrypted passwords
- **Responsive Design**: Bootstrap 4 with custom styling
