# Park REST API

This project is a RESTful API for managing a parking system, developed using Java and Spring Boot. It provides endpoints for user management, including creation, retrieval, and password updates, with role-based access control. The API is secured using JWT (JSON Web Tokens) and documented using Swagger/OpenAPI.

## 🚀 Features

-   **User Management**: CRUD operations for users.
-   **Authentication**: Secure authentication process using JWT.
-   **Authorization**: Role-based access control for different endpoints (ADMIN, CLIENT).
-   **API Documentation**: Interactive API documentation with Swagger UI.
-   **Exception Handling**: Global exception handling for clear and consistent error responses.
-   **Auditing**: Tracks creation and modification dates and the users who made the changes.

## ⚙️ Technologies Used

-   **Java 21**
-   **Spring Boot 3.3.1**: Main framework for building the application.
-   **MySQL**: Relational database for production.
-   **H2 Database**: In-memory database for testing.
-   **Maven**: Dependency management.
-   **Lombok**: To reduce boilerplate code.
-   **ModelMapper**: For object mapping between DTOs and entities.
-   **JWT**: For creating and validating JSON Web Tokens.

## API Endpoints

The API is available under the base path `/api/v1`. The documentation for all endpoints is available via Swagger UI at `/docs-park.html`.

### Authentication

-   `POST /api/v1/auth`: Authenticates a user and returns a JWT token.

### User Management

-   `POST /api/v1/users`: Creates a new user.
-   `GET /api/v1/users/{id}`: Retrieves a user by their ID.
    -   Requires `ADMIN` role, or `CLIENT` role if the user is accessing their own data.
-   `GET /api/v1/users`: Retrieves a list of all users.
    -   Requires `ADMIN` role.
-   `PATCH /api/v1/users/{id}`: Updates the password for a user.
    -   Requires `ADMIN` or `CLIENT` role, but users can only change their own password.
