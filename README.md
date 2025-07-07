# Online Bookstore System (OBS)

A full-featured online bookstore built with Spring Boot, featuring user authentication, shopping cart functionality, admin management, promotions, and genre-based filtering.

## 🏗️ Architecture Overview

### Technology Stack
- **Backend:** Spring Boot 3.5.3 (Java 17)
- **Web Framework:** Spring MVC
- **Security:** Spring Security 6
- **Template Engine:** Thymeleaf + Bootstrap 4
- **Database:** MySQL 8.0 with JPA/Hibernate
- **Build Tool:** Maven
- **Containerization:** Docker & Docker Compose

### Application Layers

```
┌─────────────────────────────────────────┐
│             Presentation Layer           │
│  (Thymeleaf Templates + Controllers)     │
├─────────────────────────────────────────┤
│             Business Layer              │
│           (Service Classes)             │
├─────────────────────────────────────────┤
│            Data Access Layer           │
│     (JPA Repositories + Entities)      │
├─────────────────────────────────────────┤
│            Database Layer              │
│              (MySQL 8.0)               │
└─────────────────────────────────────────┘
```

## 🚦 URL Routing & Controllers

### Public Routes
| URL | Controller | Method | Description |
|-----|------------|--------|-------------|
| `/` | HomeController | GET | Homepage with categorized books (Featured/Available/Coming Soon) |
| `/book/{id}` | BookController | GET | Individual book details |
| `/search` | SearchController | GET | Search books by title and/or genre |
| `/login` | AuthController | GET | User login page |
| `/register` | AuthController | GET/POST | User registration |
| `/book-images/{fileName}` | BookImageController | GET | Serve book cover images |

### Authenticated User Routes
| URL | Controller | Method | Description |
|-----|------------|--------|-------------|
| `/cart` | CartController | GET | View shopping cart |
| `/cart/add` | CartController | POST | Add book to cart |
| `/cart/clear` | CartController | POST | Clear shopping cart |
| `/cart/count` | CartController | GET | Get cart item count (AJAX) |
| `/checkout` | CheckoutController | GET/POST | Checkout process |
| `/checkout/confirmation` | CheckoutController | GET | Order confirmation |
| `/profile` | ProfileController | GET/POST | User profile management |

### Admin Routes (Role: ADMIN)
| URL | Controller | Method | Description |
|-----|------------|--------|-------------|
| `/admin` | AdminBookController | GET | Admin dashboard |
| `/admin/books` | AdminBookController | GET | Manage books |
| `/admin/books/new` | AdminBookController | GET | Add new book form |
| `/admin/books/edit/{id}` | AdminBookController | GET | Edit book form |
| `/admin/books/save` | AdminBookController | POST | Save book changes |
| `/admin/books/delete/{id}` | AdminBookController | GET | Delete book |
| `/admin/users` | AdminUserController | GET | Manage users |
| `/admin/users/delete/{id}` | AdminUserController | GET | Delete user |
| `/admin/orders` | AdminOrderController | GET | View all orders |
| `/admin/orders/{id}` | AdminOrderController | GET | View order details |
| `/admin/promos` | AdminPromoCodeController | GET | Manage promo codes |
| `/admin/promos/new` | AdminPromoCodeController | GET | Add promo code form |
| `/admin/promos/edit/{id}` | AdminPromoCodeController | GET | Edit promo code |
| `/admin/promos/save` | AdminPromoCodeController | POST | Save promo code |
| `/admin/promos/delete/{id}` | AdminPromoCodeController | GET | Delete promo code |
| `/admin/promos/toggle/{id}` | AdminPromoCodeController | POST | Toggle promo active status |

## 🗄️ Database Schema

### Core Entities

#### Book Entity
```java
@Entity
@Table(name = "book")
public class Book {
    @Id @GeneratedValue
    private Long id;
    private String title;
    private String author;
    private String genre;
    private String imageUrl;
    private BigDecimal price;
    private boolean comingSoon;
    private String description;
    
    // Promotion fields
    private boolean isOnSale;
    private BigDecimal discountPercentage;
}
```

#### User Entity
```java
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role; // "USER" or "ADMIN"
    
    // Profile fields
    private String firstName, lastName;
    private String address, city, state, zipCode;
    private String cardNumber, expirationDate, cvv;
}
```

#### Order & OrderItem Entities
```java
@Entity
public class Order {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    private User user;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;
}

@Entity
public class OrderItem {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    private Order order;
    @ManyToOne
    private Book book;
    private int quantity;
    private BigDecimal price;
}
```

#### CartItem Entity
```java
@Entity
public class CartItem {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Book book;
    private int quantity;
}
```

