# Address Book Service

A RESTful Address Book API built with Java 21 and Spring Boot.

---

## 🚀 Features

- Manage multiple address books (create, list, delete)
- Add, retrieve, delete contacts within address books
- Support for multiple phone numbers per contact
- Retrieve a unique set of contacts across multiple address books
- H2 in-memory database for demo/testing (no installation required)
- Clean architecture: controller, service, repository, DTO, and mapping layers
- Containerized with Docker for easy deployment
- Comprehensive unit and integration tests
- **Swagger/OpenAPI** documentation
- **Actuator** endpoints for health and monitoring

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot
- H2 Database
- JPA/Hibernate
- MapStruct (DTO mapping)
- JUnit 5 & Spring Boot Test
- Docker

---

## 🏁 Getting Started

### **1. Build and Run Locally**

```bash
# Clone the repository
git clone https://github.com/champ-sukra/address-book-service.git

# Build and run tests
./gradlew build

# Run the application
./gradlew bootRun

# Build docker image
./gradlew clean build -x test
docker build -t address-book-service .

# Run docker container
docker run -p 8080:8080 address-book-service

# JSON API Example
{
  "code": "success",
  "data": { ... }
}

Health check:
http://localhost:8080/address-book-service/actuator/health

Actuator info:
http://localhost:8080/address-book-service/actuator

H2 Console:
http://localhost:8080/address-book-service/h2-console

Swagger/OpenAPI docs:
http://localhost:8080/address-book-service/swagger-ui/index.html#


NOTE
- All API input is validated.
- Standardized error and success responses.
- Designed for clean code, SOLID, and easy testability.

