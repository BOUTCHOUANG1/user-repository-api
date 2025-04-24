# User Management API

A comprehensive Spring Boot-based User Management API that allows secure user authentication and profile management. This API supports user registration, authentication via JWT, and complete user CRUD operations, all documented with Swagger.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation & Setup](#installation--setup)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Authentication & Security](#authentication--security)
- [Testing Authentication](#testing-authentication)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- **User Authentication:** Secure login with JWT token generation and validation
- **User Registration:** Create new users with encrypted passwords
- **User Management:** CRUD operations for user profiles (create, read, update, delete)
- **Security:** Password encryption using BCrypt and JWT-based authentication
- **API Documentation:** Comprehensive Swagger/OpenAPI documentation for all endpoints

## Technology Stack

- **Java:** 17 or higher
- **Spring Boot:** REST API development
- **Spring Security:** Authentication and authorization
- **Spring Data JPA:** Database interactions
- **JWT:** JSON Web Token for stateless authentication
- **BCrypt**: Password hashing
- **Lombok:** Reducing boilerplate code in model classes
- **Springdoc OpenAPI:** API documentation (Swagger UI)
- **Database:** Postgresql

## Getting Started

### Prerequisites

- Java 17 or later installed
- Maven (or use the Maven Wrapper provided)
- An IDE such as IntelliJ IDEA or Eclipse
- PostgreSQL database

### Installation & Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/BOUTCHOUANG1/user-management-api.git 
   cd user-management-api
   
2. **Configure the database:**

- Modify src/main/resources/application.properties with your database credentials:

   ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/user_management
   spring.datasource.username=postgres
   spring.datasource.password=your_password

3. **Build the project using Maven:**

   ```bash
   mvn clean install

4. **Run the application:**

- The application will start on port 8080 by default.
  
   ```bash
   mvn spring-boot:run

## API Documentation

- The application’s API documentation is automatically generated with Springdoc OpenAPI (Swagger). After starting the application, open your browser and visit:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs
- This interactive documentation allows you to explore and test every endpoint directly from your browser.

## API Endpoints

- Below is an overview of the primary endpoints exposed by the API:

1. **Register a New User**

- **Endpoint:** POST /api/auth/signup
- **Description:** Creates a new user account with encrypted password
- **Request Body Example:** 

   ```Json
  {
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "securePassword123"
  }

2. **Authenticate a User**

- **Endpoint:** POST /api/auth/login
- **Description:** Validates credentials and returns a JWT token
- **Request Body Example:**

  ```Json
  {
  "email": "john.doe@example.com",
  "password": "securePassword123"
  }

- **Response Example:**

  ```Json
  {
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com"
  }


**User Management Endpoints (All require authentication)**

3. **Get All Users**

- **Endpoint:** GET /api/users
- **Description:** Retrieves all users in the system
- **Authentication:** Required (Bearer Token)

4. **Get User by ID**

- **Endpoint:** GET /api/users/{id}
- **Description:** Retrieves a specific user by their ID
- **Authentication:** Required (Bearer Token)

5. **Update User**

- **Endpoint:** PUT /api/users/{id}
- **Description:** Updates an existing user's information
- **Authentication:** Required (Bearer Token)
- **Request Body Example:** 

   ```Json
  {
  "name": "John Updated",
  "email": "john.updated@example.com",
  "password": "newPassword123"
  }

6. **Delete User**

- **Endpoint:** DELETE /api/users/{id}
- **Description:** Permanently removes a user from the system
- **Authentication:** Required (Bearer Token)
   
## Authentication & Security

This API implements JWT (JSON Web Token) authentication:

- **Stateless Authentication:** No session state is stored on the server
- **Token-Based:** All authenticated requests must include a valid JWT in the Authorization header
- **Password Encryption:** BCrypt is used to securely hash passwords before storage
- **Token Expiration:** JWT tokens have a configurable expiration time (default: 24 hours)

## Testing Authentication

- To test the authentication flow:
- **Register a user** using the /api/auth/signup endpoint
- Obtain a JWT token by authenticating with /api/auth/login
- Use the token in protected endpoints by including it in the Authorization header:
  ```bash
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

- You can test directly via Swagger UI:
- Login to get a token
- Click the "Authorize" button at the top right
- Enter your token without the prefix "Bearer "
- Now all protected endpoints will include your token in requests

## Contributing

Contributions are welcome! Feel free to fork the repository, create a new branch for your feature or bug fix, and submit a pull request. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.

## Contact

For any inquiries, suggestions, or issues, please contact:

- **Boutchouang Nathan Elija:** boutchouangelija@gmail.com