#### PromoCode Entity
```java
@Entity
public class PromoCode {
    @Id @GeneratedValue
    private Long id;
    private String code;
    private String description;
    private BigDecimal discountPercentage;
    private BigDecimal minimumOrderAmount;
    private int usageLimit;
    private int timesUsed;
    private LocalDate expiryDate;
    private boolean isActive;
}
```

### Database Relationships
- **User ↔ Order**: One-to-Many (User can have multiple orders)
- **Order ↔ OrderItem**: One-to-Many (Order contains multiple items)
- **OrderItem ↔ Book**: Many-to-One (Items reference books)
- **User ↔ CartItem**: One-to-Many (User has cart items)
- **CartItem ↔ Book**: Many-to-One (Cart items reference books)

## 🔒 Security Configuration

- **Authentication**: Username/password with session management
- **Authorization**: Role-based access control (USER/ADMIN)
- **Default Admin**: `admin`/`password`
- **Session Timeout**: 30 minutes
- **Password Encoding**: BCrypt

### Security Rules
- Public access: `/`, `/book/**`, `/search`, `/login`, `/register`, `/book-images/**`
- Authenticated required: `/cart/**`, `/checkout/**`, `/profile/**`
- Admin only: `/admin/**`

## 🚀 Key Features

### User Features
- **Authentication**: Login/Register with role-based access
- **Book Browsing**: Homepage with Featured/Available/Coming Soon sections
- **Search & Filter**: Search by title and filter by genre
- **Shopping Cart**: Add books, view cart, manage quantities
- **Checkout**: Complete purchase with order confirmation
- **Profile Management**: Update personal info, address, payment details

### Admin Features
- **Book Management**: CRUD operations with image upload
- **User Management**: View and delete users
- **Order Management**: View all orders and order details
- **Promotion Management**: Create and manage promo codes and book sales
- **Dashboard**: Overview of system status

### Promotion System
- **Book Sales**: Individual book discounts with visual indicators
- **Promo Codes**: Reusable discount codes with usage limits and expiry
- **Pricing Display**: Original price, discounted price, and savings amount

## 🗂️ Project Structure

```
obs/
├── src/main/java/com/example/obs/
│   ├── controller/          # Spring MVC Controllers
│   ├── model/              # JPA Entities
│   ├── repository/         # Spring Data JPA Repositories
│   ├── service/            # Business Logic Services
│   ├── config/             # Configuration Classes
│   └── DataLoader.java     # Sample Data Initialization
├── src/main/resources/
│   ├── templates/          # Thymeleaf Templates
│   │   ├── admin/         # Admin Panel Templates
│   │   └── *.html         # User Interface Templates
│   ├── static/            # CSS, JS, Images
│   └── application.properties
├── docker-compose.yml      # Docker Configuration
├── Dockerfile             # Application Container
└── pom.xml               # Maven Dependencies
```

## 🏃‍♂️ Running the Application

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+ or Docker

### Local Development
```bash
# Clone repository
git clone https://github.com/samballington/SWE_Project.git
cd SWE_Project/obs

# Run with Maven
./mvnw spring-boot:run

# Or build and run JAR
./mvnw package -DskipTests
java -jar target/online-bookstore-0.0.1-SNAPSHOT.jar
```

### Docker Deployment
```bash
# Run with Docker Compose (includes MySQL)
docker-compose up --build

# Access application
http://localhost:8080
```

### Database Configuration
The application uses environment-based configuration:
- **URL**: `${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/obs_db}`
- **Username**: `${SPRING_DATASOURCE_USERNAME:root}`
- **Password**: `${SPRING_DATASOURCE_PASSWORD:Hellonghi@123}`

## 📊 Spring Boot Annotations Used

### Controller Annotations
- `@Controller`: Marks classes as Spring MVC controllers
- `@RequestMapping("/path")`: Maps controllers to base URLs
- `@GetMapping`, `@PostMapping`: Maps methods to HTTP verbs
- `@PathVariable`, `@RequestParam`: Extracts URL parameters

### JPA Annotations
- `@Entity`: Marks classes as database entities
- `@Table(name="table_name")`: Specifies table names
- `@Id`, `@GeneratedValue`: Primary key configuration
- `@ManyToOne`, `@OneToMany`: Relationship mappings
- `@Column`: Column-specific configurations

### Security Annotations
- `@PreAuthorize`: Method-level security
- `@Secured`: Role-based access control

### Service Annotations
- `@Service`: Marks business logic classes
- `@Repository`: Marks data access classes
- `@Autowired`: Dependency injection

This architecture provides a scalable, maintainable foundation for an e-commerce bookstore with clear separation of concerns and comprehensive feature coverage. 