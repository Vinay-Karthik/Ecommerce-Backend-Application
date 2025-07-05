# E-commerce Backend Application

A robust and scalable e-commerce backend application built with Spring Boot 3.4.3, Spring Security, JWT authentication, and MySQL database. This application provides RESTful APIs for an e-commerce platform, handling user authentication, product management, orders, and more.

## Features

- **User Authentication & Authorization**
  - JWT-based authentication
  - Role-based access control (Admin, User)
  - Secure password storage with encryption

- **Product Management**
  - CRUD operations for products
  - Product categorization
  - Search and filter products

- **Order Processing**
  - Shopping cart functionality
  - Order creation and management
  - Order history

- **User Management**
  - User registration and profile management
  - Address management
  - Order tracking

- **Email Notifications**
  - Order confirmation emails
  - Account-related notifications

## Tech Stack

- **Backend Framework**: Spring Boot 3.4.3
- **Security**: Spring Security, JWT
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **Java Version**: 23
- **Dependency Management**: Maven
- **Documentation**: Swagger/OpenAPI (if implemented)

## Prerequisites

- Java 23 or later
- MySQL 8.0 or later
- Maven 3.6.0 or later
- Git (for version control)

## Getting Started

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ecommerce
   ```

2. **Configure Database**
   - Create a MySQL database
   - Update the `application.properties` or `application.yml` with your database credentials

3. **Build the Application**
   ```bash
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**
   - The application will be available at `http://localhost:8080`
   - API documentation (if Swagger is enabled) at `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Authenticate user and get JWT token
- `POST /api/auth/refresh-token` - Refresh JWT token

### Users
- `GET /api/users/me` - Get current user profile
- `PUT /api/users/me` - Update user profile
- `GET /api/users/addresses` - Get user addresses
- `POST /api/users/addresses` - Add new address

### Products
- `GET /api/products` - Get all products (with pagination)
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product (Admin only)
- `PUT /api/products/{id}` - Update product (Admin only)
- `DELETE /api/products/{id}` - Delete product (Admin only)

### Categories
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}/products` - Get products by category

### Cart
- `GET /api/cart` - Get user's cart
- `POST /api/cart/items` - Add item to cart
- `PUT /api/cart/items/{itemId}` - Update cart item quantity
- `DELETE /api/cart/items/{itemId}` - Remove item from cart

### Orders
- `GET /api/orders` - Get user's orders
- `POST /api/orders` - Create new order
- `GET /api/orders/{id}` - Get order details
- `PUT /api/orders/{id}/cancel` - Cancel order

## Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based authorization
- CSRF protection
- Input validation

## Error Handling

- Custom exception handling
- Meaningful error messages
- Proper HTTP status codes

## Testing

To run tests:
```bash
mvn test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For any queries, please contact the project maintainers.
