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
```bash
git clone <repository-url>
cd blog
```

2. Build the application
```bash
mvn clean install
```

3. Run the application
```bash
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

## Testing the API

### 🚀 Quick Demo - Populate Sample Articles

**For Git Bash / Unix terminals (Recommended):**

Copy and paste this entire block to create all 5 articles at once:

```bash
# Create Article 1 - Published today
curl -u user:user123 -X POST "http://localhost:8080/api/articles/create" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Getting Started with Spring Boot",
    "author": "John Developer",
    "content": "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can just run.",
    "publicationDate": "2025-06-25T10:30:00"
  }'

# Create Article 2 - Published yesterday
curl -u user:user123 -X POST "http://localhost:8080/api/articles/create" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "REST API Best Practices",
    "author": "Jane Architect",
    "content": "When designing REST APIs, there are several key principles to follow for maintainability and scalability.",
    "publicationDate": "2025-06-24T14:15:00"
  }'

# Create Article 3 - Published 2 days ago
curl -u admin:admin123 -X POST "http://localhost:8080/api/articles/create" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Database Security Fundamentals",
    "author": "Mike Security",
    "content": "Protecting your database requires a multi-layered approach including encryption, access controls, and monitoring.",
    "publicationDate": "2025-06-23T09:45:00"
  }'

# Create Article 4 - Published 3 days ago
curl -u user:user123 -X POST "http://localhost:8080/api/articles/create" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Microservices Architecture Guide",
    "author": "Sarah Cloud",
    "content": "Microservices architecture has become the standard for building scalable applications in modern development.",
    "publicationDate": "2025-06-22T16:20:00"
  }'

# Create Article 5 - Published 4 days ago
curl -u admin:admin123 -X POST "http://localhost:8080/api/articles/create" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Testing Strategies for Modern Applications",
    "author": "Tom Tester",
    "content": "Comprehensive testing strategies include unit tests, integration tests, and end-to-end testing frameworks.",
    "publicationDate": "2025-06-21T11:00:00"
  }'
```

**For Windows Command Prompt (cmd):**

```cmd
curl -u user:user123 -X POST "http://localhost:8080/api/articles/create" -H "Content-Type: application/json" -d "{\"title\": \"Getting Started with Spring Boot\", \"author\": \"John Developer\", \"content\": \"Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can just run.\", \"publicationDate\": \"2025-06-25T10:30:00\"}" && curl -u user:user123 -X POST "http://localhost:8080/api/articles/create" -H "Content-Type: application/json" -d "{\"title\": \"REST API Best Practices\", \"author\": \"Jane Architect\", \"content\": \"When designing REST APIs, there are several key principles to follow for maintainability and scalability.\", \"publicationDate\": \"2025-06-24T14:15:00\"}" && curl -u admin:admin123 -X POST "http://localhost:8080/api/articles/create" -H "Content-Type: application/json" -d "{\"title\": \"Database Security Fundamentals\", \"author\": \"Mike Security\", \"content\": \"Protecting your database requires a multi-layered approach including encryption, access controls, and monitoring.\", \"publicationDate\": \"2025-06-23T09:45:00\"}" && curl -u user:user123 -X POST "http://localhost:8080/api/articles/create" -H "Content-Type: application/json" -d "{\"title\": \"Microservices Architecture Guide\", \"author\": \"Sarah Cloud\", \"content\": \"Microservices architecture has become the standard for building scalable applications in modern development.\", \"publicationDate\": \"2025-06-22T16:20:00\"}" && curl -u admin:admin123 -X POST "http://localhost:8080/api/articles/create" -H "Content-Type: application/json" -d "{\"title\": \"Testing Strategies for Modern Applications\", \"author\": \"Tom Tester\", \"content\": \"Comprehensive testing strategies include unit tests, integration tests, and end-to-end testing frameworks.\", \"publicationDate\": \"2025-06-21T11:00:00\"}"
```

### 📋 Test Article Listing (Public)

```bash
# Get all articles (paginated)
curl "http://localhost:8080/api/articles"

# Get articles with custom pagination
curl "http://localhost:8080/api/articles?page=0&size=3"
```

### 📊 Test Statistics (Admin Only)

```bash
# Get statistics for last 7 days (admin only)
curl -u admin:admin123 "http://localhost:8080/api/statistics"

# Regular user should get 403 Forbidden
curl -u user:user123 "http://localhost:8080/api/statistics"
```

### 🧪 Test Validation

```bash
# Test title too long (should fail)
curl -u user:user123 -X POST "http://localhost:8080/api/articles/create" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "This title is way too long and exceeds the 100 character limit which should trigger a validation error in the API response",
    "author": "Test Author",
    "content": "Test content",
    "publicationDate": "2025-06-25T12:00:00"
  }'

# Test missing required fields (should fail)
curl -u user:user123 -X POST "http://localhost:8080/api/articles/create" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "",
    "author": "Test Author",
    "content": "Test content"
  }'
```

## Data Validation

- Title: Required, maximum 100 characters
- Author: Required
- Content: Required
- Publication Date: Required, ISO 8601 format

## Database

The application uses an in-memory H2 database that can be accessed at `/h2-console` with the following credentials:
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:blogdb`
- **Username**: `sa`
- **Password**: `password`

## Testing

Run unit tests with:
```bash
mvn test
```

## Sample API Responses

### Article Creation Response
```json
{
  "id": 1,
  "title": "Getting Started with Spring Boot",
  "author": "John Developer",
  "content": "Spring Boot makes it easy to create...",
  "publicationDate": "2025-06-25T10:30:00",
  "createdAt": "2025-06-25T18:46:25.820",
  "updatedAt": null
}
```

### Article List Response
```json
{
  "content": [
    {
      "id": 1,
      "title": "Getting Started with Spring Boot",
      "author": "John Developer",
      "content": "Spring Boot makes it easy...",
      "publicationDate": "2025-06-25T10:30:00",
      "createdAt": "2025-06-25T18:46:25.820",
      "updatedAt": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

### Statistics Response
```json
[
  {"date": "2025-06-25", "count": 2},
  {"date": "2025-06-24", "count": 1},
  {"date": "2025-06-23", "count": 1},
  {"date": "2025-06-22", "count": 1},
  {"date": "2025-06-21", "count": 1},
  {"date": "2025-06-20", "count": 0},
  {"date": "2025-06-19", "count": 0}
]
```

## License

This project is licensed under the MIT License - see the LICENSE file for details. 