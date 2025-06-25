# Blog API

A secure, lightweight REST API for creating and listing blog articles built with Java and Spring Boot.

## Features

- Create new blog articles with title, author, content, and publication date
- List articles with pagination
- View statistics of published articles for the last 7 days (admin only)
- Secure endpoints with Spring Security
- In-memory H2 database for data storage

## Technology Stack

- Java 17
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- H2 Database
- Lombok
- JUnit 5 & Mockito for testing

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation & Running

1. Clone the repository
```
git clone <repository-url>
cd blog
```

2. Build the application
```
mvn clean install
```

3. Run the application
```
mvn spring-boot:run
```

The application will start on port 8080.

## API Endpoints

### Articles

| Method | URL                   | Description                      | Access       |
|--------|---------------------|----------------------------------|--------------|
| GET    | /api/articles        | Get paginated articles           | Public       |
| POST   | /api/articles/create | Create a new article             | Authenticated |

### Statistics

| Method | URL              | Description                              | Access      |
|--------|------------------|------------------------------------------|-------------|
| GET    | /api/statistics  | Get article statistics for last 7 days   | Admin only  |

## Authentication

The application uses HTTP Basic Authentication. There are two pre-configured users:

1. **Regular User**
   - Username: `user`
   - Password: `user123`
   - Role: `ROLE_USER`

2. **Admin User**
   - Username: `admin`
   - Password: `admin123`
   - Role: `ROLE_ADMIN`

## Data Validation

- Title: Required, maximum 100 characters
- Author: Required
- Content: Required
- Publication Date: Required, ISO 8601 format

## Database

The application uses an in-memory H2 database that can be accessed at `/h2-console` with the following credentials:
- JDBC URL: `jdbc:h2:mem:blogdb`
- Username: `sa`
- Password: `password`

## Testing

Run unit tests with:
```
mvn test
```

## License

This project is licensed under the MIT License - see the LICENSE file for details. 