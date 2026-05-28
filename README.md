# Java Spring Boot Coding Exercise

## Important Information
You will receive the coding exercise via email 20 minutes before the interview. Please ensure you download this repository and create a new one for your modifications, which will be committed during the interview.

## Running the Application
To run the Spring Boot application and access the H2 console, follow these steps:

1. **Start the Spring Boot application.**
2. **Access the H2 console**:
   - Navigate to [http://localhost:8080/h2-console](http://localhost:8080/h2-console).
   - Use the following JDBC URL: `jdbc:h2:mem:testdb`.
   - User Name: `sa`
   - Password: `password`

The database schema is created based on the file **src/main/resources/schema.sql**.

## 1. Project Changes

This document details the architecture, features, and changes implemented in the **SecureFlight API**. \The project's goal is to manage an airline's flights and passengers, featuring a robust authentication and authorization system for company staff.

Recent changes have focused on code refactoring, implementing development best practices, enhancing security, and adding new features, such as managing passengers on flights.

## 2. Architecture and Package Structure

The project has been reorganized to follow a cleaner architecture with better separation of concerns. The main structure is divided into two high-level packages:

-   **`api`**: Contains all classes related to business logic and API exposure, such as:
    -   `controllers`: Responsible for receiving HTTP requests and returning responses.
    -   `services`: Where the core business logic resides.
    -   `entities`: Classes that map to database tables.
    -   `repositories`: Spring Data JPA interfaces for database access.
    -   `dto` (Data Transfer Objects): Used to transfer data between the client and the controller.
    -   `presenter`: Classes responsible for mapping Entities to DTOs and vice versa.
    -   `validations`: Business rule validation classes (using the Strategy Pattern).

-   **`infrastructure`**: Contains infrastructure configurations and cross-cutting concerns, such as:
    -   `security`: Spring Security, JWT, and authorization filter configurations.
    -   `swagger`: API documentation configuration with Swagger/OpenAPI.
    -   `exceptions`: Custom exception classes and the global `ExceptionHandler`.

## 3. Data Model

The system uses the following main tables:

-   **`users`**: Stores data for airline employees who use the system.
-   **`passenger`**: Stores data for the airline's customers (passengers).
-   **`flight_passengers`**: A join table that links a passenger to a specific flight.

### Initial Data (Seed)

To facilitate usage and testing, the database is initialized with the following user accounts.

| Username | Password      | Role(s) |
| :--- |:--------------| :--- |
| `admin` | `password123` | `ADMIN` |
| `operator` | `password123`    | `OPERATOR` |
| `agent` | `password123`       | `AGENT` |

Two pre-registered passengers are also added for demonstration purposes.

## 4. API Endpoints

Below are the main implemented endpoints.

-   `POST /authentication/login`
    -   **Description**: Receives a `login` and `password`. If valid, it returns a JWT for authorization.

-   `GET /flights`
    -   **Description**: Returns a paginated list of all registered flights.

-   `GET /flights/all`
    -   **Description**: Returns a complete, non-paginated list of all flights.

-   `GET /flights/{flightId}/passengers`
    -   **Description**: Returns a flight's details, including its list of passengers.

-   `POST /flights/{flightId}/add/passengers`
    -   **Description**: Assigns a passenger to the specified flight.

-   `DELETE /flights/{flightId}/remove/passengers`
    -   **Description**: Removes a passenger from the specified flight.

-   `GET /passengers`
    -   **Description**: Returns a list of all registered passengers.

## 5. Authorization (Role-Based Access)

Access to the API endpoints is restricted based on user roles. The table below outlines the required role for each endpoint.

| Endpoint | Method | ADMIN | OPERATOR | AGENT |
| :--- | :--- | :---: | :---: | :---: |
| `/authentication/login` | `POST` | ✅ | ✅ | ✅ |
| `/flights` | `GET` | ✅ | ✅ | ✅ |
| `/flights/all` | `GET` | ✅ | ✅ | ✅ |
| `/flights/{id}` | `GET` | ✅ | ✅ | ✅ |
| `/flights/{id}` | `PUT` | ✅ | ✅ | ❌ |
| `/flights/{flightId}/passengers` | `GET` | ✅ | ✅ | ✅ |
| `/flights/{flightId}/add/passengers` | `POST` | ✅ | ❌ | ✅ |
| `/flights/{flightId}/remove/passengers` | `DELETE` | ✅ | ❌ | ✅ |
| `/passengers` | `GET` | ✅ | ❌ | ✅ |

*(`✅` = Allowed, `❌` = Denied)*

## 6. Implemented Changes and Improvements

### 6.1. Security (JWT and RBAC)

A security layer was implemented using **Spring Security** and **OAuth2 Resource Server**.

-   **JWT Authentication**: The login endpoint generates a JWT signed with a public/private key pair, ensuring the token's authenticity.
-   **Authorization (RBAC)**: The system uses a Role-Based Access Control model. The JWT contains the user's `roles`, which are used to authorize or deny access to endpoints based on each role's permissions.

### 6.2. Validations and Exception Handling

-   **Validation Strategy Pattern**: The Strategy pattern was implemented for business rule validations, promoting low coupling and making rules easy to reuse and extend.
-   **Bean Validation**: The `spring-boot-starter-validation` dependency was added to validate request bodies (`@RequestBody`) using annotations (`@NotNull`, `@NotEmpty`, etc.).
-   **Custom Exceptions and Handler**: `SecureFlightException` and `SecureFlightNotFoundException` were created to handle custom errors. A `GlobalExceptionHandler` formats these exceptions into a standardized JSON response.

### 6.3. Refactoring and Best Practices

-   **Use of DTOs**: All endpoints now return DTOs to avoid exposing database entities. `Mapper` classes handle the conversion.
-   **Bug Fixes**: Business logic was moved from controllers to the service layer. All controllers now return `ResponseEntity` for finer control over HTTP status codes.
-   **Lombok**: The library was added to reduce boilerplate code.
-   **Read-Only Transactions**: The `@Transactional(readOnly = true)` annotation was added to read-only service methods to optimize database performance.
-   **Unit Tests**: Implemented unit tests with Mockito and JUnit 5, achieving over 95% code coverage in the API and business rule layers.

### 6.4. New Features

-   **Passenger Management on Flights**: Specific services (`AddPassengerService`, `RemovePassengerService`) were created to isolate the responsibility of adding and removing passengers from a flight.

## 7. Tooling and Execution

-   **Swagger (OpenAPI)**: The project includes interactive API documentation generated by Swagger. It can be accessed at `http://localhost:8082/swagger-ui.html` and includes DTO examples.
-   **Docker & Docker Compose**: To streamline deployment and ensure a consistent environment, the project includes a `Dockerfile` and a `docker-compose.yml` file.
    -   **Prerequisite**: You must have Docker and Docker Compose installed on your machine (e.g., by installing Docker Desktop).
    -   To run the entire application, navigate to the project root directory in your terminal and execute:
        ```sh
        docker-compose up
        ```
    -   This command will build the Docker image and start the application container. The API, including the H2 database and Swagger, will be available on port `8082`.
-   **Postman**: A Postman collection has been added to the project to facilitate testing. It is located in the [collection-postman](collection-postman) folder at the root of the project and contains all configured endpoints. (postman collection is configured to make calls on port 8082)

## 8. Suggestions for Future Improvements

1.  **Single-Responsibility Controllers**: Refactor controllers so that each has a single method (the "Action" pattern).
2.  **Adoption of the Use Case Pattern**: Expand the specific-service pattern to the entire application.
3.  **Domain Entities vs. Database Entities**: Introduce a separation between domain entities and persistence entities.
4.  **Swagger Annotations to interfaces**: Move the swagger annotation from controllers to interfaces to make controllers more clean. 
5.  **Implement Full CRUD Endpoints**: To provide more comprehensive administrative control, the following CRUD (Create, Read, Update, Delete) endpoints could be added:
    -   **Passenger Management**:
        -   `POST /passengers`: To create a new passenger.
        -   `PUT /passengers/{id}`: To update an existing passenger's details.
        -   `DELETE /passengers/{id}`: To delete a passenger.
    -   **User Management**:
        -   `POST /users`: To create a new system user (e.g., agent, operator).
        -   `PUT /users/{id}`: To update a user's details or roles.
        -   `DELETE /users/{id}`: To delete a user.
    -   **Flight Management**:
    -   `POST /flights`: To create a new flight.
    -   `DELETE /flights/{id}`: To delete a flight.

## 9. Design Notes

The decision to create the `passenger` table separately from the `users` table was made because passengers and system employees are distinct entities. An alternative would be to treat passengers as a type of `user` with a `PASSENGER` role, which would allow them to log in and access specific endpoints. The choice depends on the desired business rules.